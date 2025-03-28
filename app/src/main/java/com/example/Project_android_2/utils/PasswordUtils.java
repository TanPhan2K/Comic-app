package com.example.Project_android_2.utils;

import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Project_android_2.R;

public class PasswordUtils {

    public static void togglePasswordVisibility(EditText editText, TextView eyeTextView, boolean isPasswordVisible) {
        if (isPasswordVisible) {
            // Nếu đang hiển thị mật khẩu, chuyển thành ẩn mật khẩu
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_slash_regular, 0);
        } else {
            // Nếu đang ẩn mật khẩu, chuyển thành hiển thị mật khẩu
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_regular, 0);
        }
        // Đặt con trỏ về cuối văn bản
        editText.setSelection(editText.getText().length());
    }


}
