package com.example.duan1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.BuildConfig;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText edtUsername, edtPassword;
    TextInputLayout textInputLayout;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtUsername = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        textInputLayout = findViewById(R.id.pass);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("vi");

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        if (BuildConfig.DEBUG) {
            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
            );
        } else {
            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
            );
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            edtUsername.setText(extras.getString("email", ""));
            textInputLayout.getEditText().setText(extras.getString("password", ""));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = new Date();

        checkLogin();
        //LoadLogin();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = edtUsername.getText().toString();
                String password = textInputLayout.getEditText().getText().toString();
                if (validate(email, password)) {

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    saveUser(email);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private boolean validate(String email, String password) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Các trường không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUser(String email) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    user.setId(item.getKey());
                    saveObjectToSharedPreference(getApplicationContext(), "User", "User", user);
                }
                checkUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkLogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            checkUser();
        }
    }

    private void checkUser() {
        User user = getSavedObjectFromPreference(this, "User", "User", User.class);
        if (user != null) {
            if (user.getRole().equalsIgnoreCase("customer")) {
                if (user.getStatus().equalsIgnoreCase("blocked")) {
                    showBlockedDialog();
                    FirebaseAuth.getInstance().signOut();
                } else {
                    Intent i = new Intent(LoginActivity.this, HomeUserActivity.class);
                    startActivity(i);
                    finish();
                }
            } else if (user.getRole().equalsIgnoreCase("admin")) {
                Intent i = new Intent(LoginActivity.this, HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(this, "Error... Please use another account!", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void showBlockedDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialog_blocked);
        dialog.setCancelable(false);

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void SaveUser(String username, String password, boolean check) {
        SharedPreferences preferences = getSharedPreferences("infoUser.dat", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (check) {
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("check", check);
        } else {
            editor.clear();
        }
        editor.commit();
    }

    private void LoadLogin() {
        SharedPreferences pref = getSharedPreferences("infoUser.dat", MODE_PRIVATE);
        boolean check = pref.getBoolean("check", false);
        if (check) {
            edtUsername.setText(pref.getString("username", ""));
            edtPassword.setText(pref.getString("password", ""));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}