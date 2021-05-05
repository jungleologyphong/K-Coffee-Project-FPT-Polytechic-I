package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.Adapter.ProductAdapter;
import com.example.duan1.Adapter.ViewedProductAdapter;
import com.example.duan1.DAO.ProductDAO;
import com.example.duan1.DAO.ViewedProductDAO;
import com.example.duan1.Fragment.HomeCustomer.Product.DrinkFragment;
import com.example.duan1.Fragment.HomeCustomer.Product.FoodFragment;
import com.example.duan1.Fragment.HomeCustomer.Product.SearchFragment;
import com.example.duan1.Model.Product;
import com.example.duan1.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ProductDAO.GetRecommendInterface, ViewedProductAdapter.ViewedProductInterface, ProductAdapter.ProductInterface {
    RecyclerView rcvRecommend, rcvViewed;
    // Viewed
    ViewedProductDAO viewedProductDAO;
    ViewedProductAdapter viewedProductAdapter;
    ArrayList<Product> viewedProductList;
    //
    ProductDAO productDAO;
    ArrayList<Product> recommendList;
    ProductAdapter productAdapter;
    //
    LinearLayout llFood, llDrink;
    EditText edtSearch;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvRecommend = view.findViewById(R.id.rcvRecommend);
        rcvViewed = view.findViewById(R.id.rcvViewed);
        llFood = view.findViewById(R.id.llFood);
        edtSearch = view.findViewById(R.id.edtSearch);
        llDrink = view.findViewById(R.id.llDrink);

        viewedProductDAO = new ViewedProductDAO(getContext());
        productDAO = new ProductDAO(getContext(), this);

        rcvRecommend.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rcvViewed.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        viewedProductList = new ArrayList<>();
        viewedProductAdapter = new ViewedProductAdapter(viewedProductList, getContext(), this);
        rcvViewed.setAdapter(viewedProductAdapter);
        //
        recommendList = new ArrayList<>();
        productAdapter = new ProductAdapter(recommendList, getContext(), this);
        rcvRecommend.setAdapter(productAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRecommendList();
        llDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeUserActivity) getActivity()).switchFragment(new DrinkFragment());
            }
        });
        llFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeUserActivity) getActivity()).switchFragment(new FoodFragment());
            }
        });
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeUserActivity) getActivity()).switchFragment(new SearchFragment());
            }
        });
    }

    private void loadRecommendList() {
        productDAO.getRecommend();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Trang chủ");
        loadViewedProduct();
    }

    private void loadViewedProduct() {
        viewedProductList = viewedProductDAO.getAll();
        viewedProductAdapter = new ViewedProductAdapter(viewedProductList, getContext(), this);
        rcvViewed.setAdapter(viewedProductAdapter);
    }

    @Override
    public void getRecommend(ArrayList<Product> productList) {
        recommendList.clear();
        recommendList.addAll(productList);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDetail(Product product) {
        ((HomeUserActivity) getActivity()).showProductDetail(product);
    }
}