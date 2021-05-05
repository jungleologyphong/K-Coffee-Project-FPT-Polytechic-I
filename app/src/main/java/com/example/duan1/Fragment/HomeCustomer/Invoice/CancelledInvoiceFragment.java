package com.example.duan1.Fragment.HomeCustomer.Invoice;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.duan1.Adapter.InvoiceAdapter;
import com.example.duan1.DAO.InvoiceDAO;
import com.example.duan1.Fragment.HomeCustomer.InvoiceDetailFragment;
import com.example.duan1.Model.Invoice;
import com.example.duan1.R;

import java.util.ArrayList;

public class CancelledInvoiceFragment extends Fragment implements InvoiceDAO.GetAllInvoiceInterface, InvoiceAdapter.InvoiceInterface {
    RecyclerView rcvInvoice;
    InvoiceAdapter invoiceAdapter;
    ArrayList<Invoice> invoices;
    SwipeRefreshLayout srlInvoice;
    InvoiceDAO invoiceDAO;
    AutoCompleteTextView actvInvoice;

    public CancelledInvoiceFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancelled_invoice, container, false);


        rcvInvoice = view.findViewById(R.id.rcvInvoice);
        srlInvoice = view.findViewById(R.id.srlInvoice);
        actvInvoice = view.findViewById(R.id.actvInvoice);

        invoiceDAO = new InvoiceDAO(getContext(), this);
        invoices = new ArrayList<>();
        invoiceAdapter = new InvoiceAdapter(invoices, getContext(), this);
        rcvInvoice.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvInvoice.setAdapter(invoiceAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadInvoice();

        srlInvoice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInvoice();
                srlInvoice.setRefreshing(false);
            }
        });
        actvInvoice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    actvInvoice.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            invoiceAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });
    }

    private void loadInvoice() {
        invoiceDAO.getAllByUserIdAndStatus(HomeUserActivity.user.getId(), "cancelled");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getAllInvoice(ArrayList<Invoice> invoiceList) {
        for (Invoice invoice : invoiceList) {
            Log.e("Cancelled invoice", invoice.toString());
        }
        invoices.clear();
        invoices.addAll(invoiceList);
        invoiceAdapter.notifyDataSetChanged();
    }

    @Override
    public void addInvoice(Invoice invoice) {

    }

    @Override
    public void changeInvoice(Invoice invoice) {

    }

    @Override
    public void removeInvoice(Invoice invoice) {

    }

    @Override
    public void showDetail(int position) {
        ((HomeUserActivity) getActivity()).switchFragment(new InvoiceDetailFragment(invoices.get(position).getId()));
    }
}