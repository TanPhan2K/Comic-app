package com.example.Project_android_2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Project_android_2.R;

import com.example.Project_android_2.activities.RC_recyclerView.RCAdapter;
import com.example.Project_android_2.activities.RC_recyclerView.RCAdapter_type_two;
import com.example.Project_android_2.activities.RC_recyclerView.author_comic_model;
import com.example.Project_android_2.activities.RC_recyclerView.author_model;
import com.example.Project_android_2.activities.RC_recyclerView.category_model;
import com.example.Project_android_2.activities.RC_recyclerView.comic_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchComic extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    RecyclerView rc_of_detail_cato;
    RCAdapter rcAdapter;
    RCAdapter_type_two rcAdapterTypeTwo;
    private ALodingDialog aLodingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        aLodingDialog = new ALodingDialog(this);
        AppCompatImageView appcompatBack_home = findViewById(R.id.back_home);
        handleback_home(appcompatBack_home);
        getList_cate_on_firebase();
        EditText editSeach = findViewById(R.id.Search_comic);

        setupEditText(editSeach);
        getdata_fire_with_seachtext("");
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String editSeach_ = editSeach.getText().toString();


                if (editSeach_.isEmpty()) {
                    getdata_fire_with_seachtext("");
                }
            }
        };

        editSeach.addTextChangedListener(textWatcher);
    }

    /* ------------------------------- phần xử lý back home ---------------------------------------- */
    public void handleback_home(AppCompatImageView appcompatback_home) {
        appcompatback_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchComic.this, Home.class);
                onBackPressed();
                startActivity(intent);
            }
        });
    }

    /* -------------------------------- phần xử lý search comic ------------------------------------ */
    public void setupEditText(EditText edittext) {
        try {
            edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        // Thực hiện hành động khi người dùng nhấn IME_ACTION_SEARCH hoặc Enter
                        getdata_fire_with_seachtext(edittext.getText().toString().trim());
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getdata_fire_with_seachtext(String textseach) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference comicRef = database.getReference("comic");

        // Hiển thị aLodingDialog
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }

        comicRef.addValueEventListener(new ValueEventListener() {
            ArrayList<comic_model> query_comic = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                query_comic.clear(); // Clear the list before adding new results
                for (DataSnapshot comicshot : snapshot.getChildren()) {
                    comic_model model = comicshot.getValue(comic_model.class);
                    if (model != null && model.getTITLE() != null) {
                        String title = model.getTITLE().toLowerCase();
                        if (textseach.isEmpty()) {
                            // Nếu textseach rỗng, thêm tất cả các mục vào danh sách
                            query_comic.add(model);
                        } else {
                            String[] words = title.split("\\s+"); // Tách tiêu đề thành các từ riêng lẻ
                            for (String word : words) {
                                if (word.startsWith(textseach.toLowerCase())) { // So sánh từng từ với từ khóa tìm kiếm
                                    query_comic.add(model);
                                    break; // Chỉ cần thêm một lần nếu có sự trùng khớp
                                }
                            }
                        }
                    }
                }
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
                if (!query_comic.isEmpty()) {
                    getAuthor(query_comic);
                } else {
                    Toast.makeText(SearchComic.this, "Không thể tìm kiếm truyện tranh.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
                Toast.makeText(SearchComic.this, "Lỗi tìm kiếm truyện tranh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*------------------------ phần xử lý recyclerview firebase category---------------------------*/
    public void getList_cate_on_firebase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cateRef = database.getReference("category");

        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        cateRef.addValueEventListener(new ValueEventListener() {
            ArrayList<category_model> modelListcate = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cateshot : snapshot.getChildren()) {
                    category_model model = cateshot.getValue(category_model.class);
                    modelListcate.add(model);
                }
                if (!modelListcate.isEmpty()) {
                    createList_cate(modelListcate);
                } else
                    Toast.makeText(SearchComic.this, "Không tìm thấy thể loại trong dữ liệu.", Toast.LENGTH_SHORT).show();
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
                Toast.makeText(SearchComic.this, "Không thể tìm thấy dữ liệu.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createList_cate(ArrayList<category_model> modelListcate) {
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        if (!modelListcate.isEmpty()) {
            recyclerView = findViewById(R.id.rc_View);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            rcAdapter = new RCAdapter(this, modelListcate, this);
            recyclerView.setAdapter(rcAdapter);
            rcAdapter.notifyDataSetChanged();
//            getID_comic_with_category(modelListcate.get(0).getID());
        }
        if (aLodingDialog != null && aLodingDialog.isShowing()) {
            aLodingDialog.dismiss();
        }
    }

    /*------------------------ phần xử lý recyclerview firebase category---------------------------*/
    /*------------------------ phần xử lý recyclerview firebase comic---------------------------*/
    public void getID_comic_with_category(String ID_category) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cateRef = database.getReference("comic_category");
        Query query = cateRef.orderByChild("ID_CATEGORY").equalTo(ID_category);
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        query.addValueEventListener(new ValueEventListener() {
            ArrayList<String> ID_comic = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot comic_cate_snap : snapshot.getChildren()) {
                    String id_comic = comic_cate_snap.child("ID_COMIC").getValue(String.class);
                    ID_comic.add(id_comic);
                }
                if (!ID_comic.isEmpty()) {
                    getComic_from_ID_comic(ID_comic);
                } else {
                    handleEmptyComicList();
                }
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
                Toast.makeText(SearchComic.this, "Không thể lấy truyện tranh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getComic_from_ID_comic(ArrayList<String> ID_comics) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference comicRef = database.getReference("comic");
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        comicRef.addValueEventListener(new ValueEventListener() {
            ArrayList<comic_model> comicmodel = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    comic_model model = snap.getValue(comic_model.class);
                    comicmodel.add(model);
                }
                ArrayList<comic_model> new_comic = new ArrayList<>();
                for (comic_model kt : comicmodel) {
                    if (ID_comics.contains(kt.getID()))
                        new_comic.add(kt);
                }
                getAuthor(new_comic);
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
                Toast.makeText(SearchComic.this, "Không thể tìm thấy truyện tranh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleEmptyComicList() {
        // Bạn cũng có thể cập nhật RecyclerView để hiển thị trạng thái rỗng
        if (rc_of_detail_cato != null) {
            rc_of_detail_cato.setAdapter(null);
        }
    }

    public void getAuthor(ArrayList<comic_model> new_comic) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference authorRef = database.getReference("author");
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        authorRef.addValueEventListener(new ValueEventListener() {
            ArrayList<author_model> author_arr = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String ID_author = snap.getKey();
                    String info = snap.child("INFO").getValue(String.class);
                    String name = snap.child("NAME").getValue(String.class);
                    String create_At = snap.child("create_At").getValue(String.class);
                    author_model model = new author_model(ID_author, info, name, create_At);
                    author_arr.add(model);
                }
                ArrayList<author_comic_model> au_co_model = new ArrayList<>();
                ArrayList<String> ID_authors = new ArrayList<>();
                for (comic_model query : new_comic) {
                    ID_authors.add(query.getAUTHOR());
                }
                for (author_model query : author_arr) {
                    if (ID_authors.contains(query.getID_author())) {
                        comic_model model = getName_comic(new_comic, query.getID_author());
                        author_comic_model au_co = new author_comic_model(model.getID(), model.getTITLE(), query.getID_author(), query.getNAME(), model.getTHUMBNAIL());
                        au_co_model.add(au_co);
                    }
                }
                createList_cate_two(au_co_model);
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (aLodingDialog != null && aLodingDialog.isShowing()) {
                    aLodingDialog.dismiss();
                }
                Toast.makeText(SearchComic.this, "Không thể tìm thấy tác giả", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public comic_model getName_comic(ArrayList<comic_model> comic_md, String ID_author) {
        comic_model model = new comic_model();
        for (comic_model kt : comic_md) {
            if (kt.getAUTHOR().equals(ID_author)) {
                model.setTITLE(kt.getTITLE());
                model.setID(kt.getID());
                model.setTHUMBNAIL(kt.getTHUMBNAIL());
            }
        }
        return model;
    }

    public void createList_cate_two(ArrayList<author_comic_model> new_comic) {
        if (aLodingDialog != null) {
            aLodingDialog.show();
        }
        if (!new_comic.isEmpty()) {
            rc_of_detail_cato = findViewById(R.id.rc_comic_two);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            rc_of_detail_cato.setLayoutManager(layoutManager);
            rc_of_detail_cato.setHasFixedSize(true);
            rcAdapterTypeTwo = new RCAdapter_type_two(this, new_comic);
            rc_of_detail_cato.setAdapter(rcAdapterTypeTwo);
            rcAdapterTypeTwo.notifyDataSetChanged();
            rcAdapterTypeTwo.setOnItemClickListener(new RCAdapter_type_two.OnItemClickListener() {
                @Override
                public void onItemClick(author_comic_model position) {
                    Intent intent = new Intent(SearchComic.this, SeenComic.class);
                    intent.putExtra("ID_COMIC", position.getID_comic());
                    startActivity(intent);
                }
            });
            if (aLodingDialog != null && aLodingDialog.isShowing()) {
                aLodingDialog.dismiss();
            }
        }
    }

    /*------------------------ phần xử lý recyclerview firebase comic---------------------------*/
    @Override
    public void onItemClick(category_model model) {
        getID_comic_with_category(model.getID());
    }
}