package com.example.duan1.Adapter;

import android.content.Context;
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
import com.example.duan1.Model.Material.SquareImageView;
import com.example.duan1.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductAdminViewHolder> implements Filterable {

    ArrayList<Product> productList;
    ArrayList<Product> filteredList;
    Context context;
    DatabaseReference databaseReference;
    CustomFilter customFilter;
    DecimalFormat df = new DecimalFormat("###,###,###");
    ProductAdminInterface productAdminInterface;


    public ProductAdminAdapter(ArrayList<Product> productList, Context context, ProductAdminInterface productAdminInterface) {
        this.productList = productList;
        this.context = context;
        filteredList = productList;
        this.productAdminInterface = productAdminInterface;
        customFilter = new CustomFilter(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
    }

    @NonNull
    @Override
    public ProductAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ProductAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdminViewHolder holder, int position) {
        Product product = filteredList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvCode.setText(product.getCode());
        holder.tvPrice.setText(df.format(product.getPrice()));
        holder.tvDescribe.setText(product.getDescribe());
        if (product.getImage() != null) {
            Picasso.with(context).load(product.getImage()).into(holder.ivProduct);
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

    public interface ProductAdminInterface {
        void update(Product product);
    }

    public class ProductAdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivProduct;
        private final TextView tvName;
        private final TextView tvCode;
        private final TextView tvPrice;
        private final TextView tvDescribe;

        public ProductAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescribe = itemView.findViewById(R.id.tvDescribe);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            // Get current item in recyclerview;
            Product item = filteredList.get(getAdapterPosition());
            // Start drink detail activity
            showProductDetailDialog(item);
        }


        private void showActionDialog(final Product product) {
            final BottomSheetDialog bsdAction = new BottomSheetDialog(context);
            bsdAction.setContentView(R.layout.dialog_select_drink_action);
            //
            CircleImageView civDrink = bsdAction.findViewById(R.id.civDrink);
            TextView tvView = bsdAction.findViewById(R.id.tvView);
            TextView tvEdit = bsdAction.findViewById(R.id.tvEdit);
            TextView tvDrinkName = bsdAction.findViewById(R.id.tvDrinkName);
            //
            tvDrinkName.setText(product.getName());
            if (!product.getImage().isEmpty()) {
                Picasso.with(context).load(product.getImage()).into(civDrink);
            }
            //
            tvView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bsdAction.dismiss();
                    showProductDetailDialog(product);
                }
            });
            //
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bsdAction.dismiss();
                    productAdminInterface.update(product);
                }
            });

            bsdAction.show();
        }

        public void showProductDetailDialog(final Product product) {
            final BottomSheetDialog bsdProductDetail = new BottomSheetDialog(context);
            bsdProductDetail.setContentView(R.layout.dialog_product_detail);
            //
            SquareImageView sivProduct = bsdProductDetail.findViewById(R.id.sivProduct);
            SquareImageView sivEdit = bsdProductDetail.findViewById(R.id.sivEdit);
            TextView tvName = bsdProductDetail.findViewById(R.id.tvName);
            TextView tvCode = bsdProductDetail.findViewById(R.id.tvCode);
            TextView tvPrice = bsdProductDetail.findViewById(R.id.tvPrice);
            TextView tvDescribe = bsdProductDetail.findViewById(R.id.tvDescribe);
            //
            if (!product.getImage().isEmpty()) {
                Picasso.with(context).load(product.getImage()).into(sivProduct);
            }
            tvName.setText(product.getName());
            tvCode.setText(product.getCode());
            tvPrice.setText(String.valueOf(product.getPrice()));
            tvDescribe.setText(product.getDescribe());
            //
            sivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productAdminInterface.update(product);
                    bsdProductDetail.dismiss();
                }
            });
            //
            bsdProductDetail.show();

        }

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
            for (int i = 0; i < newList.size(); i++) {
                Product currentItemFilter = newList.get(i);
                String title = deAccent(currentItemFilter.getName()
                        .concat(currentItemFilter.getCode())
                        .concat(currentItemFilter.getDescribe()));
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
}


