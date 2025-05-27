package com.example.duan1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.DAO.CartDAO;
import com.example.duan1.Model.CartItem;
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
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    CartAdapterInterface cartAdapterInterface;
    CartDAO cartDAO;
    //
    DecimalFormat df = new DecimalFormat("###,###,###");
    private List<CartItem> list;

    public CartAdapter(List<CartItem> list, Context context, CartAdapterInterface cartAdapterInterface) {
        this.list = list;
        this.context = context;
        this.cartAdapterInterface = cartAdapterInterface;
        cartDAO = new CartDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final CartItem cart = list.get(position);
        if (list != null) {
            holder.txtPrice.setText(String.valueOf(cart.getPrice()));
            holder.txtNumber.setText(String.valueOf(cart.getQuantity()));

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");
            databaseReference.child(cart.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product item = snapshot.getValue(Product.class);
                    holder.txtName.setText(item.getName());
                    if (item.getPrice() != cart.getPrice()) {
                        holder.txtPrice.setText(df.format(item.getPrice()));
                        cart.setPrice(item.getPrice());
                        CartDAO cartDAO = new CartDAO(context);
                        cartDAO.update(cart);
                    }
                    if (!item.getImage().isEmpty()) {
                        Picasso.get().load(item.getImage()).into(holder.ivProduct);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getQuantity() < 99) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    holder.txtNumber.setText(String.valueOf(cart.getQuantity()));
                    cartDAO.update(cart);
                    cartAdapterInterface.update();
                }
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    holder.txtNumber.setText(String.valueOf(cart.getQuantity()));
                    cartDAO.update(cart);
                    cartAdapterInterface.update();
                }
            }
        });
        holder.sivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDAO.delete(cart);
                list.remove(position);
                notifyDataSetChanged();
                cartAdapterInterface.update();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface CartAdapterInterface {
        void update();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SquareImageView sivRemove;
        private ImageView ivProduct;
        private TextView txtName, txtPrice, txtNumber;
        private AppCompatButton btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sivRemove = itemView.findViewById(R.id.sivRemove);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            txtName = itemView.findViewById(R.id.name);
            txtPrice = itemView.findViewById(R.id.price);
            txtNumber = itemView.findViewById(R.id.number);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}
