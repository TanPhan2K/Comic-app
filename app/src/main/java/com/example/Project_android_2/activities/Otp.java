package com.example.Project_android_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Project_android_2.R;
import com.example.Project_android_2.utils.DialogHelper;


public class Otp extends AppCompatActivity {
    private EditText otpEt1, otpEt2, otpEt3, otpEt4, otpEt5, otpEt6, edpasss;
    private TextView resendBtn;
    private boolean resendEnabled = false;
    private int resendTime = 60;
    private int selectedETPosition = 0;
    private AppCompatImageView appCompatImageView_back;
    private TextView Email_otp, text_title;
    private String code_otp_string, Email_otp_, code_cp, email_cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        setupUI();

        Intent intent = getIntent();
        Email_otp_ = intent.getStringExtra("Email_otp");
        int code_otp = intent.getIntExtra("Code_otp", 0);

        email_cp = intent.getStringExtra("Email_otp_cp");
        int code_cp_int = intent.getIntExtra("Code_otp_cp", 0);

        if (Email_otp_ != null) {
            text_title.setText("Quên mật khẩu");
            Email_otp.setText(Email_otp_);
        }
        if (email_cp != null) {
            text_title.setText("Đổi mật khẩu");
            Email_otp.setText(email_cp);
        }
        code_otp_string = String.valueOf(code_otp);
        code_cp = String.valueOf(code_cp_int);

        otpEt1.addTextChangedListener(textWatcher);
        otpEt2.addTextChangedListener(textWatcher);
        otpEt3.addTextChangedListener(textWatcher);
        otpEt4.addTextChangedListener(textWatcher);
        otpEt5.addTextChangedListener(textWatcher);
        otpEt6.addTextChangedListener(textWatcher);
        showKeyboard(otpEt1);
        starCountDownTimer();
        handleclickbutton();
    }

    private void setupUI() {
        otpEt1 = findViewById(R.id.otpEt1);
        otpEt2 = findViewById(R.id.otpEt2);
        otpEt3 = findViewById(R.id.otpEt3);
        otpEt4 = findViewById(R.id.otpEt4);
        otpEt5 = findViewById(R.id.otpEt5);
        otpEt6 = findViewById(R.id.otpEt6);
        resendBtn = findViewById(R.id.rensendcode);
        Email_otp = findViewById(R.id.textv_email_Address);
        text_title = findViewById(R.id.text_title);
        appCompatImageView_back = findViewById(R.id.back);
        edpasss = findViewById(R.id.edpasss);


        otpEt2.setEnabled(false);
        otpEt3.setEnabled(false);
        otpEt4.setEnabled(false);
        otpEt5.setEnabled(false);
        otpEt6.setEnabled(false);
    }

    private void handleclickbutton() {
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnabled) {
                    starCountDownTimer();
                }
            }
        });
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Email_otp_ != null) {
                    DialogHelper.showBottomDialog(Otp.this, ForgotPassword.class);
                }
                if (email_cp != null) {
                    DialogHelper.showBottomDialog(Otp.this, User.class);
                }
            }
        });


    }

    private void showKeyboard(EditText otpET) {
        otpET.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);

    }

    private void starCountDownTimer() {
        resendEnabled = false;
        resendBtn.setTextColor(Color.parseColor("#D8D9E1"));
        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                resendBtn.setText("Gửi lại (" + (l / 1000) + "s)");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                resendBtn.setText("Gửi");
                resendBtn.setTextColor(getResources().getColor(R.color.color_background));
            }
        }.start();
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {

                if (selectedETPosition == 0) {
                    selectedETPosition = 1;
                    otpEt2.setEnabled(true);
                    showKeyboard(otpEt2);
                    otpEt1.setEnabled(false);
                } else if (selectedETPosition == 1) {
                    selectedETPosition = 2;
                    otpEt3.setEnabled(true);
                    showKeyboard(otpEt3);
                    otpEt2.setEnabled(false);
                } else if (selectedETPosition == 2) {
                    selectedETPosition = 3;
                    otpEt4.setEnabled(true);
                    showKeyboard(otpEt4);
                    otpEt3.setEnabled(false);
                } else if (selectedETPosition == 3) {
                    selectedETPosition = 4;
                    otpEt5.setEnabled(true);
                    showKeyboard(otpEt5);
                    otpEt4.setEnabled(false);
                } else if (selectedETPosition == 4) {
                    selectedETPosition = 5;
                    otpEt6.setEnabled(true);
                    showKeyboard(otpEt6);
                    otpEt5.setEnabled(false);
                } else if (selectedETPosition == 5) {
                    final String generateOtp = otpEt1.getText().toString() + otpEt2.getText().toString() + otpEt3.getText().toString() + otpEt4.getText().toString() + otpEt5.getText().toString() + otpEt6.getText().toString();
                    if (generateOtp.length() == 6) {
                        if (generateOtp.equals(code_otp_string)) {
                            Intent intent = new Intent(Otp.this, NewPass.class);
                            intent.putExtra("email_newPass", Email_otp_);
                            startActivity(intent);
                            finish();
                        } else if (generateOtp.equals(code_cp)) {
                            Intent intent = new Intent(Otp.this, NewPass.class);
                            intent.putExtra("email_cp", email_cp);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Otp.this, "Mã OTP không chính xác. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }

                        otpEt1.setEnabled(true);
                        otpEt6.setEnabled(false);
                        showKeyboard(otpEt1);
                        selectedETPosition = 0;
                        otpEt1.setText("");
                        otpEt2.setText("");
                        otpEt3.setText("");
                        otpEt4.setText("");
                        otpEt5.setText("");
                        otpEt6.setText("");
                    }

                }
            }
        }
    };


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            otpEt1.setText("");
            otpEt2.setText("");
            otpEt3.setText("");
            otpEt4.setText("");
            otpEt5.setText("");
            otpEt6.setText("");
            otpEt1.setEnabled(true);
            otpEt6.setEnabled(false);
            showKeyboard(otpEt1);
            selectedETPosition = 0;
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}