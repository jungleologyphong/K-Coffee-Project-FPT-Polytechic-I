package com.example.duan1.Fragment.HomeCustomer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainCustomerFragment extends Fragment  {
    BottomNavigationView bnvMain;

    public MainCustomerFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_customer, container, false);

        bnvMain = view.findViewById(R.id.bnvMain);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (((HomeUserActivity) getActivity()).navPos == 0) {
            loadFragment(new HomeFragment());
        }

        bnvMain.setSelectedItemId(((HomeUserActivity) getActivity()).navPos);

        bnvMain.setOnNavigationItemSelectedListener(item -> {
            handleNavigation(item);
            return true;
        });
    }


    public void handleNavigation(MenuItem item) {
        Fragment selectedFragment = null;

        CharSequence title = item.getTitle();
        if (title.equals("Trang chủ")) {
            ((HomeUserActivity) getActivity()).navPos = R.id.nav_home;
            selectedFragment = new HomeFragment();
        } else if (title.equals("Hóa đơn")) {
            ((HomeUserActivity) getActivity()).navPos = R.id.nav_bill;
            selectedFragment = new InvoiceFragment();
        } else if (title.equals("Yêu thích")) {
            ((HomeUserActivity) getActivity()).navPos = R.id.nav_favorite;
            selectedFragment = new FavoriteFragment();
        } else if (title.equals("Cá nhân")) {
            ((HomeUserActivity) getActivity()).navPos = R.id.nav_person;
            selectedFragment = new PersonFragment();
        }

        loadFragment(selectedFragment);
    }


    private void loadFragment(Fragment fragment) {
        if (fragment != null && getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMain, fragment)
                    .commit();
        } else {
            Log.e("MainCustomerFragment", "loadFragment: Fragment hoặc Activity null");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}