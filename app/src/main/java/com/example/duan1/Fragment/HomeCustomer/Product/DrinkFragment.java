package com.example.duan1.Fragment.HomeCustomer.Product;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.Adapter.ProductSearchAdapter;
import com.example.duan1.DAO.ProductDAO;
import com.example.duan1.Model.Product;
import com.example.duan1.R;

import java.util.ArrayList;

public class DrinkFragment extends Fragment implements ProductDAO.GetAllProductInterface, ProductSearchAdapter.ProductSearchInterface {
    ArrayList<Product> products;
    ProductSearchAdapter productSearchAdapter;
    ProductDAO productDAO;
    RecyclerView rcvProduct;
    AutoCompleteTextView actvProduct;
    SwipeRefreshLayout srlProduct;

    public DrinkFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        actvProduct = view.findViewById(R.id.actvProduct);
        rcvProduct = view.findViewById(R.id.rcvProduct);
        srlProduct = view.findViewById(R.id.srlProduct);
        rcvProduct.setLayoutManager(new LinearLayoutManager(getContext()));

        products = new ArrayList<>();
        productSearchAdapter = new ProductSearchAdapter(products, getContext(), this);
        rcvProduct.setAdapter(productSearchAdapter);

        productDAO = new ProductDAO(getContext(), this);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProduct();
        actvProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    actvProduct.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            productSearchAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });
        srlProduct.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProduct();
                srlProduct.setRefreshing(false);
            }
        });
    }

    private void loadProduct() {
        productDAO.getAllByTypeNoRealtime("Drink");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Nước uống");
    }

    @Override
    public void getAllProduct(ArrayList<Product> productList) {
        products.clear();
        products.addAll(productList);
        productSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public void changeProduct(Product product) {

    }

    @Override
    public void removeProduct(Product product) {

    }

    @Override
    public void showDetail(Product product) {
        ((HomeUserActivity) getActivity()).showProductDetail(product);
    }
}
