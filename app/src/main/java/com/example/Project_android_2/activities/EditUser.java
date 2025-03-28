package com.example.Project_android_2.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;

import com.bumptech.glide.Glide;
import com.example.Project_android_2.R;
import com.example.Project_android_2.utils.DialogHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditUser extends AppCompatActivity {
    private AppCompatImageView appCompatImageView_back;
    private EditText edtext_edProfile;
    private Uri uri;
    private RoundedImageView imageProfile;
    private TextView textAdImage, clearTextView_email, usname_usname, usname_empty;
    private Button button_save;
    private FrameLayout progressBar;

    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userUid = sharedPreferences.getString("userUid", "");
        String userPhoto = sharedPreferences.getString("photoUrl", "");
        String userName = sharedPreferences.getString("userName", "");
        String userEmail = sharedPreferences.getString("userEmail", "");

        appCompatImageView_back = findViewById(R.id.back);
        edtext_edProfile = findViewById(R.id.edtext_edProfile);
        imageProfile = findViewById(R.id.imageProfile);
        textAdImage = findViewById(R.id.textAdImage);
        button_save = findViewById(R.id.button_save);
        usname_empty = findViewById(R.id.usname_empty);
        usname_usname = findViewById(R.id.usname_usname);
        clearTextView_email = findViewById(R.id.clearTextView_email);
        progressBar = findViewById(R.id.progressBar);
        loading();

        Glide.with(this).load(userPhoto).into(imageProfile);
        textAdImage.setText("");
        edtext_edProfile.setText(userName);

        edtext_edProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtext_edProfile.setFocusableInTouchMode(true);
            }
        });
        edtext_edProfile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Username = edtext_edProfile.getText().toString().trim();
                if (edtext_edProfile.getText().toString().length() <= 20 && !Username.isEmpty()) {
                    button_save.setEnabled(true);
                    button_save.setAlpha(1.0f);
                } else {
                    button_save.setEnabled(false);
                    button_save.setAlpha(0.5f);
                }

                if (Username.isEmpty()) {
                    clearTextView_email.setVisibility(View.GONE);
                    setDrawableLeft(EditUser.this, usname_empty, R.drawable.background_chest);
                    setDrawableLeft(EditUser.this, usname_usname, R.drawable.background_chest);
                } else {
                    clearTextView_email.setVisibility(View.VISIBLE);
                    setDrawableLeft(EditUser.this, usname_empty, R.drawable.background_chest_614385);
                    if (Username.equals(userName)) {
                        // Nếu trùng khớp, sử dụng empty state không có hình ảnh
                        setDrawableLeft(EditUser.this, usname_empty, R.drawable.background_chest);
                        setDrawableLeft(EditUser.this, usname_usname, R.drawable.background_chest);

                        button_save.setEnabled(false);
                        button_save.setAlpha(0.5f);
                    } else {
                        // Nếu không trùng khớp, sử dụng empty state có hình ảnh
                        setDrawableLeft(EditUser.this, usname_empty, R.drawable.background_chest_614385);
                        setDrawableLeft(EditUser.this, usname_usname, R.drawable.background_chest_614385);
                        button_save.setEnabled(true);
                        button_save.setAlpha(1.0f);
                        if (Username.length() >= 1 && Username.length() <= 20) {
                            setDrawableLeft(EditUser.this, usname_usname, R.drawable.background_chest_614385);
                        } else {
                            setDrawableLeft(EditUser.this, usname_usname, R.drawable.background_chest);
                        }
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = edtext_edProfile.getText().toString().trim();
                if (Username.equals(userName)) {
                    onBackPressed();
                } else {
                    DialogHelper.showBottomDialog(EditUser.this, User.class);
                }
            }
        });
        clearTextView_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtext_edProfile.setText("");
            }
        });

        ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            Intent dataI = o.getData();
                            uri = dataI.getData();
                            imageProfile.setImageURI(uri);
                            button_save.setEnabled(true);
                            button_save.setAlpha(1.0f);
                        } else {
                            button_save.setEnabled(false);
                            button_save.setAlpha(0.5f);
                            Toast.makeText(EditUser.this, "Không có hình ảnh nào được chọn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_photo = new Intent(Intent.ACTION_PICK);
                intent_photo.setType("image/*");
                intentActivityResultLauncher.launch(intent_photo);
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                String Username = edtext_edProfile.getText().toString().trim();
                saveImageToStorage(uri, userEmail, Username);

            }
        });
    }
    private void setUpdateUser(String userEmail, String newUsername, String imageUrl) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user");

        // Tìm kiếm người dùng với email tương ứng
        userRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lặp qua tất cả các nút con tương ứng với email
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Cập nhật thông tin người dùng
                        userSnapshot.getRef().child("username").setValue(newUsername);
                        userSnapshot.getRef().child("avatar").setValue(imageUrl);
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userName", newUsername);
                        editor.putString("photoUrl", imageUrl);
                        editor.apply();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(EditUser.this, User.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(EditUser.this, "Không tìm thấy người dùng với email này!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("edittext", "Lỗi khi cập nhật thông tin người dùng: " + databaseError.getMessage());
                Toast.makeText(EditUser.this, "Đã xảy ra lỗi khi cập nhật thông tin người dùng!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveImageToStorage(Uri imageUri, String userEmail, String newUsername) {
        // Kiểm tra xem ảnh có được chọn không
        if (imageUri != null) {
            // Của Trường

            // Lấy tên file
//            String fileName = new File(uri.getPath()).getName();

            // Tạo tham chiếu tên ảnh trên store:

//            StorageReference imageRef = FirebaseStorage.getInstance().getReference("USER").child(fileName);
//
//            imageRef.putFile(imageUri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        // Lấy đường dẫn của ảnh sau khi upload thành công
//                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            String imageUrl = uri.toString();
//                            if (!TextUtils.isEmpty(userUid)) {
//                                setUpdateUser(userEmail, newUsername, imageUrl);
//                            } else {
//                                setupdteUse(newUsername, imageUrl);
//                            }
//                        });
//                    })
//                    .addOnFailureListener(exception -> {
//                        Toast.makeText(EditUser.this, "Lỗi khi tải ảnh lên: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    });




            // Của tôi
            String fileName = new File(uri.getPath()).getName();
            String UPLOAD_URL = "https://fpytgnoybnnnhflnybhp.supabase.co/storage/v1/object/media-storage/USER/" + fileName;
            String SERVICE_ROLE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZweXRnbm95Ym5ubmhmbG55YmhwIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAwMDgxOCwiZXhwIjoyMDU4NTc2ODE4fQ.ZexuNkVRZnjg8ECNRxtI8smfJuSICKa7xTXCRRTC6zw";
            new Thread(() -> {
                try {
                    byte[] fileData = readBytesFromUri(imageUri);

                    URL url = new URL(UPLOAD_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "Bearer " + SERVICE_ROLE_KEY);
                    connection.setRequestProperty("apikey", SERVICE_ROLE_KEY);
                    connection.setRequestProperty("Content-Type", "image/jpeg");
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(fileData);
                    connection.getOutputStream().close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        String imageUrl = "https://fpytgnoybnnnhflnybhp.supabase.co/storage/v1/object/public/media-storage/USER/" + fileName;
                        if (!TextUtils.isEmpty(userUid)) {
                            setUpdateUser(userEmail, newUsername, imageUrl);
                        } else {
                            setupdteUse(newUsername, imageUrl);
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(EditUser.this, "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(EditUser.this, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();

        } else {
            if (!TextUtils.isEmpty(userUid)) {
                setUpdateUsername(userEmail, newUsername);
            } else {
                setupdteUsername(newUsername);
            }
        }
    }

    private byte[] readBytesFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        byte[] fileData = new byte[inputStream.available()];
        inputStream.read(fileData);
        inputStream.close();
        return fileData;
    }

    private void setUpdateUsername(String userEmail, String newUsername) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user");

        // Tìm kiếm người dùng với email tương ứng
        userRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lặp qua tất cả các nút con tương ứng với email
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Chỉ cập nhật thông tin tên người dùng
                        userSnapshot.getRef().child("username").setValue(newUsername);
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userName", newUsername);
                        editor.apply();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(EditUser.this, User.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(EditUser.this, "Không tìm thấy người dùng với email này!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("edittext", "Lỗi khi cập nhật thông tin người dùng: " + databaseError.getMessage());
                Toast.makeText(EditUser.this, "Đã xảy ra lỗi khi cập nhật thông tin người dùng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDrawableLeft(Context context, TextView textView, int drawableId) {
        // Lấy drawable từ tài nguyên
        Drawable drawable = context.getResources().getDrawable(drawableId);

        // Đặt drawableLeft cho TextView
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    private void setupdteUse(String name, String img) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(img))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userName", name);
                            editor.putString("photoUrl", img);
                            editor.apply();
                            Intent intent = new Intent(EditUser.this, User.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void setupdteUsername(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userName", name);
                            editor.apply();
                            Intent intent = new Intent(EditUser.this, User.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
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