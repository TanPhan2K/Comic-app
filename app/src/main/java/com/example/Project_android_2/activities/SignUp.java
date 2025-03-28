package com.example.Project_android_2.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Project_android_2.utils.IdGeneratorHelper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.models.user;
import com.example.Project_android_2.utils.NetworkUtils;
import com.example.Project_android_2.utils.PasswordHelper;
import com.example.Project_android_2.utils.PasswordUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private AppCompatImageView appCompatImageView_back;
    private EditText editText_exemail, editText_exusername, editText_expassword, editText_exConfirmpassword;
    private TextView textAdImage, clearTextView_email, clearTextView_User, clearTextView_pass, clearTextView_cpass,eyeTextView,eyeTextView_cpasss;
    private Button buttonsignup;
    private RoundedImageView roundedImageView_imageProfile;
    private Uri uri;
//    private StorageReference storageReference;
    private StorageTask storageTask;
    private DatabaseReference databaseReference;
    private FrameLayout progressBar;
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        appCompatImageView_back = findViewById(R.id.back);
        editText_exemail = findViewById(R.id.exemail);
        editText_exusername = findViewById(R.id.exusername);
        editText_expassword = findViewById(R.id.expassword);
        editText_exConfirmpassword = findViewById(R.id.exConfirmpassword);
        roundedImageView_imageProfile = findViewById(R.id.imageProfile);
        textAdImage = findViewById(R.id.textAdImage);
        buttonsignup = findViewById(R.id.buttonsignup);
        progressBar = findViewById(R.id.progressBar);
        eyeTextView = findViewById(R.id.eyeTextView);
        eyeTextView_cpasss = findViewById(R.id.eyeTextView_cpasss);
        clearTextView_email = findViewById(R.id.clearTextView_email);
        clearTextView_User = findViewById(R.id.clearTextView_User);
        clearTextView_pass = findViewById(R.id.clearTextView_pass);
        clearTextView_cpass = findViewById(R.id.clearTextView_cpass);
        loading();
        editText_exemail.addTextChangedListener(textWatcher);
        editText_exusername.addTextChangedListener(textWatcher);
        editText_expassword.addTextChangedListener(textWatcher);
        editText_exConfirmpassword.addTextChangedListener(textWatcher);

