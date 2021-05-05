package com.example.duan1.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Model.CartItem;
import com.example.duan1.Model.Invoice;
import com.example.duan1.Model.InvoiceDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvoiceDAO {
    Context context;
    GetAllInvoiceInterface getAllInvoiceInterface;
    UpdateInvoiceInterface updateInvoiceInterface;
    ConfirmOrderInterface confirmOrderInterface;
    GetInvoiceInterface getInvoiceInterface;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoice");

    public InvoiceDAO(Context context) {
        this.context = context;
    }

    public InvoiceDAO(Context context, GetInvoiceInterface getInvoiceInterface) {
        this.context = context;
        this.getInvoiceInterface = getInvoiceInterface;
    }

    public InvoiceDAO(Context context, GetAllInvoiceInterface getAllInvoiceInterface) {
        this.context = context;
        this.getAllInvoiceInterface = getAllInvoiceInterface;
    }

    public InvoiceDAO(Context context, ConfirmOrderInterface confirmOrderInterface) {
        this.context = context;
        this.confirmOrderInterface = confirmOrderInterface;
    }

    public InvoiceDAO(Context context, UpdateInvoiceInterface updateInvoiceInterface, int num) {
        this.context = context;
        this.updateInvoiceInterface = updateInvoiceInterface;
    }

    public void getAll() {
        final ArrayList<Invoice> invoices = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoices.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Invoice invoice = item.getValue(Invoice.class);
                    if (invoice != null) {
                        invoice.setId(item.getKey());
                        invoices.add(0, invoice);
                    }
                }
                getAllInvoiceInterface.getAllInvoice(invoices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                if (invoice != null) {
                    invoice.setId(snapshot.getKey());
                    getAllInvoiceInterface.addInvoice(invoice);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                if (invoice != null) {
                    invoice.setId(snapshot.getKey());
                    getAllInvoiceInterface.changeInvoice(invoice);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                if (invoice != null) {
                    invoice.setId(snapshot.getKey());
                    getAllInvoiceInterface.removeInvoice(invoice);
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

    public void getAllByStatus(String status) {
        databaseReference.orderByChild("status").equalTo(status).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                if (invoice != null) {
                    invoice.setId(snapshot.getKey());
                    getAllInvoiceInterface.addInvoice(invoice);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                if (invoice != null) {
                    invoice.setId(snapshot.getKey());
                    getAllInvoiceInterface.changeInvoice(invoice);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                if (invoice != null) {
                    invoice.setId(snapshot.getKey());
                    getAllInvoiceInterface.removeInvoice(invoice);
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

    public void getAllByUserIdAndStatus(String userId, String status) {
        final ArrayList<Invoice> invoices = new ArrayList<>();
        databaseReference.orderByChild("userId_status").equalTo(userId + "_" + status)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            Invoice invoice = item.getValue(Invoice.class);
                            if (invoice != null) {
                                invoice.setId(item.getKey());
                                invoices.add(invoice);
                            }
                        }
                        getAllInvoiceInterface.getAllInvoice(invoices);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void update(final Invoice invoice, final String type) {
        databaseReference.child(invoice.getId()).setValue(invoice)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (type.equalsIgnoreCase("cancelled")) {
                            Toast.makeText(context, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        } else if (type.equalsIgnoreCase("ordered")) {
                            Toast.makeText(context, "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                        }
                        updateInvoiceInterface.updateinvoice();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Có lỗi xảy ra...", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void confirmOrder(Invoice invoice, final ArrayList<CartItem> cartItems) {
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("InvoiceDetail");
        final String invoiceId = databaseReference.push().getKey();
        databaseReference.child(invoiceId).setValue(invoice)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        for (CartItem item : cartItems) {
                            InvoiceDetail invoiceDetail = new InvoiceDetail(invoiceId, item.getId(), item.getQuantity());
                            databaseReference1.push().setValue(invoiceDetail);
                        }
                        confirmOrderInterface.orderSuccess();
                    }
                });

    }

    public void getInvoiceById(String id) {
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                if (invoice != null) {
                    invoice.setId(snapshot.getKey());
                    getInvoiceInterface.getSuccess(invoice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface GetAllInvoiceInterface {
        void getAllInvoice(ArrayList<Invoice> invoiceList);

        void addInvoice(Invoice invoice);

        void changeInvoice(Invoice invoice);

        void removeInvoice(Invoice invoice);

    }

    public interface UpdateInvoiceInterface {
        void updateinvoice();
    }

    public interface ConfirmOrderInterface {
        void orderSuccess();
    }

    public interface GetInvoiceInterface {
        void getSuccess(Invoice invoice);
    }
}
