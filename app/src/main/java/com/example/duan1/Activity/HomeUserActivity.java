package com.example.duan1.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.duan1.DAO.ViewedProductDAO;
import com.example.duan1.Fragment.HomeCustomer.CartFragment;
import com.example.duan1.Fragment.HomeCustomer.MainCustomerFragment;
import com.example.duan1.Fragment.HomeCustomer.ProductDetailFragment;
import com.example.duan1.LoginActivity;
import com.example.duan1.Model.Product;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeUserActivity extends AppCompatActivity {
    public static User user;
    public int currentMenu = R.menu.cart;

    public Uri uri;
    FrameLayout flHome;
    Toolbar tHome;
    ViewedProductDAO viewedProductDAO;
    public int navPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        flHome = findViewById(R.id.flHome);
        tHome = findViewById(R.id.tHome);

        viewedProductDAO = new ViewedProductDAO(getApplicationContext());

        user = LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "User", "User", User.class);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flHome, new MainCustomerFragment())
                .commit();

        setSupportActionBar(tHome);
        getSupportActionBar().setTitle("Trang chủ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(currentMenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                switchFragment(new CartFragment());
                break;
        }
        return true;
    }


    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up)
                .replace(R.id.flHome, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeBackButton() {
        if (getFragmentCount() != 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }


    public void showProductDetail(Product product) {
        switchFragment(new ProductDetailFragment(product.getId()));
        if (viewedProductDAO.checkExist(product.getId())) {
            viewedProductDAO.delete(product.getId());
        }
        viewedProductDAO.insert(product);
    }

    public void back() {
        super.onBackPressed();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "User", "User", null);
        Intent intent = new Intent(HomeUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}