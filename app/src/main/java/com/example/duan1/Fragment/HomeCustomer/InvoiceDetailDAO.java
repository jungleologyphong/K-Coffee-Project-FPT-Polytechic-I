package com.example.duan1.Fragment.HomeCustomer;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.duan1.Model.InvoiceDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvoiceDetailDAO {
    Context context;
    GetAllInvoiceDetailInterface getAllInvoiceDetailInterface;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("InvoiceDetail");

    public InvoiceDetailDAO(Context context, GetAllInvoiceDetailInterface getAllInvoiceDetailInterface) {
        this.context = context;
        this.getAllInvoiceDetailInterface = getAllInvoiceDetailInterface;
    }

    public void getAllInvoiceDetailByInvoiceId(String invoiceId) {
        final ArrayList<InvoiceDetail> invoiceDetails = new ArrayList<>();
        databaseReference.orderByChild("invoiceId").equalTo(invoiceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    InvoiceDetail invoiceDetail = item.getValue(InvoiceDetail.class);
                    if (invoiceDetail != null) {
                        invoiceDetail.setId(item.getKey());
                        invoiceDetails.add(invoiceDetail);
                    }
                }
                getAllInvoiceDetailInterface.getAllSuccess(invoiceDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface GetAllInvoiceDetailInterface {
        void getAllSuccess(ArrayList<InvoiceDetail> invoiceDetailList);
    }
}
