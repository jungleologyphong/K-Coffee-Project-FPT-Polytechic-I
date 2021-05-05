package com.example.duan1.Fragment.HomeAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.ViewPagerProductAdapter;
import com.example.duan1.Fragment.HomeAdmin.Product.DrinkFragment;
import com.example.duan1.Fragment.HomeAdmin.Product.FoodFragment;
import com.example.duan1.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ProductManageFragment extends Fragment {
    ViewPager2 vpProduct;
    TabLayout tlProduct;
    ArrayList<Fragment> fragments;
    ViewPagerProductAdapter viewPagerProductAdapter;

    public ProductManageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_manage, container, false);

        vpProduct = view.findViewById(R.id.vpProduct);
        tlProduct = view.findViewById(R.id.tlProduct);

        fragments = new ArrayList<>();
        fragments.add(new FoodFragment());
        fragments.add(new DrinkFragment());

        viewPagerProductAdapter = new ViewPagerProductAdapter(this, fragments);
        vpProduct.setAdapter(viewPagerProductAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tlProduct, vpProduct, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Đồ ăn");
                        break;
                    case 1:
                        tab.setText("Nước uống");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        vpProduct.setUserInputEnabled(false);

        int betweenSpace = 16;

        ViewGroup slidingTabStrip = (ViewGroup) tlProduct.getChildAt(0);

        for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAdminActivity) getActivity()).currentMenu = R.menu.product;
        getActivity().invalidateOptionsMenu();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Tin nhắn");
        ((HomeAdminActivity) getActivity()).changeBackButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeAdminActivity) getActivity()).currentMenu = R.menu.message;
        getActivity().invalidateOptionsMenu();
    }
}