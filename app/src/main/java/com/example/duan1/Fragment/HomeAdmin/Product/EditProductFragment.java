package com.example.duan1.Fragment.HomeAdmin.Product;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.DAO.ProductDAO;
import com.example.duan1.Model.Product;
import com.example.duan1.R;
import com.squareup.picasso.Picasso;

public class EditProductFragment extends Fragment implements ProductDAO.UpdateProductInterface {
    ImageView ivProduct;
    EditText edtName, edtPrice, edtDescribe, edtCode;
    Button btnUpdate, btnCancel;
    Uri uri;
    ProductDAO productDAO;
    Product oldProduct;

    public EditProductFragment(Product oldProduct) {
        this.oldProduct = oldProduct;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_product, container, false);

        ivProduct = view.findViewById(R.id.ivProduct);
        edtName = view.findViewById(R.id.edtName);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtDescribe = view.findViewById(R.id.edtDescribe);
        edtCode = view.findViewById(R.id.edtCode);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnCancel = view.findViewById(R.id.btnCancel);

        productDAO = new ProductDAO(getContext(), this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String name = edtName.getText().toString();
                    String code = edtCode.getText().toString();
                    String describe = edtDescribe.getText().toString();
                    long price = Long.parseLong(edtPrice.getText().toString());
                    Product newProduct = new Product(oldProduct.getType(), name, code, price, describe);
                    newProduct.setId(oldProduct.getId());
                    productDAO.updateProduct(newProduct, uri);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void loadData() {
        edtName.setText(oldProduct.getName());
        edtCode.setText(oldProduct.getCode());
        edtDescribe.setText(oldProduct.getDescribe());
        edtPrice.setText(String.valueOf(oldProduct.getPrice()));
        if (oldProduct.getImage() != null) {
            Picasso.with(getContext()).load(oldProduct.getImage()).into(ivProduct);
        }
    }

    private void chooseImage() {
        ((HomeAdminActivity) getActivity()).chooseImage();
    }

    private boolean validate() {
        if (edtName.getText().toString().isEmpty() || edtCode.getText().toString().isEmpty() || edtPrice.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Các trường không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        uri = ((HomeAdminActivity) getActivity()).uri;
        if (uri != null) {
            Picasso.with(getContext()).load(uri).into(ivProduct);
        }
        ((HomeAdminActivity) getActivity()).currentMenu = R.menu.blank;
        ((HomeAdminActivity) getActivity()).invalidateOptionsMenu();
        ((HomeAdminActivity) getActivity()).changeBackButton();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Cập nhật sản phẩm");
    }


    @Override
    public void upadeSuccess() {

        ((HomeAdminActivity) getActivity()).uri = null;
        getActivity().onBackPressed();
    }
}
