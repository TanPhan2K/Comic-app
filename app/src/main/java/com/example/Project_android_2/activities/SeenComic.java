package com.example.Project_android_2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.Project_android_2.R;
import com.example.Project_android_2.activities.Fragment_Seencomic.SeenComicAdapter;

import com.example.Project_android_2.utils.NetworkUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SeenComic extends AppCompatActivity {
    private AppCompatImageView appCompatImageView_back;
    private ImageView imageViewLogo;
    private Button btnConfirm;
    private TextView txtview_Title, category, txtview_Author;
    private RecyclerView rc_Chapter;

    private DatabaseReference databaseReference;
    private LinearLayout ll_info_chapter, stub_no_internet;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button try_again_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seencomic);

        // Tham chiếu các thành phần giao diện
        appCompatImageView_back = findViewById(R.id.back);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.nav_viewcomic);
        category = findViewById(R.id.category);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        btnConfirm = findViewById(R.id.confirm_button);
        txtview_Title = findViewById(R.id.textViewTitle);
        ll_info_chapter = findViewById(R.id.ll_info_chapter);
        stub_no_internet = findViewById(R.id.stub_no_internet);
        txtview_Author = findViewById(R.id.textViewauthor); // Thêm tham chiếu cho txtview_Author
        try_again_button = findViewById(R.id.try_again_button);
        try_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeenComic.this,Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra kết nối mạng sau 2 giây
                if (!NetworkUtils.isNetworkAvailable(SeenComic.this)) {
                    ll_info_chapter.setVisibility(View.GONE);
                    stub_no_internet.setVisibility(View.VISIBLE);
                    RelativeLayout headerTitle = findViewById(R.id.header_title);
                    headerTitle.setBackgroundResource(0);
                    Toast.makeText(SeenComic.this, "Không có kết nối Internet. Vui lòng kiểm tra cài đặt mạng của bạn.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ll_info_chapter.setVisibility(View.VISIBLE);
                    stub_no_internet.setVisibility(View.GONE);
                }

            }
        }, 2000);
        // Khởi tạo adapter cho ViewPager
        Bundle bundle = getIntent().getExtras();

        String idcomic = bundle.getString("ID_COMIC");
        SeenComicAdapter adapter = new SeenComicAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, idcomic);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ComicUI(idcomic);


        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference("chapter");
                Query query = databaseReference.orderByChild("ID_COMIC").equalTo(idcomic);

                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            for (DataSnapshot chapterSnapshot : snapshot.getChildren()) {
                                String id = chapterSnapshot.getKey();
                                String title = chapterSnapshot.child("TITLE").getValue(String.class);
                                Long chapterIndex = chapterSnapshot.child("CHAPTER_INDEX").getValue(Long.class);
                                if (chapterIndex != null && chapterIndex == 1) {
                                    String index = chapterIndex != null ? String.valueOf(chapterIndex) : "";
                                    // Lấy thông tin của chương đầu tiên và truyền vào ViewSee activity
                                    Intent intent = new Intent(SeenComic.this, ViewComic.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ID", id);
                                    bundle.putString("CHAPTER_INDEX", index);
                                    bundle.putString("ID_COMIC", idcomic);
                                    bundle.putString("TITLE", title);
                                    bundle.putInt("SIZE", (int) snapshot.getChildrenCount()); // Sử dụng số lượng chương từ snapshot
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                                }

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
        });
    }

    public void ComicUI(String idcomic) {

        databaseReference = FirebaseDatabase.getInstance().getReference("comic");

        if (idcomic != null) {
            Query query = databaseReference.orderByChild("ID").equalTo(idcomic);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot comicSnapshot : snapshot.getChildren()) {
                            String image = comicSnapshot.child("THUMBNAIL").getValue(String.class);
                            String name_comic = comicSnapshot.child("TITLE").getValue(String.class);
                            String author = comicSnapshot.child("AUTHOR").getValue(String.class);
                            String id = comicSnapshot.child("ID").getValue(String.class);
                            Picasso.get().load(image).into(imageViewLogo);
                            txtview_Title.setText(name_comic);
                            getAuthor(author);
                            getID_comic_with_category(id);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void getID_comic_with_category(String ID_category) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cateRef = database.getReference("comic_category");
        Query query = cateRef.orderByChild("ID_COMIC").equalTo(ID_category);
        query.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot comic_cate_snap : snapshot.getChildren()) {
                    String title_category = comic_cate_snap.child("TITLE_CATEGORY").getValue(String.class);
                    category.setText(title_category);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAuthor(String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference("author").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String author = snapshot.child("NAME").getValue(String.class);
                    txtview_Author.setText("bởi " + author);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}