package com.example.duan1.Fragment.HomeCustomer;

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

public class MainCustomerFragment extends Fragment {
    BottomNavigationView bnvMain;

    public MainCustomerFragment() {
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


        bnvMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        ((HomeUserActivity) getActivity()).navPos = R.id.nav_home;
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_bill:
                        ((HomeUserActivity) getActivity()).navPos = R.id.nav_bill;
                        selectedFragment = new InvoiceFragment();
                        break;
                    case R.id.nav_favorite:
                        ((HomeUserActivity) getActivity()).navPos = R.id.nav_favorite;
                        selectedFragment = new FavoriteFragment();
                        break;
                    case R.id.nav_person:
                        ((HomeUserActivity) getActivity()).navPos = R.id.nav_person;
                        selectedFragment = new PersonFragment();
                        break;
                }
                loadFragment(selectedFragment);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.flMain, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}