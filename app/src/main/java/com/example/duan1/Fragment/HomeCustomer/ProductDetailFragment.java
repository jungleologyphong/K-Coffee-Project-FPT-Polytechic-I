package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.DAO.CartDAO;
import com.example.duan1.DAO.FavoriteDAO;
import com.example.duan1.DAO.ProductDAO;
import com.example.duan1.Model.CartItem;
import com.example.duan1.Model.Favorite;
import com.example.duan1.Model.Product;
import com.example.duan1.Model.Material.SquareImageView;
import com.example.duan1.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ProductDetailFragment extends Fragment implements ProductDAO.GetProductInterface, FavoriteDAO.FavoriteInterface {
    SquareImageView sivProduct;
    TextView tvName, tvPrice, tvDescribe;
    Button btnAddToCart, btnBuy;
    ImageView ivLike;
    //
    Product product;
    String productId;
    ProductDAO productDAO;
    //
    DecimalFormat df = new DecimalFormat("###,###,###đ");
    //
    FavoriteDAO favoriteDAO;
    Favorite favorite = null;
    //
    CartDAO cartDAO;

    //
    public ProductDetailFragment(String productId) {
        this.productId = productId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        sivProduct = view.findViewById(R.id.sivProduct);
        tvName = view.findViewById(R.id.tvName);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvDescribe = view.findViewById(R.id.tvDescribe);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        btnBuy = view.findViewById(R.id.btnBuy);
        ivLike = view.findViewById(R.id.ivLike);

        productDAO = new ProductDAO(getContext(), this);
        favoriteDAO = new FavoriteDAO(getContext(), this);
        cartDAO = new CartDAO(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProduct();
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorite != null) {
                    favoriteDAO.unFavorite(favorite.getId());
                } else {
                    favoriteDAO.favorite(HomeUserActivity.user.getId(), productId);
                }
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem cartItem = new CartItem(productId, product.getPrice(), 1);
                if (!cartDAO.checkExist(productId)) {
                    cartDAO.insert(cartItem);
                }
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem cartItem = new CartItem(productId, product.getPrice(), 1);
                if (!cartDAO.checkExist(productId)) {
                    cartDAO.insert(cartItem);
                }
                ((HomeUserActivity) getActivity()).switchFragment(new CartFragment());
            }
        });
    }

    private void loadProduct() {
        productDAO.getProductById(productId);
        favoriteDAO.getFavoriteStateByUserIdAndProductId(HomeUserActivity.user.getId(), productId);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Chi tiết sản phẩm");
    }

    @Override
    public void getProduct(Product product) {
        this.product = product;
        if (product.getImage() != null) {
            Picasso.with(getContext()).load(product.getImage()).into(sivProduct);
        } else {
            Log.e("X", "null");
        }
        tvName.setText(product.getName());
        tvPrice.setText(df.format(product.getPrice()));
        tvDescribe.setText(product.getDescribe());
    }

    @Override
    public void getFavorite(Favorite favorite) {
        if (favorite != null) {
            ivLike.setBackgroundResource(R.drawable.like);
            this.favorite = favorite;
        } else {
            ivLike.setBackgroundResource(R.drawable.likeblue);
            this.favorite = null;
        }
    }
}