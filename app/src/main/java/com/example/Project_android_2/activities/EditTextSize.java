package com.example.Project_android_2.activities;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.Project_android_2.R;

public class EditTextSize extends AppCompatActivity {
    private TextView textViewChapterContent;
    private SeekBar seekBarTextSize;
    private float defaultTextSize; // Kích thước văn bản mặc định
    private AppCompatImageView appCompatImageView_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_size);
        seekBarTextSize = findViewById(R.id.seekBarTextSize);
        // Lấy kích thước văn bản mặc định từ MyTextStyle
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.style.MyTextStyle, typedValue, true);
        defaultTextSize = typedValue.getDimension(getResources().getDisplayMetrics());

        // Đặt sự kiện thay đổi cho SeekBar
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Định nghĩa giá trị tối thiểu và tối đa cho kích thước văn bản
                float minTextSize = getResources().getDimension(R.dimen.min_text_size); // Định nghĩa trong dimens.xml
                float maxTextSize = getResources().getDimension(R.dimen.max_text_size); // Định nghĩa trong dimens.xml

                // Tính toán kích thước văn bản mới
                float newSize = minTextSize + (maxTextSize - minTextSize) * progress / seekBar.getMax();

                // Đặt kích thước văn bản mới
                textViewChapterContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }
        });
        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}