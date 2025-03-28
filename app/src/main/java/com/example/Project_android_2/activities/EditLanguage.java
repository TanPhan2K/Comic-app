package com.example.Project_android_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.view.View;

import com.example.Project_android_2.R;

public class EditLanguage extends AppCompatActivity {
    private AppCompatImageView appCompatImageView_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_language);
        appCompatImageView_back = findViewById(R.id.back);
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}