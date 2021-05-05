package com.example.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HelloActivity extends AppCompatActivity {
    ImageView imgLogo, imgFpoly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

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