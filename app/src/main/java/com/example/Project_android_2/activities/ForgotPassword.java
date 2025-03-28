package com.example.Project_android_2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.utils.DialogHelper;
import com.example.Project_android_2.utils.SendEmailTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ForgotPassword extends AppCompatActivity {
    private AppCompatImageView appCompatImageView_back;
    private EditText editTextEmail;
    private FrameLayout progressBar;
    private Button buttonSignIn;
    private int code;
    private TextView clearTextView_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        setupUI();



        chestExdit();
        handleclickbutton();
    }

    private void setupUI() {
        appCompatImageView_back = findViewById(R.id.back);
        editTextEmail = findViewById(R.id.butonemail);
        buttonSignIn = findViewById(R.id.buttonsignin);
        progressBar = findViewById(R.id.progressBar);
        clearTextView_email = findViewById(R.id.clearTextView_email);
        loading();
    }

    private void handleclickbutton() {
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onBackPressed();
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setchestEmailFirebase();
            }
        });
        clearTextView_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEmail.setText("");
            }
        });
    }

    private void setchestEmailFirebase() {
        progressBar.setVisibility(View.VISIBLE);

        String enteredEmail = editTextEmail.getText().toString().trim();
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
                    Intent intent = new Intent(ForgotPassword.this, Otp.class);
                    intent.putExtra("Email_otp", enteredEmail);
                    intent.putExtra("Code_otp", code);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ForgotPassword.this, "Email không khớp với bất kỳ tài khoản nào", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý nếu có lỗi xảy ra
                Toast.makeText(ForgotPassword.this, "Không có Internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void chestExdit() {
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    buttonSignIn.setEnabled(true);
                    buttonSignIn.setAlpha(1.0f);
                } else {
                    buttonSignIn.setEnabled(false);
                    buttonSignIn.setAlpha(0.5f);
                }
                if (editTextEmail.getText().toString().trim().isEmpty()){
                    clearTextView_email.setVisibility(View.GONE);
                } else {
                    clearTextView_email.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void loading(){
        progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; // Chặn sự kiện touch
            }
        });
    }
}
