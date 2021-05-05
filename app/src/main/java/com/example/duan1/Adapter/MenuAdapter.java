package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Model.MenuItem;
import com.example.duan1.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    ArrayList<MenuItem> menuItems;
    Context context;
    MenuInterface menuInterface;

    public MenuAdapter(ArrayList<MenuItem> menuItems, Context context, MenuInterface menuInterface) {
        this.menuItems = menuItems;
        this.context = context;
        this.menuInterface = menuInterface;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);

        holder.ivIcon.setImageResource(menuItem.getDrawable());
        holder.tvLabel.setText(menuItem.getLabel());
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public interface MenuInterface {
        void select(int position);
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivIcon;
        TextView tvLabel;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            menuInterface.select(getAdapterPosition());
        }
    }
}
