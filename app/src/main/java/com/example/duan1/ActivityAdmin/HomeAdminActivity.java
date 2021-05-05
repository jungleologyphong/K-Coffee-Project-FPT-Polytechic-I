package com.example.duan1.ActivityAdmin;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.duan1.Fragment.HomeAdmin.MainAdminFragment;
import com.example.duan1.Fragment.HomeAdmin.MessageManageFragment;
import com.example.duan1.Fragment.HomeAdmin.Product.AddProductFragment;
import com.example.duan1.LoginActivity;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeAdminActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static User user;
    public int currentMenu = R.menu.message;
    public Uri uri;
    FrameLayout flHomeAdmin;
    Toolbar tHomeAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        flHomeAdmin = findViewById(R.id.flHomeAdmin);
        tHomeAdmin = findViewById(R.id.tHomeAdmin);

        user = LoginActivity.getSavedObjectFromPreference(this, "User", "User", User.class);

        Log.e("User", user.toString());
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up)
                .replace(R.id.flHomeAdmin, new MainAdminFragment())
                .commit();

        setSupportActionBar(tHomeAdmin);
        getSupportActionBar().setTitle("Trang chủ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tHomeAdmin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up)
                .replace(R.id.flHomeAdmin, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(currentMenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFood:
                switchFragment(new AddProductFragment("Food"));
                break;
            case R.id.addDrink:
                switchFragment(new AddProductFragment("Drink"));
                break;
            case R.id.message:
                switchFragment(new MessageManageFragment());
                break;
        }
        return true;
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "User", "User", null);
        Intent intent = new Intent(HomeAdminActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }


    public void changeBackButton() {
        if (getFragmentCount() != 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
        }
    }
}