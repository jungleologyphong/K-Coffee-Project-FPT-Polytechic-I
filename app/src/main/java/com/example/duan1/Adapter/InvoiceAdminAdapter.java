package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Model.Invoice;
import com.example.duan1.R;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class InvoiceAdminAdapter extends RecyclerView.Adapter<InvoiceAdminAdapter.InvoiceAdminViewHolder> implements Filterable {
    ArrayList<Invoice> invoiceList;
    ArrayList<Invoice> filteredList;
    Context context;
    CustomFilter customFilter;
    DecimalFormat df = new DecimalFormat("#,###,###");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    InvoiceAdminInterface invoiceAdminInterface;

    public InvoiceAdminAdapter(ArrayList<Invoice> invoiceList, Context context, InvoiceAdminInterface invoiceAdminInterface) {
        this.invoiceList = invoiceList;
        this.context = context;
        this.invoiceAdminInterface = invoiceAdminInterface;
        filteredList = invoiceList;
        customFilter = new CustomFilter(context);
    }

    @NonNull
    @Override
    public InvoiceAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAdminViewHolder holder, int position) {
        Invoice item = filteredList.get(position);
        //
        holder.tvFullName.setText(item.getFullName());
        holder.tvDate.setText(sdf.format(new Date(item.getCreatedAt())));
        holder.tvId.setText(item.getId());
        holder.tvSubTotal.setText(df.format(item.getSubTotal()));
        if (item.getStatus().equalsIgnoreCase("ordered")) {
            holder.tvSubTotal.setTextColor(context.getResources().getColor(R.color.Ordered));
        } else if (item.getStatus().equalsIgnoreCase("cancelled")) {
            holder.tvSubTotal.setTextColor(context.getResources().getColor(R.color.Cancelled));
        } else if (item.getStatus().equalsIgnoreCase("delivered")) {
            holder.tvSubTotal.setTextColor(context.getResources().getColor(R.color.Delivered));
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

    public interface InvoiceAdminInterface {
        void showDetail(int position);
    }

    public class CustomFilter extends Filter {

        Context context;

        public CustomFilter(Context context) {
            this.context = context;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Invoice> resultList = new ArrayList<>();
            FilterResults filterResults = new FilterResults();
            ArrayList<Invoice> newList = invoiceList;
            String searchValue = deAccent(constraint.toString().toLowerCase());
            for (int i = 0; i < newList.size(); i++) {
                Invoice currentItemFilter = newList.get(i);
                String title = deAccent(currentItemFilter.getId()
                        .concat(currentItemFilter.getFullName())
                        .concat(currentItemFilter.getStatus())
                        .concat(currentItemFilter.getAddress()));
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
            filteredList = (ArrayList<Invoice>) results.values;
            notifyDataSetChanged();
        }
    }

    public class InvoiceAdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvFullName, tvDate, tvId, tvSubTotal;

        public InvoiceAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvId = itemView.findViewById(R.id.tvId);
            tvSubTotal = itemView.findViewById(R.id.tvSubTotal);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            invoiceAdminInterface.showDetail(getAdapterPosition());
        }

    }
}
