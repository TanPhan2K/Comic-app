package com.example.Project_android_2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.Project_android_2.R;
import com.example.Project_android_2.utils.NetworkUtils;
import com.example.Project_android_2.utils.SendEmailTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Random;

public class User extends AppCompatActivity {
    private RelativeLayout editProfileLayout, editTextSizeLayout, editLanguageLayout;
    private AppCompatImageView appCompatImageView_back;
    private RoundedImageView img_user;
    private TextView username;
    private LinearLayout lnlo_changepass;
    private int code;
    private FrameLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setupUI();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userUid = sharedPreferences.getString("userUid", "");
        String userPhotoUrl = sharedPreferences.getString("photoUrl", "");
        String userName = sharedPreferences.getString("userName", "");
        String userEmail = sharedPreferences.getString("userEmail", "");

        if (!TextUtils.isEmpty(userUid)) {
            lnlo_changepass.setVisibility(View.VISIBLE);
        } else {
            lnlo_changepass.setVisibility(View.GONE);
        }
        Glide.with(this).load(userPhotoUrl).into(img_user);
        username.setText(userName);
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        editProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isNetworkAvailable(User.this)) {
                    Toast.makeText(User.this, "Không có kết nối Internet. Vui lòng kiểm tra cài đặt mạng của bạn.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(User.this, EditUser.class);
                    startActivity(intent);
                }
            }
        });
        editTextSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setchestEmailFirebase(userEmail);

            }
        });
        editLanguageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User.this, EditLanguage.class);
                startActivity(intent);
            }
        });
    }

    public void setupUI() {
        appCompatImageView_back = findViewById(R.id.back);
        editProfileLayout = findViewById(R.id.editProfileLayout);
        editTextSizeLayout = findViewById(R.id.editTextSizeLayout);
        editLanguageLayout = findViewById(R.id.editLanguageLayout);
        lnlo_changepass = findViewById(R.id.lnlo_changepass);
        progressBar = findViewById(R.id.progressBar);
        img_user = findViewById(R.id.img_user);
        username = findViewById(R.id.username);
    }
    private void setchestEmailFirebase(String enteredEmail) {
        progressBar.setVisibility(View.VISIBLE);

        Random random = new Random();
        code = random.nextInt(900000) + 100000;
        String messageContent = "Bạn đang Đổi mật khẩu";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("user");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean emailFound = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (email.equals(enteredEmail)) {
                        emailFound = true;
                        progressBar.setVisibility(View.GONE);
                        break;
                    }
                }
                if (emailFound) {

                    SendEmailTask sendEmailTask = new SendEmailTask(enteredEmail, code, messageContent);
                    sendEmailTask.execute();
                    Intent intent = new Intent(User.this, Otp.class);
                    intent.putExtra("Email_otp_cp", enteredEmail);
                    intent.putExtra("Code_otp_cp", code);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(User.this, "Email không khớp với bất kỳ tài khoản nào", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý nếu có lỗi xảy ra
                Toast.makeText(User.this, "Không có Internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
