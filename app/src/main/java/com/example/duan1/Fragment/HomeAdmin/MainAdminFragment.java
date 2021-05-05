package com.example.duan1.Fragment.HomeAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.InvoiceAdminAdapter;
import com.example.duan1.Adapter.MenuAdapter;
import com.example.duan1.Model.Invoice;
import com.example.duan1.Model.Material.SquareImageView;
import com.example.duan1.Model.MenuItem;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainAdminFragment extends Fragment implements MenuAdapter.MenuInterface, InvoiceAdminAdapter.InvoiceAdminInterface {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    DecimalFormat df = new DecimalFormat("###,###");
    TextView tvFullName, tvPhoneNumber, tvRole, tvCount, tvTotal, tvOrder;
    SquareImageView sivSignOut;
    RecyclerView rcvMenu, rcvOrder;
    User user;
    DatabaseReference databaseReference;
    MenuAdapter menuAdapter;
    InvoiceAdminAdapter invoiceAdminAdapter;
    ArrayList<MenuItem> menuItems;
    ArrayList<Invoice> invoices;

    public MainAdminFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = HomeAdminActivity.user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_admin, container, false);

        tvFullName = view.findViewById(R.id.tvFullName);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvRole = view.findViewById(R.id.tvRole);
        tvCount = view.findViewById(R.id.tvCount);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvOrder = view.findViewById(R.id.tvOrder);
        rcvMenu = view.findViewById(R.id.rcvMenu);
        rcvOrder = view.findViewById(R.id.rcvOrder);
        sivSignOut = view.findViewById(R.id.sivSignOut);
        rcvMenu.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Sản phẩm", new ProductManageFragment(), R.drawable.ic_product));
        menuItems.add(new MenuItem("Hóa đơn", new InvoiceManageFragment(), R.drawable.ic_invoice));
        menuItems.add(new MenuItem("Khách hàng", new UserManageFragment(), R.drawable.ic_user));
        menuItems.add(new MenuItem("Thống kê", new StatisticFragment(), R.drawable.ic_statistic));
        menuAdapter = new MenuAdapter(menuItems, getContext(), this);

        invoices = new ArrayList<>();
        invoiceAdminAdapter = new InvoiceAdminAdapter(invoices, getContext(), this);

        rcvMenu.setAdapter(menuAdapter);
        rcvOrder.setAdapter(invoiceAdminAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFullName.setText(user.getFullName());
        tvPhoneNumber.setText(user.getPhoneNumber());
        tvRole.setText(user.getRole());

        getInvoiceCount();
        getProductTotal();

        getOrder();
        sivSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeAdminActivity) getActivity()).signOut();
            }
        });
    }

    private void getOrder() {
        databaseReference.child("Invoice").limitToLast(10).orderByChild("status").equalTo("ordered")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Invoice invoice = snapshot.getValue(Invoice.class);
                        invoice.setId(snapshot.getKey());
                        invoices.add(0, invoice);
                        invoiceAdminAdapter.notifyDataSetChanged();
                        if (invoices.size() == 0) {
                            tvOrder.setVisibility(View.GONE);
                        } else {
                            tvOrder.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Invoice invoice = snapshot.getValue(Invoice.class);
                        invoice.setId(snapshot.getKey());
                        for (int i = 0; i < invoices.size(); i++) {
                            if (invoice.getId().equalsIgnoreCase(invoices.get(i).getId())) {
                                invoices.remove(i);
                                invoiceAdminAdapter.notifyDataSetChanged();
                            }
                        }
                        if (invoices.size() == 0) {
                            tvOrder.setVisibility(View.GONE);
                        } else {
                            tvOrder.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getProductTotal() {
        databaseReference.child("Invoice").orderByChild("status").equalTo("delivered")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long total = 0;
                        for (DataSnapshot item : snapshot.getChildren()) {
                            Invoice invoice = item.getValue(Invoice.class);
                            if (invoice != null) {
                                invoice.setId(item.getKey());
                                total += invoice.getSubTotal();
                            }
                        }
                        tvTotal.setText(df.format(total));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getInvoiceCount() {
        databaseReference.child("Invoice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Quản lí");
        ((HomeAdminActivity) getActivity()).changeBackButton();
    }

    @Override
    public void select(int position) {
        ((HomeAdminActivity) getActivity()).switchFragment(menuItems.get(position).getFragment());
    }

    @Override
    public void showDetail(int position) {
        ((HomeAdminActivity) getActivity()).switchFragment(new InvoiceDetailAdminFragment(invoices.get(position).getId()));
    }

}