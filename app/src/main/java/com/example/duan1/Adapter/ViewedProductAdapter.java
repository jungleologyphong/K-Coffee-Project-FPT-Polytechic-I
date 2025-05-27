package com.example.duan1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.DAO.ViewedProductDAO;
import com.example.duan1.Model.Product;
import com.example.duan1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewedProductAdapter extends RecyclerView.Adapter<ViewedProductAdapter.ViewedProductViewholder> {
    List<Product> list;
    Context context;
    ViewedProductDAO viewedProductDAO;
    ViewedProductInterface viewedProductInterface;

    public ViewedProductAdapter(List<Product> list, Context context, ViewedProductInterface viewedProductInterface) {
        this.list = list;
        this.context = context;
        this.viewedProductInterface = viewedProductInterface;
        viewedProductDAO = new ViewedProductDAO(context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewedProductViewholder holder, @SuppressLint("RecyclerView") final int position) {
        final Product product = list.get(position);
        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        //
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        databaseReference.child(product.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product item = snapshot.getValue(Product.class);
                        if (item != null) {
                            holder.tvName.setText(item.getName());
                            if (item.getPrice() != product.getPrice()) {
                                holder.tvPrice.setText(String.valueOf(item.getPrice()));
                                product.setPrice(item.getPrice());
                                ViewedProductDAO viewedProductDAO = new ViewedProductDAO(context);
                                viewedProductDAO.update(product);
                            }
                            if (item.getImage() != null) {
                                Picasso.get().load(item.getImage()).into(holder.ivProduct);
                            }
                        } else {
                            viewedProductDAO.delete(product.getId());
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @NonNull
    @Override
    public ViewedProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewedProductViewholder(view);

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public interface ViewedProductInterface {
        void showDetail(Product product);
    }

    public class ViewedProductViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProduct;
        TextView tvName, tvPrice;

        public ViewedProductViewholder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            viewedProductInterface.showDetail(list.get(getAdapterPosition()));
        }

    }
}



