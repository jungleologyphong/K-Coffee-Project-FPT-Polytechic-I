package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.DAO.FavoriteDAO;
import com.example.duan1.Model.Favorite;
import com.example.duan1.Model.Product;
import com.example.duan1.Model.Material.SquareImageView;
import com.example.duan1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements FavoriteDAO.RemoveFavoriteInterface {
    Context context;
    ArrayList<Favorite> favoriteList;
    FavoriteDAO favoriteDAO;
    DecimalFormat df = new DecimalFormat("###,###,###");

    public FavoriteAdapter(Context context, ArrayList<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
        favoriteDAO = new FavoriteDAO(context, this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Favorite favorite = favoriteList.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        databaseReference.child(favorite.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    product.setId(snapshot.getKey());
                    if (!product.getImage().isEmpty()) {
                        Picasso.with(context).load(product.getImage()).into(holder.ivProduct);
                    }
                    holder.tvName.setText(product.getName());
                    holder.tvPrice.setText(df.format(product.getPrice()));
                    holder.tvCode.setText(product.getCode());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    @Override
    public void removeFavorite(int position) {
        favoriteList.remove(position);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SquareImageView sivRemove;
        ImageView ivProduct;
        TextView tvName, tvCode, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivProduct = itemView.findViewById(R.id.ivProduct);
            this.sivRemove = itemView.findViewById(R.id.sivRemove);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvCode = itemView.findViewById(R.id.tvCode);
            this.tvPrice = itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(this);
            sivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favoriteDAO.removeFavorite(getAdapterPosition(), favoriteList.get(getAdapterPosition()).getId());
                }
            });

        }

        @Override
        public void onClick(View v) {
        }
    }
}
