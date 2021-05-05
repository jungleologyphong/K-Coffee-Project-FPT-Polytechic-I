package com.example.duan1.Fragment.HomeAdmin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.InvoiceDetailAdapter;
import com.example.duan1.DAO.InvoiceDAO;
import com.example.duan1.Fragment.HomeCustomer.InvoiceDetailDAO;
import com.example.duan1.Model.Invoice;
import com.example.duan1.Model.InvoiceDetail;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvoiceDetailAdminFragment extends Fragment implements InvoiceDAO.GetInvoiceInterface, InvoiceDetailDAO.GetAllInvoiceDetailInterface, InvoiceDAO.UpdateInvoiceInterface {
    TextView tvId, tvStatus, tvTotal, tvDeliveryFee, tvServiceFee, tvDiscount, tvSubTotal, tvFullName, tvAddress, tvPhoneNumber;
    Button btnCancel, btnConfirm;
    LinearLayout llButton;
    User user;
    DatabaseReference databaseReference;
    RecyclerView rcvInvoiceDetail;
    //
    InvoiceDAO invoiceDAO;
    InvoiceDAO invoiceDAO2;
    //
    DecimalFormat df = new DecimalFormat("###,###,###");
    //
    String invoiceId;
    //
    ArrayList<InvoiceDetail> invoiceDetails;
    InvoiceDetailDAO invoiceDetailDAO;
    InvoiceDetailAdapter invoiceDetailAdapter;
    //
    Invoice invoice;


    public InvoiceDetailAdminFragment(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_admin_detail, container, false);

        tvId = view.findViewById(R.id.tvId);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDeliveryFee = view.findViewById(R.id.tvDeliveryFee);
        tvServiceFee = view.findViewById(R.id.tvServiceFee);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        rcvInvoiceDetail = view.findViewById(R.id.rcvInvoiceDetail);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        llButton = view.findViewById(R.id.llButton);
        rcvInvoiceDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        invoiceDetails = new ArrayList<>();
        invoiceDetailAdapter = new InvoiceDetailAdapter(invoiceDetails, getContext());

        invoiceDAO = new InvoiceDAO(getContext(), this);
        invoiceDAO2 = new InvoiceDAO(getContext(), this, 0);
        invoiceDetailDAO = new InvoiceDetailDAO(getContext(), this);

        rcvInvoiceDetail.setAdapter(invoiceDetailAdapter);

        user = HomeAdminActivity.user;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCustomer();
        loadInvoiceDetail();
        invoiceDAO.getInvoiceById(invoiceId);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoice.setStatus("delivered");
                invoice.setUserId_status(invoice.getUserId() + "_" + "delivered");
                invoiceDAO2.update(invoice, "delivered");
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_confirm);
                dialog.setCancelable(false);

                TextView tvTitle = dialog.findViewById(R.id.tvTitle);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

                tvTitle.setText("Xác nhận hủy đơn hàng?");
                dialog.show();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        invoice.setStatus("cancelled");
                        invoice.setUserId_status(invoice.getUserId() + "_" + "cancelled");
                        invoiceDAO2.update(invoice, "cancelled");
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void loadInvoiceDetail() {
        invoiceDetailDAO.getAllInvoiceDetailByInvoiceId(invoiceId);
    }

    private void loadCustomer() {
        tvFullName.setText(user.getFullName());
        tvAddress.setText(user.getPhoneNumber());
        tvPhoneNumber.setText(user.getAddress());
    }


    public void setData(Invoice invoice) {
        tvId.setText(invoice.getId());
        tvTotal.setText(df.format(invoice.getTotal()));
        tvDeliveryFee.setText(df.format(invoice.getDeliveryFee()));
        tvServiceFee.setText(df.format(invoice.getServiceFee()));
        tvDiscount.setText(df.format(invoice.getDiscount()));
        tvSubTotal.setText(df.format(invoice.getSubTotal()));
        if (invoice.getStatus().equalsIgnoreCase("cancelled")) {
            tvStatus.setText("Đã hủy");
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.Cancelled));
        } else if (invoice.getStatus().equalsIgnoreCase("ordered")) {
            tvStatus.setText("Đã đặt");
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.Ordered));
        } else if (invoice.getStatus().equalsIgnoreCase("delivered")) {
            tvStatus.setText("Đã giao");
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.Delivered));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAdminActivity) getActivity()).changeBackButton();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Chi tiết hóa đơn");
    }

    @Override
    public void getSuccess(Invoice invoice) {
        if (invoice != null) {
            this.invoice = invoice;
            if(invoice.getStatus().equalsIgnoreCase("ordered")){
                llButton.setVisibility(View.VISIBLE);
            }
            setData(invoice);
        } else {
            Toast.makeText(getContext(), "Có lỗi xảy ra...", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
    }

    @Override
    public void getAllSuccess(ArrayList<InvoiceDetail> invoiceDetailList) {
        invoiceDetails.clear();
        invoiceDetails.addAll(invoiceDetailList);
        invoiceDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateinvoice() {
        getActivity().onBackPressed();
    }
}