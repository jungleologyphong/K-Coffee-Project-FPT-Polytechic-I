package com.example.duan1.Fragment.HomeAdmin.Invoice;

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

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.InvoiceAdminAdapter;
import com.example.duan1.DAO.InvoiceDAO;
import com.example.duan1.Fragment.HomeAdmin.InvoiceDetailAdminFragment;
import com.example.duan1.Model.Invoice;
import com.example.duan1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CancelledInvoiceFragment extends Fragment implements InvoiceDAO.GetAllInvoiceInterface, InvoiceAdminAdapter.InvoiceAdminInterface {
    AutoCompleteTextView actvInvoice;
    RecyclerView rcvInvoice;
    ArrayList<Invoice> invoices;
    InvoiceAdminAdapter invoiceAdminAdapter;
    DatabaseReference databaseReference;
    InvoiceDAO invoiceDAO;

    public CancelledInvoiceFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);

        rcvInvoice = view.findViewById(R.id.rcvInvoice);
        actvInvoice = view.findViewById(R.id.actvInvoice);
        rcvInvoice.setLayoutManager(new LinearLayoutManager(getContext()));

        invoiceDAO = new InvoiceDAO(getContext(), this);

        invoices = new ArrayList<>();
        invoiceAdminAdapter = new InvoiceAdminAdapter(invoices, getContext(), this);
        rcvInvoice.setAdapter(invoiceAdminAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Invoice");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadInvoice();
        actvInvoice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    actvInvoice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    actvInvoice.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            invoiceAdminAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    actvInvoice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
                }
            }
        });
    }

    private void loadInvoice() {
        invoiceDAO.getAllByStatus("cancelled");
    }

    @Override
    public void getAllInvoice(ArrayList<Invoice> invoiceList) {
        invoices.clear();
        invoices.addAll(invoiceList);
        invoiceAdminAdapter.notifyDataSetChanged();
    }

    @Override
    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
        invoiceAdminAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeInvoice(Invoice invoice) {
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getId().equalsIgnoreCase(invoice.getId())) {
                invoices.set(i, invoice);
                invoiceAdminAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void removeInvoice(Invoice invoice) {
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getId().equalsIgnoreCase(invoice.getId())) {
                invoices.remove(i);
                invoiceAdminAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showDetail(int position) {
        ((HomeAdminActivity) getActivity()).switchFragment(new InvoiceDetailAdminFragment(invoices.get(position).getId()));
    }
}