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
import com.example.duan1.Adapter.ViewPagerAdapter;
import com.example.duan1.Fragment.HomeAdmin.User.ActiveUserFragment;
import com.example.duan1.Fragment.HomeAdmin.User.BlockedUserFragment;
import com.example.duan1.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class UserManageFragment extends Fragment {
    ViewPager2 vpUser;
    TabLayout tlUser;
    ViewPagerAdapter vpaUser;
    ArrayList<Fragment> fragments;

    public UserManageFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_manage, container, false);

        vpUser = view.findViewById(R.id.vpUser);
        tlUser = view.findViewById(R.id.tlUser);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragments = new ArrayList<>();
        fragments.add(new ActiveUserFragment());
        fragments.add(new BlockedUserFragment());
        vpaUser = new ViewPagerAdapter(this, fragments);
        vpUser.setAdapter(vpaUser);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tlUser, vpUser, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Hoạt động");
                        break;
                    case 1:
                        tab.setText("Đã khóa");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        vpUser.setUserInputEnabled(false);

        int betweenSpace = 16;

        ViewGroup slidingTabStrip = (ViewGroup) tlUser.getChildAt(0);

        for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Khách hàng");
        ((HomeAdminActivity) getActivity()).changeBackButton();
    }
}