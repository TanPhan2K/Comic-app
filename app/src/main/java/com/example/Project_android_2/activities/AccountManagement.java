package com.example.Project_android_2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.AccessToken;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.Project_android_2.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

public class AccountManagement extends AppCompatActivity {
    private AppCompatImageView appCompatImageView_exit;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button butonsignin, btButtonTiwtter, buttongoogle, buttonfacebook;
    CallbackManager callbackManager;
    private FrameLayout progressBar;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Xử lý kết quả ở đây
                    if (result.getResultCode() == RESULT_OK) {
                        progressBar.setVisibility(View.VISIBLE);
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                            progressBar.setVisibility(View.VISIBLE);
                        } catch (ApiException e) {
                            Toast.makeText(AccountManagement.this, "Đăng nhập Google không thành công", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountmanagement);
        setupUI();

        callbackManager = CallbackManager.Factory.create();
        //google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        boolean logoutRequested = getIntent().getBooleanExtra("logout", false);
        if (logoutRequested) {
            // Thực hiện đăng xuất
            signOut();
        }
        handleclickbutton();
//        setupFaceBook();
        // Nếu đã đăng nhập, chuyển hướng trực tiếp đến t Home
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String userUid = sharedPreferences.getString("user_uid", "");
            String userEmail = sharedPreferences.getString("userEmail", "");
            String userName = sharedPreferences.getString("userName", "");
            String photoUrl = sharedPreferences.getString("photoUrl", "");

            // Chuyển hướng người dùng đến màn hình Home và truyền thông tin người dùng
            Intent intent = new Intent(AccountManagement.this, Home.class);
            startActivity(intent);
            finish();
        }

    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);

    }

    public void setupUI() {
        appCompatImageView_exit = findViewById(R.id.exit);
        progressBar = findViewById(R.id.progressBar);
        butonsignin = findViewById(R.id.butonsignin);
        buttongoogle = findViewById(R.id.buttongoogle);
        buttonfacebook = findViewById(R.id.buttonfacebook);
        btButtonTiwtter = findViewById(R.id.buttontwitter);
        loading();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AccountManagement.this, "Đăng nhập Google không thành công. Vui lòng thử lại hoặc sử dụng phương pháp khác để đăng nhập.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            mGoogleSignInClient.signOut();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void handleFaceBookAccestoken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(AccountManagement.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void setupFaceBook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(AccountManagement.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Xử lý khi đăng nhập thành công
                Toast.makeText(AccountManagement.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);

                handleFaceBookAccestoken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(AccountManagement.this, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
                updateUI(null);
                mGoogleSignInClient.signOut();
            }
        });
    }

    private void setupTiwtter() {

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        // Localize to French.
        provider.addCustomParameter("lang", "fr");

        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            progressBar.setVisibility(View.VISIBLE);
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(AccountManagement.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    progressBar.setVisibility(View.GONE);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AccountManagement.this, "Lỗi đăng nhập :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
        } else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth
                    .startActivityForSignInWithProvider(AccountManagement.this, provider.build())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Xử lý khi đăng nhập thành công với Twitter
                            Toast.makeText(AccountManagement.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            progressBar.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý khi đăng nhập thất bại
                            Toast.makeText(AccountManagement.this, "Lỗi đăng nhập :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void saveUserInfoToSharedPreferences(String userEmail, String userName, String photoUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userEmail", userEmail);
        editor.putString("userName", userName);
        editor.putString("photoUrl", photoUrl);
        editor.apply();
    }

    private void clearUserInfoFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("userEmail");
        editor.remove("userName");
        editor.remove("userUid");
        editor.remove("photoUrl");
        editor.apply();
    }
    private void handleclickbutton() {
        //thực hiện click button
        buttongoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        appCompatImageView_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        butonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountManagement.this, SignIn.class);
                startActivity(intent);
            }
        });
        buttonfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupFaceBook();
            }
        });
        btButtonTiwtter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setupTiwtter();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        clearUserInfoFromSharedPreferences();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AccountManagement.this, "Đăng xuất thành công.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(AccountManagement.this, Home.class);
            saveUserInfoToSharedPreferences(user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString()) ;
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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