//        storageReference = FirebaseStorage.getInstance().getReference("USER");
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        appCompatImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                            roundedImageView_imageProfile.setImageURI(uri);
                        } else {
                            Toast.makeText(SignUp.this, "Không có hình ảnh nào được chọn", Toast.LENGTH_SHORT).show();
                            textAdImage.setText("Chọn một hình ảnh");
                        }
                    }
                });

        roundedImageView_imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_photo = new Intent(Intent.ACTION_PICK);
                intent_photo.setType("image/*");
                intentActivityResultLauncher.launch(intent_photo);
                textAdImage.setText("");
            }
        });
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isNetworkAvailable(SignUp.this)) {
                    Toast.makeText(SignUp.this, "Không có kết nối Internet. Vui lòng kiểm tra cài đặt mạng của bạn.", Toast.LENGTH_SHORT).show();
                } else if (storageTask != null && storageTask.isInProgress()) {
                    Toast.makeText(SignUp.this, "Đang tải lên", Toast.LENGTH_SHORT).show();
                } else {
                    if (isValidSignInDetails()) {
                        checkIfEmailExists();

                    }
                }
            }
        });
        eyeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordUtils.togglePasswordVisibility(editText_expassword, eyeTextView, isPasswordVisible);
                isPasswordVisible = !isPasswordVisible;
            }
        });
        eyeTextView_cpasss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordUtils.togglePasswordVisibility(editText_exConfirmpassword, eyeTextView_cpasss, isPasswordVisible);
                isPasswordVisible = !isPasswordVisible;
            }
        });
        clearTextView_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_exemail.setText("");
            }
        });
        clearTextView_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_exusername.setText("");
            }
        });
        clearTextView_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_expassword.setText("");
            }
        });
        clearTextView_cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_exConfirmpassword.setText("");
            }
        });

    }


    private void UploadFile() {
        if (uri != null) {
            String fileName = new File(uri.getPath()).getName();
//            StorageReference fileReference = storageReference.child(fileName);
//            storageTask = fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri downloadUri) {
//                            Toast.makeText(SignUp.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
//
//                            String uploadId = databaseReference.push().getKey();
//                            int id = IdGeneratorHelper.generateAutomaticId();
//                            String avatarUrl = downloadUri.toString();
//                            String email = editText_exemail.getText().toString();
//                            String password = editText_expassword.getText().toString();
//                            String hashPassword = PasswordHelper.hashPassword(password);
//                            String username = editText_exusername.getText().toString();
//
//                            user user = new user(id, avatarUrl, email, hashPassword, username, "email");
//                            databaseReference.child(uploadId).setValue(user);
//                            progressBar.setVisibility(View.GONE);
//                            Intent intent = new Intent(SignUp.this, SignIn.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                }
//            });

            String UPLOAD_URL = "https://fpytgnoybnnnhflnybhp.supabase.co/storage/v1/object/media-storage/USER/" + fileName;
            String SERVICE_ROLE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZweXRnbm95Ym5ubmhmbG55YmhwIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAwMDgxOCwiZXhwIjoyMDU4NTc2ODE4fQ.ZexuNkVRZnjg8ECNRxtI8smfJuSICKa7xTXCRRTC6zw";
            new Thread(() -> {
                try {
                    byte[] fileData = readBytesFromUri(uri);

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
                            runOnUiThread(() -> Toast.makeText(SignUp.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show());
                            String uploadId = databaseReference.push().getKey();
                            int id = IdGeneratorHelper.generateAutomaticId();
                            String avatarUrl = imageUrl;
                            String email = editText_exemail.getText().toString();
                            String password = editText_expassword.getText().toString();
                            String hashPassword = PasswordHelper.hashPassword(password);
                            String username = editText_exusername.getText().toString();

                            user user = new user(id, avatarUrl, email, hashPassword, username, "email");
                            databaseReference.child(uploadId).setValue(user);
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(SignUp.this, SignIn.class);
                            startActivity(intent);
                            finish();
                    } else {
                        runOnUiThread(() -> Toast.makeText(SignUp.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show());
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(SignUp.this, "Có lỗi xảy ra nè: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    progressBar.setVisibility(View.GONE);
                }
            }).start();
        }
    }

    private byte[] readBytesFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        byte[] fileData = new byte[inputStream.available()];
        inputStream.read(fileData);
        inputStream.close();
        return fileData;
    }

    private void checkIfEmailExists() {
        String email = editText_exemail.getText().toString().trim();
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(SignUp.this, "Email này đã được đăng ký. Vui lòng sử dụng một email khác.", Toast.LENGTH_SHORT).show();
                } else {
                    UploadFile();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUp.this, "Lỗi cơ sở dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            updateButtonState();
            String email = editText_exemail.getText().toString().trim();
            String username = editText_exusername.getText().toString().trim();
            String pass = editText_expassword.getText().toString().trim();
            String confirpass = editText_exConfirmpassword.getText().toString().trim();
            if (email.isEmpty()) {
                clearTextView_email.setVisibility(View.GONE);
            } else {
                clearTextView_email.setVisibility(View.VISIBLE);
            }
            if (username.isEmpty()) {
                clearTextView_User.setVisibility(View.GONE);
            } else {
                clearTextView_User.setVisibility(View.VISIBLE);
            }
            if (pass.isEmpty()) {
                clearTextView_pass.setVisibility(View.GONE);
            } else {
                clearTextView_pass.setVisibility(View.VISIBLE);
            }
            if (confirpass.isEmpty()) {
                clearTextView_cpass.setVisibility(View.GONE);
            } else {
                clearTextView_cpass.setVisibility(View.VISIBLE);
            }
        }
    };


    private void updateButtonState() {
        String email = editText_exemail.getText().toString();
        String username = editText_exusername.getText().toString().trim();
        String password = editText_expassword.getText().toString();
        String confirmPassword = editText_exConfirmpassword.getText().toString();
        boolean isButtonEnabled = !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && uri != null;
        buttonsignup.setEnabled(isButtonEnabled);
        buttonsignup.setAlpha(isButtonEnabled ? 1.0f : 0.5f);
    }

    private Boolean isValidSignInDetails() {
        String username = editText_exusername.getText().toString().trim();
        String email = editText_exemail.getText().toString();
        String password = editText_expassword.getText().toString();
        String Confirmpassword = editText_exConfirmpassword.getText().toString();
        String normalizedPassword = password.replaceAll("[^\\x00-\\x7F]", "");
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Định dạng email không hợp lệ.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (username.length() <= 20) {
            Toast.makeText(SignUp.this, "Tên người dùng phải dài hơn 20 ký tự.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(normalizedPassword)) {
            Toast.makeText(getApplicationContext(), "Mật khẩu chứa dấu.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(Confirmpassword)) {
            Toast.makeText(SignUp.this, "Mật khẩu không phù hợp", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 8) {
            Toast.makeText(SignUp.this, "Mật khẩu phải dài ít nhất 8 ký tự.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
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
