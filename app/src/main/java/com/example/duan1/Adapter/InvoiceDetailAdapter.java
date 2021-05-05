package com.example.duan1.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Model.InvoiceDetail;
import com.example.duan1.Model.Product;
import com.example.duan1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.InvoiceDetailViewHolder> {

    ArrayList<InvoiceDetail> list;
    Context context;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");

    public InvoiceDetailAdapter(ArrayList<InvoiceDetail> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public InvoiceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice_detail, parent, false);
        return new InvoiceDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InvoiceDetailViewHolder holder, int position) {
        InvoiceDetail item = list.get(position);
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        databaseReference.child(item.getProductId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product item = snapshot.getValue(Product.class);
                if (item != null) {
                    holder.tvName.setText(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InvoiceDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvQuantity;

        public InvoiceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
