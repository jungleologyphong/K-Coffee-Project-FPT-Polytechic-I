package com.example.duan1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.SliderAdapter;
import com.example.duan1.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;


public class OnBoardingActivity extends AppCompatActivity {
    Button btnGetStarted;
    public static ViewPager viewPager;

    SliderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_broarding);

        btnGetStarted = findViewById(R.id.btnGetStarted);
        viewPager = findViewById(R.id.viewpager);
        adapter = new SliderAdapter(this);
        viewPager.setAdapter(adapter);

        checkUser();

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnBoardingActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        if (isOpenAlread()) {
            Intent intent = new Intent(OnBoardingActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("slide", MODE_PRIVATE).edit();
            editor.putBoolean("slide", true);
            editor.commit();
        }

    }

    private boolean isOpenAlread() {

        SharedPreferences sharedPreferences = getSharedPreferences("slide", MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("slide", false);
        return false;

    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    private void checkUser() {
        User user = getSavedObjectFromPreference(this, "User", "User", User.class);
        if (user != null) {
            if (user.getRole().equalsIgnoreCase("customer")) {
                if (user.getStatus().equalsIgnoreCase("blocked")) {
                    showBlockedDialog();
                    FirebaseAuth.getInstance().signOut();
                } else {
                    Intent i = new Intent(this, HomeUserActivity.class);
                    startActivity(i);
                    finish();
                }
            } else if (user.getRole().equalsIgnoreCase("admin")) {
                Intent i = new Intent(this, HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(this, "Error... Please use another account!", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void showBlockedDialog() {
        final Dialog dialog = new Dialog(this);
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
}
