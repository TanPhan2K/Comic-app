package com.example.Project_android_2.activities;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Project_android_2.R;

import com.example.Project_android_2.activities.RC_recyclerView.RCAdapter_Trending;
import com.example.Project_android_2.activities.RC_recyclerView.chapter_model;
import com.example.Project_android_2.activities.RC_recyclerView.comic_chapter_model;
import com.example.Project_android_2.activities.RC_recyclerView.comic_model;
import com.example.Project_android_2.utils.NetworkUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView2;
    RCAdapter_Trending rcAdapter2;
    ArrayList<comic_chapter_model> comic_chapter = new ArrayList<>();
    ArrayList<chapter_model> lastchapter = new ArrayList<>();
    ImageView Search_icon;
    DrawerLayout drawerLayout;
    RoundedImageView buttonDrawerToggle, imageViewDrawer;
    TextView username, useremail;
    private ALodingDialog aLodingDialog;
    private LinearLayout listRcCateLayout, noInternetLayout;
    private Button try_again_button;
    private boolean shouldExit = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userUid = sharedPreferences.getString("userUid", "");
        String userPhotoUrl = sharedPreferences.getString("photoUrl", "");
        String userName = sharedPreferences.getString("userName", "");
        String userEmail = sharedPreferences.getString("userEmail", "");

        buttonDrawerToggle = findViewById(R.id.imageUser);
        listRcCateLayout = findViewById(R.id.id_list_rc_cate);
        noInternetLayout = findViewById(R.id.stub_no_internet);
        try_again_button = findViewById(R.id.try_again_button);
        try_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_logout) {
                    // Kết thúc Activity Home
                    finish();
                    // Tạo Intent để chuyển đến AccountManagement
                    Intent intent = new Intent(Home.this, AccountManagement.class);
                    // Gửi thông điệp yêu cầu đăng xuất
                    intent.putExtra("logout", true);
                    startActivity(intent);
                    // Đóng drawer sau khi xử lý sự kiện
                    DrawerLayout drawer = findViewById(R.id.drawerLayout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                if (id == R.id.nav_setting) {
                    Intent intentProfile = new Intent(Home.this, User.class);
                    startActivity(intentProfile);
                    return true;
                }
                if (id == R.id.nav_home) {
                    recreate();
                    DrawerLayout drawer = findViewById(R.id.drawerLayout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                if (id == R.id.nav_about) {
                    Intent intentProfile = new Intent(Home.this, AboutUs.class);
                    startActivity(intentProfile);
                    return true;
                }
                return false;
            }
        });


        Glide.with(this).load(userPhotoUrl).into(buttonDrawerToggle);
        // Khởi tạo aLodingDialog
        aLodingDialog = new ALodingDialog(this);
        getListChapter();

        handleSearch();
        handleslidermenu(userPhotoUrl, userName, userEmail);
        //  handleswitch();
    }

    public void getListChapter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chapterRef = database.getReference("chapter");
        // Hiển thị aLodingDialog
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }

        chapterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            final ArrayList<chapter_model> list_chapter = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot chaptershot : snapshot.getChildren()) {
                    int chapter_index = chaptershot.child("CHAPTER_INDEX").getValue(Integer.class);
                    String Content = chaptershot.child("CONTENT").getValue(String.class);
                    String id_comic = chaptershot.child("ID_COMIC").getValue(String.class);
                    String title = chaptershot.child("TITLE").getValue(String.class);
                    String create_at = chaptershot.child("create_at").getValue(String.class);
                    int has_html = chaptershot.child("has_html").getValue(Integer.class);
                    String id = chaptershot.getKey();
                    chapter_model model = new chapter_model(id, chapter_index, Content, id_comic, title, create_at, has_html);
                    list_chapter.add(model);
                }
                if (!list_chapter.isEmpty()) {
                    getCommit(list_chapter);
                } else
                    Toast.makeText(Home.this, "Không thể tìm thấy danh sách truyện tranh có bản cập nhật mới nhất.", Toast.LENGTH_SHORT).show();
                // Ẩn aLodingDialog khi dữ liệu đã được tải xong
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "Không có dữ liệu truyện tranh nào.", Toast.LENGTH_SHORT).show();
                // Ẩn aLodingDialog khi dữ liệu đã được tải xong
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
            }
        });

        // Sử dụng Handler để đặt hẹn giờ
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra kết nối mạng sau 2 giây
                if (!NetworkUtils.isNetworkAvailable(Home.this)) {
                    // Nếu không có kết nối mạng, ẩn aLodingDialog và hiển thị thông báo
                    if (aLodingDialog != null && aLodingDialog.isShowing()) {
                        aLodingDialog.dismiss();
                    }

                    listRcCateLayout.setVisibility(View.GONE);
                    noInternetLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(Home.this, "Không có kết nối Internet. Vui lòng kiểm tra cài đặt mạng của bạn.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    listRcCateLayout.setVisibility(View.VISIBLE);
                    noInternetLayout.setVisibility(View.GONE);
                }

            }
        }, 2000);
    }

    private void getCommit(ArrayList<chapter_model> list_chapter) {
        //       Toast.makeText(Home.this,listChapter.get(0).getID_COMIC()+" "+listChapter.get(0).getTITLE(),Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference comicsRef = database.getReference("comic");
        // Hiển thị aLodingDialog
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        comicsRef.addValueEventListener(new ValueEventListener() {
            ArrayList<comic_model> arr_comic = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot comicsnap : snapshot.getChildren()) {
                    comic_model model = comicsnap.getValue(comic_model.class);
                    arr_comic.add(model);
                }
                create_list_comic_chapter(list_chapter, arr_comic);
                // Ẩn aLodingDialog khi dữ liệu đã được tải xong
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
                // Toast.makeText(Home.this,arr_comic.get(0).getTITLE(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "Không có dữ liệu truyện tranh nào.", Toast.LENGTH_SHORT).show();
                // Ẩn aLodingDialog khi dữ liệu đã được tải xong
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
            }
        });
    }

    public void create_list_comic_chapter(ArrayList<chapter_model> listChapter, ArrayList<comic_model> arr_comic) {
        // Hiển thị aLodingDialog
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }

        try {
            HashMap<String, chapter_model> lastChapterMap = new HashMap<>();
            // Xóa hết các phần tử cũ trong lastChapters
            if (!lastchapter.isEmpty()) {
                lastchapter.clear();
            }
            // Lặp qua danh sách các chapter
            for (chapter_model chapter : listChapter) {
                // Lấy id_comic của chapter
                String id_comic = chapter.getID_COMIC();

                // Lấy chapter cuối cùng của id_comic từ lastChapterMap, nếu có
                chapter_model lastChapter = lastChapterMap.get(id_comic);

                // Nếu không có chapter cuối cùng hoặc chapter hiện tại mới hơn
                if (lastChapter == null || lastChapter.getCreate_At() == null ||
                        (chapter.getCreate_At() != null && chapter.getCreate_At().compareTo(lastChapter.getCreate_At()) > 0)) {
                    // Thêm chapter vào lastChapterMap
                    lastChapterMap.put(id_comic, chapter);
                }
            }

            // Tạo một ArrayList mới chỉ chứa các chapter cuối cùng đã được lựa chọn
            lastchapter.addAll(lastChapterMap.values());

            for (chapter_model chapter : lastchapter) {
                String id_comic = chapter.getID_COMIC();
                for (comic_model comic : arr_comic) {
                    if (id_comic.equals(comic.getID())) {
                        comic_chapter_model md = new comic_chapter_model(chapter.getID_COMIC(), comic.getTITLE(), chapter.getCHAPTER_INDEX(), comic.getTHUMBNAIL());
                        comic_chapter.add(md);
                        break;
                    }
                }
            }
            createList_story();
            // Ẩn aLodingDialog khi dữ liệu đã được tải xong
            if (aLodingDialog != null && aLodingDialog.isShowing()) {
                aLodingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra log lỗi nếu có
            // Xử lý các ngoại lệ nếu cần
        }
    }

    @SuppressLint({"WrongViewCast", "NotifyDataSetChanged"})
    public void createList_story() {
        // Hiển thị aLodingDialog
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        if (!comic_chapter.isEmpty()) {
            recyclerView2 = findViewById((R.id.rc_story));
            recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView2.setHasFixedSize(true);
            rcAdapter2 = new RCAdapter_Trending(this, comic_chapter);
            recyclerView2.setAdapter(rcAdapter2);
            rcAdapter2.notifyDataSetChanged();
            rcAdapter2.setOnItemClickListener(new RCAdapter_Trending.OnItemClickListener() {
                @Override
                public void onItemClick(comic_chapter_model position) {
                    Intent intent = new Intent(Home.this, SeenComic.class);
                    intent.putExtra("ID_COMIC", position.getId_Comic());
                    startActivity(intent);
                }
            });
        }

        // Ẩn aLodingDialog khi RecyclerView đã được hiển thị hoàn chỉnh
        if (aLodingDialog != null && aLodingDialog.isShowing()) {
            aLodingDialog.dismiss();
        }
    }

    @SuppressLint("ResourceType")
    public void handleSearch() {
        Search_icon = findViewById(R.id.imageSearch);
        Search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SearchComic.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("WrongViewCast")
    public void handleslidermenu(String userPhotoUrl, String userName, String userEmail) {
        drawerLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle.setOnClickListener(v -> {
            drawerLayout.open();
            imageViewDrawer = drawerLayout.findViewById(R.id.imageUserViewDrawer);
            username = drawerLayout.findViewById(R.id.username);
            useremail = drawerLayout.findViewById(R.id.userEmail);
            Glide.with(Home.this).load(userPhotoUrl).into(imageViewDrawer);
            username.setText(userName);
            useremail.setText(userEmail);
        });
    }


    @Override
    public void onBackPressed() {
        if (shouldExit) {
            super.onBackPressed();
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogView);

        // Lấy các nút OK và Cancel từ layout
        Button buttonCancel = dialogView.findViewById(R.id.dialog_button_cancel);
        Button buttonOK = dialogView.findViewById(R.id.dialog_button_ok);

        // Tạo và hiển thị AlertDialog
        AlertDialog alertDialog = builder.create();

        // Thiết lập sự kiện cho nút Cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // Thiết lập sự kiện cho nút OK
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishAffinity();
            }
        });
        alertDialog.show();
    }

}

//    public void handleswitch(){
//        switchmode = findViewById(R.id.switchMode);
//        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
//        nightMode = sharedPreferences.getBoolean("nightMode",false);
//        if(nightMode){
//            switchmode.setChecked(true);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }
//        switchmode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(nightMode){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    editor = sharedPreferences.edit();
//                    editor.putBoolean("nightMode",false);
//                }else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    editor = sharedPreferences.edit();
//                    editor.putBoolean("nightMode",true);
//                }
//                editor.apply();
//            }
//        });
//    }