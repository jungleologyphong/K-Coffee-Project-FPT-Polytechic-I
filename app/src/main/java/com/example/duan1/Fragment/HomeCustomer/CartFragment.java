package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.Adapter.CartAdapter;
import com.example.duan1.DAO.CartDAO;
import com.example.duan1.Model.CartItem;
import com.example.duan1.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment implements CartAdapter.CartAdapterInterface {
    RecyclerView rcvCart;
    CartAdapter cartAdapter;
    ArrayList<CartItem> cartItems;
    CartDAO cartDAO;
    TextView tvTotal;
    Button btnCheckOut;
    //
    DecimalFormat df = new DecimalFormat("###,###,###");
    //
    long total = 0;

    public CartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rcvCart = view.findViewById(R.id.rcvCart);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnCheckOut = view.findViewById(R.id.btnCheckOut);
        rcvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        cartDAO = new CartDAO(getContext());

        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems, getContext(), this);
        rcvCart.setAdapter(cartAdapter);
        loadCart();
        getTotal();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    ((HomeUserActivity) getActivity()).switchFragment(new CheckOutFragment());
                }
            }
        });
    }

    private boolean validate() {
        if (total == 0) {
            Toast.makeText(getContext(), "Chưa có sản phẩm nào trong giỏ hàng", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadCart() {
        cartItems = cartDAO.getAll();
        cartAdapter = new CartAdapter(cartItems, getContext(), this);
        rcvCart.setAdapter(cartAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).currentMenu = R.menu.blank;
        ((HomeUserActivity) getActivity()).invalidateOptionsMenu();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Giỏ hàng");
    }

    @Override
    public void update() {
        getTotal();
    }

    private void getTotal() {
        total = cartDAO.totalPrice();
        tvTotal.setText(df.format(total));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeUserActivity) getActivity()).currentMenu = R.menu.cart;
        ((HomeUserActivity) getActivity()).invalidateOptionsMenu();
    }
}