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

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> implements Filterable {
    ArrayList<Invoice> invoiceList;
    ArrayList<Invoice> filteredList;
    Context context;
    CustomFilter customFilter;
    DecimalFormat df = new DecimalFormat("#,###,###");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    InvoiceInterface invoiceInterface;

    public InvoiceAdapter(ArrayList<Invoice> invoiceList, Context context, InvoiceInterface invoiceInterface) {
        this.invoiceList = invoiceList;
        this.context = context;
        this.invoiceInterface = invoiceInterface;
        filteredList = invoiceList;
        customFilter = new CustomFilter(context);
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
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

    public interface InvoiceInterface {
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

    public class InvoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvFullName, tvDate, tvId, tvSubTotal;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvId = itemView.findViewById(R.id.tvId);
            tvSubTotal = itemView.findViewById(R.id.tvSubTotal);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            invoiceInterface.showDetail(getAdapterPosition());
        }

    }
}
