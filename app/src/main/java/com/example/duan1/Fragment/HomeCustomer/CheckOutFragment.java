package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.DAO.CartDAO;
import com.example.duan1.DAO.InvoiceDAO;
import com.example.duan1.Model.CartItem;
import com.example.duan1.Model.Invoice;
import com.example.duan1.Model.Material.SquareImageView;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckOutFragment extends Fragment implements InvoiceDAO.ConfirmOrderInterface {
    CartDAO cartDAO;
    TextView tvTotal, tvDeliveryFee, tvServiceFee, tvDiscount, tvSubTotal, tvFullName, tvAddress, tvPhoneNumber;
    Button btnOrder;
    CheckBox cbCOD, cbInternetBanking, cbATM;
    SquareImageView sivEdit;
    User user;
    DatabaseReference databaseReference;
    //
    ArrayList<CartItem> list;
    //
    InvoiceDAO invoiceDAO;
    //
    DecimalFormat df = new DecimalFormat("###,###,###");
    //
    String fullName = "";
    String address = "";
    String phoneNumber = "";

    long total = 0;
    long deliveryFee = 0;
    long serviceFee = 0;
    long discount = 0;
    long subTotal = 0;

    public CheckOutFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        tvTotal = view.findViewById(R.id.tvTotal);
        tvDeliveryFee = view.findViewById(R.id.tvDeliveryFee);
        tvServiceFee = view.findViewById(R.id.tvServiceFee);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        btnOrder = view.findViewById(R.id.btnOrder);
        sivEdit = view.findViewById(R.id.sivEdit);
        cbCOD = view.findViewById(R.id.cbCOD);

        invoiceDAO = new InvoiceDAO(getContext(), this);

        user = HomeUserActivity.user;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        cartDAO = new CartDAO(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCartItemList();
        loadTotal();
        loadCustomer();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    confirmOrder();
                }
            }
        });
        sivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void getCartItemList() {
        list = cartDAO.getAll();
    }

    public boolean validate() {
        if (fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(getContext(), "Chưa cập nhật đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!cbCOD.isChecked()) {
            Toast.makeText(getContext(), "Chưa chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void confirmOrder() {
        Date date = new Date();
        final Invoice invoice = new Invoice(user.getId(), total, deliveryFee, serviceFee, discount, subTotal, date.getTime(), "COD", fullName, address, phoneNumber, "ordered");
        invoiceDAO.confirmOrder(invoice, list);
    }

    private void clearList() {
        cartDAO.deleteAll();
    }

    private void loadCustomer() {
        fullName = user.getFullName();
        phoneNumber = user.getPhoneNumber();
        address = user.getAddress();
        setCustomerData();
    }

    private void setCustomerData() {
        tvFullName.setText(fullName);
        tvAddress.setText(address);
        tvPhoneNumber.setText(phoneNumber);
    }

    private void loadTotal() {
        total = cartDAO.totalPrice();
        deliveryFee = 20000;
        serviceFee = 1000;
        //
        if (total > 100000) {
            double data = (double) total * 5 / 100;
            data = Math.ceil(data / 1000) * 1000;
            discount = (long) data;
        }
        //
        subTotal = total + deliveryFee + serviceFee - discount;
        //
        tvTotal.setText(df.format(total));
        tvDeliveryFee.setText(df.format(deliveryFee));
        tvServiceFee.setText(df.format(serviceFee));
        tvDiscount.setText(df.format(discount));
        tvSubTotal.setText(df.format(subTotal));
    }

    public void showEditDialog() {
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext());
        bsd.setContentView(R.layout.dialog_edit_checkout_info);

        final EditText edtFullName, edtPhoneNumber, edtAddress;
        Button btnChange, btnCancel;

        edtFullName = bsd.findViewById(R.id.edtFullName);
        edtPhoneNumber = bsd.findViewById(R.id.edtPhoneNumber);
        edtAddress = bsd.findViewById(R.id.edtAddress);
        btnChange = bsd.findViewById(R.id.btnChange);
        btnCancel = bsd.findViewById(R.id.btnCancel);

        edtFullName.setText(fullName);
        edtPhoneNumber.setText(phoneNumber);
        edtAddress.setText(address);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = edtFullName.getText().toString();
                phoneNumber = edtPhoneNumber.getText().toString();
                address = edtAddress.getText().toString();
                setCustomerData();
                bsd.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsd.dismiss();
            }
        });

        bsd.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Thanh toán");
    }

    @Override
    public void orderSuccess() {
        Toast.makeText(getContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
        clearList();
        getActivity().onBackPressed();
    }
}