package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.DAO.UserDAO;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewholder> implements Filterable {

    ArrayList<User> userList;
    ArrayList<User> filteredList;
    Context context;
    CustomFilter customFilter;
    DatabaseReference databaseReference;
    UserDAO userDAO;

    public UserAdapter(ArrayList<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
        filteredList = userList;
        customFilter = new CustomFilter(context);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userDAO = new UserDAO(context);

    }

    @NonNull
    @Override
    public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder, int position) {
        User user = filteredList.get(position);
        holder.tvFullName.setText(user.getFullName());
        holder.tvPhoneNumber.setText(user.getPhoneNumber());
        holder.tvEmail.setText(user.getEmail());
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Picasso.get().load(user.getImage()).into(holder.civUser);
        } else {
            Picasso.get().load(R.drawable.testlogo).into(holder.civUser);
        }


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
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

    @Override
    public Filter getFilter() {
        return customFilter;
    }

    public class UserViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvFullName, tvEmail, tvPhoneNumber;
        CircleImageView civUser;

        public UserViewholder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            civUser = itemView.findViewById(R.id.civUser);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final BottomSheetDialog bsd = new BottomSheetDialog(context, R.style.BottomSheetDialog);
            bsd.setContentView(R.layout.dialog_user_detail);

            CircleImageView civAvatar;
            final TextView tvId, tvFullName, tvEmail, tvPhoneNumber, tvAddress, tvInvoiceCount, tvCancelledInvoiceCount, tvStatus, tvDeliveredInvoiceCount;
            final Button btnBlock, btnUnBlock;
            tvId = bsd.findViewById(R.id.tvId);
            tvFullName = bsd.findViewById(R.id.tvFullName);
            tvEmail = bsd.findViewById(R.id.tvEmail);
            tvPhoneNumber = bsd.findViewById(R.id.tvPhoneNumber);
            tvAddress = bsd.findViewById(R.id.tvAddress);
            tvStatus = bsd.findViewById(R.id.tvStatus);
            civAvatar = bsd.findViewById(R.id.civAvatar);
            tvInvoiceCount = bsd.findViewById(R.id.tvInvoiceCount);
            tvCancelledInvoiceCount = bsd.findViewById(R.id.tvCancelledInvoiceCount);
            tvDeliveredInvoiceCount = bsd.findViewById(R.id.tvDeliveredInvoiceCount);
            btnBlock = bsd.findViewById(R.id.btnBlock);
            btnUnBlock = bsd.findViewById(R.id.btnUnBlock);

            final User user = userList.get(getAdapterPosition());
            tvId.setText(user.getId());
            tvFullName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
            tvPhoneNumber.setText(user.getPhoneNumber());
            tvAddress.setText(user.getAddress());
            if (user.getStatus().equalsIgnoreCase("active")) {
                tvStatus.setText("Bình thường");
                tvStatus.setTextColor(context.getResources().getColor(R.color.LimeGreen));
            } else {
                tvStatus.setText("Đã bị khóa");
                tvStatus.setTextColor(context.getResources().getColor(R.color.Red));
            }
            if (!user.getImage().isEmpty()) {
                Picasso.get().load(user.getImage()).into(civAvatar);
            } else {
                Picasso.get().load(R.drawable.testlogo).into(civAvatar);
            }
            if (user.getStatus().equalsIgnoreCase("blocked")) {
                btnUnBlock.setVisibility(View.VISIBLE);
                btnBlock.setVisibility(View.GONE);
            } else {
                btnUnBlock.setVisibility(View.GONE);
                btnBlock.setVisibility(View.VISIBLE);
            }

            databaseReference.child("Invoice").orderByChild("userId").equalTo(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvInvoiceCount.setText(String.valueOf(snapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.child("Invoice").orderByChild("userId_status").equalTo(user.getId() + "_cancelled").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvCancelledInvoiceCount.setText(String.valueOf(snapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.child("Invoice").orderByChild("userId_status").equalTo(user.getId() + "_delivered").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvDeliveredInvoiceCount.setText(String.valueOf(snapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btnBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnBlock.setText("Xác nhận");
                    btnBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.setStatus("blocked");
                            user.setRole_status(user.getRole().concat("_").concat(user.getStatus()));
                            userDAO.updateUser(user, user.getStatus());
                            bsd.dismiss();
                        }
                    });
                }
            });
            btnUnBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnUnBlock.setText("Xác nhận");
                    btnUnBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.setStatus("active");
                            user.setRole_status(user.getRole().concat("_").concat(user.getStatus()));
                            userDAO.updateUser(user, user.getStatus());
                            bsd.dismiss();
                        }
                    });
                }
            });

            bsd.show();
        }

    }

    public class CustomFilter extends Filter {

        Context context;

        public CustomFilter(Context context) {
            this.context = context;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> resultList = new ArrayList<>();
            FilterResults filterResults = new FilterResults();
            ArrayList<User> newList = userList;
            String searchValue = deAccent(constraint.toString().toLowerCase());
            for (int i = 0; i < newList.size(); i++) {
                User currentItemFilter = newList.get(i);
                String title = deAccent(currentItemFilter.getFullName()
                        .concat(currentItemFilter.getEmail())
                        .concat(currentItemFilter.getAddress())
                        .concat(currentItemFilter.getPhoneNumber())
                        .concat(currentItemFilter.getId()));
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
            filteredList = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }
}

