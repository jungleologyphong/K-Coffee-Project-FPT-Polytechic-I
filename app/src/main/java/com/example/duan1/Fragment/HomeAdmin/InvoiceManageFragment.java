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
import com.example.duan1.Adapter.ViewPagerBillAdapter;
import com.example.duan1.Fragment.HomeAdmin.Invoice.CancelledInvoiceFragment;
import com.example.duan1.Fragment.HomeAdmin.Invoice.DeliveredInvoiceFragment;
import com.example.duan1.Fragment.HomeAdmin.Invoice.OrderedInvoiceFragment;
import com.example.duan1.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class InvoiceManageFragment extends Fragment {

    ViewPager2 vpInvoice;
    TabLayout tlInvoice;
    ViewPagerBillAdapter viewPagerBillAdapter;
    ArrayList<Fragment> fragments;

    public InvoiceManageFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_manage, container, false);

        vpInvoice = view.findViewById(R.id.vpInvoice);
        tlInvoice = view.findViewById(R.id.tlInvoice);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragments = new ArrayList<>();
        fragments.add(new OrderedInvoiceFragment());
        fragments.add(new DeliveredInvoiceFragment());
        fragments.add(new CancelledInvoiceFragment());
        viewPagerBillAdapter = new ViewPagerBillAdapter(this, fragments);
        vpInvoice.setAdapter(viewPagerBillAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tlInvoice, vpInvoice, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Đã đặt");
                        break;
                    case 1:
                        tab.setText("Đã giao");
                        break;
                    case 2:
                        tab.setText("Đã hủy");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        vpInvoice.setUserInputEnabled(false);

        int betweenSpace = 16;

        ViewGroup slidingTabStrip = (ViewGroup) tlInvoice.getChildAt(0);

        for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Hóa đơn");
        ((HomeAdminActivity) getActivity()).changeBackButton();
    }
}