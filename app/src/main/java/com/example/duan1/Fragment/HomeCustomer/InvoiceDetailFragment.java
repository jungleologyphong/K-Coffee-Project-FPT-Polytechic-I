package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.Adapter.InvoiceDetailAdapter;
import com.example.duan1.DAO.InvoiceDAO;
import com.example.duan1.Model.Invoice;
import com.example.duan1.Model.InvoiceDetail;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvoiceDetailFragment extends Fragment implements InvoiceDAO.GetInvoiceInterface, InvoiceDetailDAO.GetAllInvoiceDetailInterface {
    TextView tvId, tvStatus, tvTotal, tvDeliveryFee, tvServiceFee, tvDiscount, tvSubTotal, tvFullName, tvAddress, tvPhoneNumber;
    User user;
    DatabaseReference databaseReference;
    RecyclerView rcvInvoiceDetail;
    //
    InvoiceDAO invoiceDAO;
    //
    DecimalFormat df = new DecimalFormat("###,###,###");
    //
    String invoiceId;
    //
    ArrayList<InvoiceDetail> invoiceDetails;
    InvoiceDetailDAO invoiceDetailDAO;
    InvoiceDetailAdapter invoiceDetailAdapter;


    public InvoiceDetailFragment(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_detail, container, false);

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
        rcvInvoiceDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        invoiceDetails = new ArrayList<>();
        invoiceDetailAdapter = new InvoiceDetailAdapter(invoiceDetails, getContext());

        invoiceDAO = new InvoiceDAO(getContext(), this);
        invoiceDetailDAO = new InvoiceDetailDAO(getContext(), this);

        rcvInvoiceDetail.setAdapter(invoiceDetailAdapter);

        user = HomeUserActivity.user;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCustomer();
        loadInvoiceDetail();
        invoiceDAO.getInvoiceById(invoiceId);
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
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Chi tiết hóa đơn");
    }

    @Override
    public void getSuccess(Invoice invoice) {
        if (invoice != null) {
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
}