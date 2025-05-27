package com.example.duan1.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Model.Product;
import com.example.duan1.R;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ProductViewHolder> implements Filterable {
    ArrayList<Product> productList;
    ArrayList<Product> filteredList;
    Context context;
    CustomFilter customFilter;
    ProductSearchInterface productSearchInterface;

    public ProductSearchAdapter(ArrayList<Product> productList, Context context, ProductSearchInterface productSearchInterface) {
        this.productList = productList;
        this.context = context;
        this.productSearchInterface = productSearchInterface;
        filteredList = productList;
        customFilter = new CustomFilter(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_search, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = filteredList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.tvCode.setText(String.valueOf(product.getCode()));
        if (!product.getImage().isEmpty() && product.getImage() != null) {
            Picasso.get().load(product.getImage()).into(holder.ivProduct);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return customFilter;
    }

    public String deAccent(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "").replaceAll("đ", "d");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public interface ProductSearchInterface {
        void showDetail(Product product);
    }

    public class CustomFilter extends Filter {

        Context context;

        public CustomFilter(Context context) {
            this.context = context;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> resultList = new ArrayList<>();
            FilterResults filterResults = new FilterResults();
            ArrayList<Product> newList = productList;
            String searchValue = deAccent(constraint.toString().toLowerCase());
            Log.e("x", searchValue);
            for (int i = 0; i < newList.size(); i++) {
                Product currentItemFilter = newList.get(i);
                String title = deAccent(currentItemFilter.getName()
                        .concat(currentItemFilter.getCode()));
                if (title.toLowerCase().contains(searchValue)) {
                    resultList.add(currentItemFilter);
                }
            }
            filterResults.count = resultList.size();
            filterResults.values = resultList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProduct;
        TextView tvName;
        TextView tvCode;
        TextView tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            productSearchInterface.showDetail(productList.get(getAdapterPosition()));
        }

    }
}
