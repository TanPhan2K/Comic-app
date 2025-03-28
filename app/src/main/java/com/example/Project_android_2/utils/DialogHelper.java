package com.example.Project_android_2.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.Project_android_2.R;

public class DialogHelper {
    // Trong DialogHelper
    public static void showBottomDialog(Activity activity, Class<?> targetClass) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bottomsheetlayout);

        Button btncan = dialog.findViewById(R.id.btncan);
        Button btnback = dialog.findViewById(R.id.btnback);
        btncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Chuyển đến activity targetClass và xóa tất cả các activity khác trên đỉnh stack
                Intent intent = new Intent(activity, targetClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}



