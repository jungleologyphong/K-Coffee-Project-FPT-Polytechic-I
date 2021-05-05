package com.example.duan1.Fragment.HomeAdmin.Product;

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

import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.ProductAdminAdapter;
import com.example.duan1.DAO.ProductDAO;
import com.example.duan1.Model.Product;
import com.example.duan1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DrinkFragment extends Fragment implements ProductDAO.GetAllProductInterface, ProductAdminAdapter.ProductAdminInterface {
    AutoCompleteTextView actvProduct;
    RecyclerView rcvProduct;
    ArrayList<Product> products;
    ProductAdminAdapter productAdminAdapter;
    DatabaseReference databaseReference;
    ProductDAO productDAO;

    public DrinkFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_admin, container, false);

        actvProduct = view.findViewById(R.id.actvProduct);
        rcvProduct = view.findViewById(R.id.rcvProduct);
        rcvProduct.setLayoutManager(new LinearLayoutManager(getContext()));

        productDAO = new ProductDAO(getContext(), this);

        products = new ArrayList<>();
        productAdminAdapter = new ProductAdminAdapter(products, getContext(), this);
        rcvProduct.setAdapter(productAdminAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDrinkProduct();
        actvProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    actvProduct.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    actvProduct.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            productAdminAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    actvProduct.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
                }
            }
        });
    }

    private void loadDrinkProduct() {
        productDAO.getAllByType("Drink");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getAllProduct(ArrayList<Product> productList) {

    }

    @Override
    public void addProduct(Product product) {
        products.add(0, product);
        productAdminAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(product.getId())) {
                products.set(i, product);
                productAdminAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void removeProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(product.getId())) {
                products.remove(i);
                productAdminAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void update(Product product) {
        ((HomeAdminActivity) getActivity()).switchFragment(new EditProductFragment(product));
    }
}