package com.example.Project_android_2.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.models.user;
import com.example.Project_android_2.utils.DialogHelper;
import com.example.Project_android_2.utils.PasswordHelper;
import com.example.Project_android_2.utils.PasswordUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private TextView textv_fpassword, textv_SignUpnow, eyeTextView, clearTextView_email, clearTextView_pass;
    private EditText EdTemail_username, EdTpassword;
    private AppCompatImageView appCompatImageView_back;
    private Button buttonsignin;
    private FrameLayout progressBar;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        EdTemail_username = findViewById(R.id.EdTemail_username);
        EdTpassword = findViewById(R.id.EdTpassword);
        textv_fpassword = findViewById(R.id.textv_fpassword);
        textv_SignUpnow = findViewById(R.id.textv_SignUpnow);
        appCompatImageView_back = findViewById(R.id.back);
        buttonsignin = findViewById(R.id.buttonsignin);
        progressBar = findViewById(R.id.progressBar);
        eyeTextView = findViewById(R.id.eyeTextView);
        clearTextView_email = findViewById(R.id.clearTextView_email);
        clearTextView_pass = findViewById(R.id.clearTextView_pass);
        Intent intent = getIntent();
        String Email_otp_ = intent.getStringExtra("email");

        if (Email_otp_ != null) {
            EdTemail_username.setText(Email_otp_);
            EdTpassword.requestFocus();
        }


        loading();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Kiểm tra nếu cả email/username và password đều không rỗng
                boolean isEmailOrUsernameNotEmpty = !EdTemail_username.getText().toString().isEmpty();
                boolean isPasswordNotEmpty = !EdTpassword.getText().toString().isEmpty();
                String emailOrUsername = EdTemail_username.getText().toString().trim();
                String passwordText = EdTpassword.getText().toString().trim();
                // Kiểm tra xem chuỗi nhập vào có đúng định dạng email không
                boolean isEmailFormatValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches();

                // Nếu cả email/username và password không rỗng và đúng định dạng email
                if (isEmailOrUsernameNotEmpty && isPasswordNotEmpty && isEmailFormatValid) {
                    buttonsignin.setEnabled(true);
                    buttonsignin.setAlpha(1.0f);
                } else {
                    buttonsignin.setEnabled(false);
                    buttonsignin.setAlpha(0.5f);
                }

                if (emailOrUsername.isEmpty()) {
                    clearTextView_email.setVisibility(View.GONE);
                } else {
                    clearTextView_email.setVisibility(View.VISIBLE);
                }

                if (passwordText.isEmpty()) {
                    clearTextView_pass.setVisibility(View.GONE);
                } else {
                    clearTextView_pass.setVisibility(View.VISIBLE);
                }
            }
        };

        EdTemail_username.addTextChangedListener(textWatcher);
        EdTpassword.addTextChangedListener(textWatcher);

        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSign_in();
            }
        });
        textv_fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        textv_SignUpnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        eyeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordUtils.togglePasswordVisibility(EdTpassword, eyeTextView, isPasswordVisible);
                isPasswordVisible = !isPasswordVisible;
            }
        });
        clearTextView_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdTemail_username.setText("");
            }
        });
        clearTextView_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdTpassword.setText("");
            }
        });
    }

    private void setSign_in() {
        // Lấy thông tin từ hai trường EditText
        progressBar.setVisibility(View.VISIBLE);
        String emailOrUsername = EdTemail_username.getText().toString().trim();
        String password = EdTpassword.getText().toString().trim();
        String hashedInputPassword = PasswordHelper.hashPassword(password);
        // Truy vấn Realtime Database để lấy thông tin người dùng có email hoặc username tương ứng
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user");
        usersRef.orderByChild("email").equalTo(emailOrUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    user user = userSnapshot.getValue(user.class);
                    // Kiểm tra xem email và mật khẩu đã mã hóa từ người dùng có trùng khớp với dữ liệu trong Firebase không
                    if (user != null && user.getPassword().equals(hashedInputPassword)) {
                        // Nếu trùng khớp, chuyển dữ liệu qua màn hình Home
                        String name = user.getUsername();
                        String email = user.getEmail();
                        String uid = user.getUid();
                        String avatar = user.getAvatar().toString();
                        saveUserInfoToSharedPreferences(email, name, avatar,uid);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(SignIn.this, Home.class);
                        startActivity(intent);
                        finish();
                        return;
                    } else {
                        // Nếu không tìm thấy email hoặc mật khẩu tương ứng, hiển thị thông báo lỗi
                        Toast.makeText(SignIn.this, "Email/tên người dùng hoặc mật khẩu không hợp lệ.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi truy vấn bị hủy
            }
        });
    }

    private void saveUserInfoToSharedPreferences(String userEmail, String userName, String photoUrl,String userUid) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userUid", userUid);
        editor.putString("userEmail", userEmail);
        editor.putString("userName", userName);
        editor.putString("photoUrl", photoUrl);
        editor.apply();
    }

    private void loading() {
        progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; // Chặn sự kiện touch
            }
        });
    }
}