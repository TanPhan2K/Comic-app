package com.example.Project_android_2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Project_android_2.R;
import com.example.Project_android_2.utils.DialogHelper;
import com.example.Project_android_2.utils.PasswordHelper;
import com.example.Project_android_2.utils.PasswordUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewPass extends AppCompatActivity {
    private AppCompatImageView appCompatImageView_back;
    private EditText edpasss, edConfirmpassword;
    private TextView Confirmpassword, password_sign, password_minimum, eyeTextView, eyeTextView_cpasss, clearTextView_pass, clearTextView_Cpass, title_newpass;
    private Button butnsuccess;
    private FrameLayout progressBar;
    private String Email_otp_, email_cp;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);

        setupUI();
        Intent intent = getIntent();
        Email_otp_ = intent.getStringExtra("email_newPass");
        email_cp = intent.getStringExtra("email_cp");
        if (Email_otp_ != null) {
            title_newpass.setText("Quên Mật Khẩu");
            edConfirmpassword.setHint("Vui lòng nhập lại mật khẩu");
        }
        if (email_cp != null) {
            title_newpass.setText("Đổi Mật Khẩu");
            edConfirmpassword.setHint("Vui lòng nhập mật khẩu mới");
            Confirmpassword.setVisibility(View.GONE);
        }
        textchanged();
        handleclickbutton();

    }

    private void handleclickbutton() {
        butnsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (Email_otp_ != null) {
                    setupNewpass();
                }
                if (email_cp != null) {
                    setuppass();

                }
            }
        });
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Email_otp_ != null) {
                    DialogHelper.showBottomDialog(NewPass.this, ForgotPassword.class);
                }
                if (email_cp != null) {
                    DialogHelper.showBottomDialog(NewPass.this, User.class);
                }
            }
        });

        eyeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordUtils.togglePasswordVisibility(edpasss, eyeTextView, isPasswordVisible);
                isPasswordVisible = !isPasswordVisible;
            }
        });
        eyeTextView_cpasss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordUtils.togglePasswordVisibility(edConfirmpassword, eyeTextView_cpasss, isPasswordVisible);
                isPasswordVisible = !isPasswordVisible;
            }
        });
        clearTextView_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edpasss.setText("");
            }
        });
        clearTextView_Cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edConfirmpassword.setText("");
            }
        });
    }

    private void setuppass() {
        String newPassword = edpasss.getText().toString();
        String hashedPassword = PasswordHelper.hashPassword(newPassword);
        String newcfPassword = edConfirmpassword.getText().toString();
        String hashedcfPassword = PasswordHelper.hashPassword(newcfPassword);
        updatePasswordIfMatch(email_cp, hashedPassword, hashedcfPassword);
    }


    private void setupNewpass() {
        String newPassword = edpasss.getText().toString();
        String hashedPassword = PasswordHelper.hashPassword(newPassword);

        // Kiểm tra trên Realtime Database xem email nào trùng khớp với Email_otp_
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user");
        usersRef.orderByChild("email").equalTo(Email_otp_).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Lấy thông tin người dùng có email trùng khớp
                    String userId = userSnapshot.getKey();

                    // Cập nhật mật khẩu mới cho người dùng
                    updatePasswordInFirebase(userId, hashedPassword);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(NewPass.this, "Không thể truy vấn dữ liệu từ Realtime Database!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void textchanged() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = edpasss.getText().toString();
                String confirmpassword = edConfirmpassword.getText().toString();
                String normalizedPassword = password.replaceAll("[^\\x00-\\x7F]", "");
                String normalizedconfiPassword = confirmpassword.replaceAll("[^\\x00-\\x7F]", "");

                if (!password.isEmpty() && !confirmpassword.isEmpty()) {
                    if (Email_otp_ != null) {
                        if (!password.equals(normalizedPassword)) {
                            setDrawableLeft(NewPass.this, password_sign, R.drawable.background_chest);
                        } else if (password.equals(normalizedPassword)) {
                            setDrawableLeft(NewPass.this, password_sign, R.drawable.background_chest_614385);
                        }
                        if (edpasss.getText().toString().length() > 8) {
                            setDrawableLeft(NewPass.this, password_minimum, R.drawable.background_chest_614385);
                        } else {
                            setDrawableLeft(NewPass.this, password_minimum, R.drawable.background_chest);
                        }
                        if (edConfirmpassword.getText().toString().equals(edpasss.getText().toString())) {
                            // Nếu trùng, đặt drawableLeft cho password_minimum
                            setDrawableLeft(NewPass.this, Confirmpassword, R.drawable.background_chest_614385);
                        } else {
                            setDrawableLeft(NewPass.this, Confirmpassword, R.drawable.background_chest);
                        }
                        if (password.equals(normalizedPassword) && edpasss.getText().toString().length() > 8 && edConfirmpassword.getText().toString().equals(edpasss.getText().toString())) {
                            butnsuccess.setEnabled(true);
                            butnsuccess.setAlpha(1.0f);
                        } else {
                            butnsuccess.setEnabled(false);
                            butnsuccess.setAlpha(0.5f);
                        }
                    }
                    if (email_cp != null) {
                        if (!confirmpassword.equals(normalizedconfiPassword)) {
                            setDrawableLeft(NewPass.this, password_sign, R.drawable.background_chest);
                        } else if (confirmpassword.equals(normalizedconfiPassword)) {
                            setDrawableLeft(NewPass.this, password_sign, R.drawable.background_chest_614385);
                        }
                        if (edConfirmpassword.getText().toString().length() > 8) {
                            setDrawableLeft(NewPass.this, password_minimum, R.drawable.background_chest_614385);
                        } else {
                            setDrawableLeft(NewPass.this, password_minimum, R.drawable.background_chest);
                        }
                        if (confirmpassword.equals(normalizedconfiPassword) && edConfirmpassword.getText().toString().length() > 8) {

                            butnsuccess.setEnabled(true);
                            butnsuccess.setAlpha(1.0f);
                        } else {
                            butnsuccess.setEnabled(false);
                            butnsuccess.setAlpha(0.5f);
                        }
                    }

                } else {
                    setDrawableLeft(NewPass.this, Confirmpassword, R.drawable.background_chest);
                    setDrawableLeft(NewPass.this, password_minimum, R.drawable.background_chest);
                    setDrawableLeft(NewPass.this, password_sign, R.drawable.background_chest);
                    butnsuccess.setEnabled(false);
                    butnsuccess.setAlpha(0.5f);
                }
                if (password.isEmpty()) {
                    clearTextView_pass.setVisibility(View.GONE);
                } else {
                    clearTextView_pass.setVisibility(View.VISIBLE);
                }

                if (confirmpassword.isEmpty()) {
                    clearTextView_Cpass.setVisibility(View.GONE);
                } else {
                    clearTextView_Cpass.setVisibility(View.VISIBLE);
                }
            }
        };

        edpasss.addTextChangedListener(textWatcher);
        edConfirmpassword.addTextChangedListener(textWatcher);
    }

    // Phương thức cập nhật mật khẩu mới vào Firebase
    private void updatePasswordInFirebase(String userId, String newPassword) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(userId);
        userRef.child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NewPass.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(NewPass.this, SignIn.class);
                    intent.putExtra("email", Email_otp_);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(NewPass.this, "Cập nhật mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updatePasswordIfMatch(String email, String hashedPassword, String newPassword) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user");
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Lấy thông tin người dùng có email trùng khớp
                    String currentPassword = userSnapshot.child("password").getValue(String.class);

                    // Kiểm tra nếu password hiện tại trên Firebase giống với hashedPassword
                    if (currentPassword != null && currentPassword.equals(hashedPassword)) {
                        // Cập nhật password mới
                        userSnapshot.getRef().child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(NewPass.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(NewPass.this, User.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(NewPass.this, "Cập nhật mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {
                        // Hiển thị thông báo hoặc xử lý khi password hiện tại không khớp
                        Toast.makeText(NewPass.this, "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(NewPass.this, "Không thể truy vấn dữ liệu từ Realtime Database!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void setupUI() {
        edpasss = findViewById(R.id.edpasss);
        edConfirmpassword = findViewById(R.id.edConfirmpassword);
        password_sign = findViewById(R.id.password_sign);
        password_minimum = findViewById(R.id.password_minimum);
        Confirmpassword = findViewById(R.id.Confirmpassword);
        butnsuccess = findViewById(R.id.butnsuccess);
        title_newpass = findViewById(R.id.title_newpass);
        progressBar = findViewById(R.id.progressBar);
        appCompatImageView_back = findViewById(R.id.back);
        eyeTextView = findViewById(R.id.eyeTextView);
        eyeTextView_cpasss = findViewById(R.id.eyeTextView_cpasss);
        clearTextView_pass = findViewById(R.id.clearTextView_pass);
        clearTextView_Cpass = findViewById(R.id.clearTextView_Cpass);
        loading();
    }


    public void setDrawableLeft(Context context, TextView textView, int drawableId) {
        // Lấy drawable từ tài nguyên
        Drawable drawable = context.getResources().getDrawable(drawableId);

        // Đặt drawableLeft cho TextView
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
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