package com.example.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class HelloActivity extends AppCompatActivity {
    ImageView imgLogo, imgFpoly;
    private static boolean cloudinaryInitialized = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        String cloudName = getString(R.string.cloudinary_name);
        String apiKeyCloudinary = getString(R.string.cloudinary_apiKey);
        String apiSecretKeyCloudinary = getString(R.string.cloudinary_secretKey);

        Log.e("HelloActivity", "onCreate: " + cloudName + " " + apiKeyCloudinary + " " + apiSecretKeyCloudinary );

        if (!cloudinaryInitialized) {
            Map config = new HashMap();
            config.put("cloud_name", cloudName);
            config.put("api_key", apiKeyCloudinary);
            config.put("api_secret", apiSecretKeyCloudinary);

            MediaManager.init(this, config);
            cloudinaryInitialized = true;
        }

        final Animation buttonClick = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        Thread bamgio = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(HelloActivity.this, OnBoardingActivity.class);
                    startActivity(i);
                }
            }
        };
        bamgio.start();
    }
    protected void onPause() {
        super.onPause();
        finish();
    }
}