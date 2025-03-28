package com.example.Project_android_2.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.utils.NetworkUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewComic extends AppCompatActivity {
    private AppCompatImageView appCompatImageView_back;
    private Bundle bundle;
    private TextView txt_Chapter;
    private DatabaseReference databaseReference;
    private TextView imageViewChapter;
    private ImageView settingIcon;
    private FrameLayout progressBar;
    private ScrollView scrollView;
    private Handler handler;
    private Runnable rightRunnable, leftRunnable;
    private SeekBar seekBarTextSize;
    private LinearLayout stub_no_internet;
    private RelativeLayout rlLayout;
    private Button try_again_button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcomic);
        appCompatImageView_back = findViewById(R.id.back);
        imageViewChapter = findViewById(R.id.txtViewchapter);
        txt_Chapter = findViewById(R.id.textView);
        settingIcon = findViewById(R.id.settingIcon);
        stub_no_internet = findViewById(R.id.stub_no_internet);
        rlLayout = findViewById(R.id.rl_1);
        progressBar = findViewById(R.id.progressBar);
        bundle = getIntent().getExtras();

        // Load saved text size
        SharedPreferences sharedPreferences = getSharedPreferences("TEXTSIZE", MODE_PRIVATE);
        float savedTextSize;
        try {
            savedTextSize = sharedPreferences.getFloat("VALUE_TEXT", 14f); // Use getFloat initially
        } catch (ClassCastException e) {
            // If there is a ClassCastException, the value was stored as a String
            String savedTextSizeString = sharedPreferences.getString("VALUE_TEXT", "14");
            savedTextSize = Float.parseFloat(savedTextSizeString);
        }
        try_again_button = findViewById(R.id.try_again_button);
        try_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewComic.this,Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        // Set the text size of the TextView
        imageViewChapter.setTextSize(savedTextSize);

        String idcomic = bundle.getString("ID_COMIC");
        String textchapter = bundle.getString("TITLE");
        Long chapterIndex = Long.parseLong(bundle.getString("CHAPTER_INDEX"));
        txt_Chapter.setText(textchapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("chapter");
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
        scrollView = findViewById(R.id.scrollView);

        // Lắng nghe sự kiện cuộn ScrollView
        handler = new Handler();

        // Lắng nghe sự kiện cuộn ScrollView
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isScrollViewAtTop()) {
                    if (leftRunnable != null) {
                        handler.removeCallbacks(leftRunnable);
                    }
                    leftRunnable = new Runnable() {
                        @Override
                        public void run() {
                            Left(idcomic, chapterIndex);
                        }
                    };
                    handler.postDelayed(leftRunnable, 1500);
                } else {
                    if (leftRunnable != null) {
                        handler.removeCallbacks(leftRunnable);
                        leftRunnable = null;
                    }
                }
                if (isScrollViewAtBottom()) {
                    if (rightRunnable != null) {
                        handler.removeCallbacks(rightRunnable);
                    }
                    rightRunnable = new Runnable() {
                        @Override
                        public void run() {
                            Right(idcomic, chapterIndex);
                        }
                    };
                    handler.postDelayed(rightRunnable, 1500); // Delay 2 seconds
                } else {
                    if (rightRunnable != null) {
                        handler.removeCallbacks(rightRunnable);
                        rightRunnable = null;
                    }
                }
            }
        });

        ViewChapter(idcomic, chapterIndex);
    }

    public void Right(String idcomic, Long Index) {
        Query query = databaseReference.orderByChild("ID_COMIC").equalTo(idcomic);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean foundNextChapter = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Long index = dataSnapshot.child("CHAPTER_INDEX").getValue(Long.class);
                        if (index != null && index == (Index + 1)) {
                            // Lấy giá trị của CONTENT và TITLE
                            String content = dataSnapshot.child("CONTENT").getValue(String.class);
                            String title = dataSnapshot.child("TITLE").getValue(String.class);
                            bundle.putString("CONTENT", content);
                            bundle.putString("TITLE", title);
                            bundle.putString("CHAPTER_INDEX", index + "");
                            foundNextChapter = true;
                            break;
                        }
                    }
                    if (foundNextChapter) {
                        Intent intent1 = new Intent(ViewComic.this, ViewComic.class);
                        intent1.putExtras(bundle);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                    } else {
                        Toast.makeText(ViewComic.this, "Không có chương tiếp theo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ViewSee", "Database error: " + error.getMessage());
            }
        });
    }


    public void Left(String idcomic, Long Index) {

        Intent intent1 = new Intent(ViewComic.this, ViewComic.class);
        Query query = databaseReference.orderByChild("ID_COMIC").equalTo(idcomic);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundIndex = false;
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Long index = dataSnapshot.child("CHAPTER_INDEX").getValue(Long.class);
                        if (index != null && index == (Index - 1)) {
                            // Lấy giá trị của CONTENT và TITLE
                            String content = dataSnapshot.child("CONTENT").getValue(String.class);
                            String title = dataSnapshot.child("TITLE").getValue(String.class);
                            bundle.putString("CONTENT", content);
                            bundle.putString("TITLE", title);
                            bundle.putString("CHAPTER_INDEX", index + "");
                            foundIndex = true;
                            break;
                        }
                    }
                    if (foundIndex) { // Kiểm tra nếu đã tìm thấy index
                        intent1.putExtras(bundle);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                    } else {
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_edit_text_size);
        seekBarTextSize = dialog.findViewById(R.id.seekBarTextSize);

        seekBarTextSize.setMax(16);

        // Load saved text size
        SharedPreferences sharedPreferences = getSharedPreferences("TEXTSIZE", MODE_PRIVATE);
        float savedTextSize;
        try {
            savedTextSize = sharedPreferences.getFloat("VALUE_TEXT", 14f); // Try to get as Float
        } catch (ClassCastException e) {
            // If there is a ClassCastException, the value was stored as a String
            String savedTextSizeString = sharedPreferences.getString("VALUE_TEXT", "14");
            savedTextSize = Float.parseFloat(savedTextSizeString);
        }

        // Set the progress of the SeekBar
        seekBarTextSize.setProgress((int) (savedTextSize - 14f));
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float minSize = 14f;
                float textSize = minSize + progress;

                // Update the TextView text size
                imageViewChapter.setTextSize(textSize);

                // Save the text size
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("VALUE_TEXT", textSize); // Save as Float
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something when the user starts to touch the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do something when the user stops touching the SeekBar
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    private boolean isScrollViewAtBottom() {
        int scrollY = scrollView.getScrollY();
        int scrollViewHeight = scrollView.getHeight();
        int scrollContentHeight = scrollView.getChildAt(0).getHeight(); // Chiều cao nội dung của ScrollView
        return (scrollY + scrollViewHeight) >= (scrollContentHeight - 10);
    }


    private boolean isScrollViewAtTop() {
        int scrollY = scrollView.getScrollY();
        return scrollY <= 0; // Ensure the check includes zero or less
    }

    public void ViewChapter(String idcomic, Long chap) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra kết nối mạng sau 2 giây
                if (!NetworkUtils.isNetworkAvailable(ViewComic.this)) {
                    rlLayout.setVisibility(View.GONE);
                    stub_no_internet.setVisibility(View.VISIBLE);
                    RelativeLayout headerTitle = findViewById(R.id.header_title);
                    headerTitle.setBackgroundResource(0);
                    Toast.makeText(ViewComic.this, "Không có kết nối Internet. Vui lòng kiểm tra cài đặt mạng của bạn.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    rlLayout.setVisibility(View.VISIBLE);
                    stub_no_internet.setVisibility(View.GONE);
                }

            }
        }, 2000);
        databaseReference = FirebaseDatabase.getInstance().getReference("chapter");
        Query query = databaseReference.orderByChild("ID_COMIC").equalTo(idcomic);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Long index = dataSnapshot.child("CHAPTER_INDEX").getValue(Long.class);
                        if (index != null && index == chap) {
                            String content = dataSnapshot.child("CONTENT").getValue(String.class);
                            Integer html = dataSnapshot.child("has_html").getValue(Integer.class);
                            if (html == 1) {

                                Spanned spannedText = Html.fromHtml(content);
                                imageViewChapter.setText(spannedText);
                            } else imageViewChapter.setText(content);
                        }
                    }

                }
                progressBar.setVisibility(View.GONE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, 50);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}