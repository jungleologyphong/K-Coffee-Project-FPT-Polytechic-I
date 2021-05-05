package com.example.duan1.DAO;

import androidx.annotation.NonNull;

import com.example.duan1.Model.Invoice;
import com.example.duan1.Model.InvoiceDetail;
import com.example.duan1.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatisticDAO {

    StatisticInterface statisticInterface;
    DatabaseReference invoiceReference = FirebaseDatabase.getInstance().getReference("Invoice");
    DatabaseReference invoiceDetailReference = FirebaseDatabase.getInstance().getReference("InvoiceDetail");
    DatabaseReference productReference = FirebaseDatabase.getInstance().getReference("Product");

    public StatisticDAO(StatisticInterface statisticInterface) {
        this.statisticInterface = statisticInterface;
    }

    public void getAllTotal(long start, long end) {
        // 0: all, 1: cancelled, 2: ordered, 3: delivered
        final long[] discount = {0, 0, 0, 0};
        final long[] total = {0, 0, 0, 0};
        final long[] subTotal = {0, 0, 0, 0};
        final long[] deliveryFee = {0, 0, 0, 0};
        final long[] serviceFee = {0, 0, 0, 0};
        final long[] invoiceCount = {0, 0, 0, 0};
        // 0: all, 1: drink, 2: food
        final long[] productCount = {0, 0, 0};
        final long[] orderedProductCount = {0, 0, 0};
        final long[] cancelledProductCount = {0, 0, 0};
        final long[] deliveredProductCount = {0, 0, 0};
        //
        final long[] allTotal = {0, 0, 0};
        final long[] cancelledTotal = {0, 0, 0};
        final long[] orderedTotal = {0, 0, 0};
        final long[] deliveredTotal = {0, 0, 0};
        statisticInterface.getAllTotal(total, subTotal, discount, deliveryFee, serviceFee, invoiceCount);
        statisticInterface.getProductCount(productCount, cancelledProductCount, orderedProductCount, deliveredProductCount);
        statisticInterface.getProductTotal(allTotal, cancelledTotal, orderedTotal, deliveredTotal);
        invoiceReference.orderByChild("createdAt").startAt(start).endAt(end)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            final Invoice invoice = item.getValue(Invoice.class);
                            if (invoice != null) {
                                invoice.setId(item.getKey());
                                discount[0] += invoice.getDiscount();
                                total[0] += invoice.getTotal();
                                subTotal[0] += invoice.getSubTotal();
                                deliveryFee[0] += invoice.getDeliveryFee();
                                serviceFee[0] += invoice.getServiceFee();
                                invoiceCount[0] += 1;
                                if (invoice.getStatus().equalsIgnoreCase("cancelled")) {
                                    discount[1] += invoice.getDiscount();
                                    total[1] += invoice.getTotal();
                                    subTotal[1] += invoice.getSubTotal();
                                    deliveryFee[1] += invoice.getDeliveryFee();
                                    serviceFee[1] += invoice.getServiceFee();
                                    invoiceCount[1] += 1;
                                } else if (invoice.getStatus().equalsIgnoreCase("ordered")) {
                                    discount[2] += invoice.getDiscount();
                                    total[2] += invoice.getTotal();
                                    subTotal[2] += invoice.getSubTotal();
                                    deliveryFee[2] += invoice.getDeliveryFee();
                                    serviceFee[2] += invoice.getServiceFee();
                                    invoiceCount[2] += 1;
                                } else if (invoice.getStatus().equalsIgnoreCase("delivered")) {
                                    discount[3] += invoice.getDiscount();
                                    total[3] += invoice.getTotal();
                                    subTotal[3] += invoice.getSubTotal();
                                    deliveryFee[3] += invoice.getDeliveryFee();
                                    serviceFee[3] += invoice.getServiceFee();
                                    invoiceCount[3] += 1;
                                }
                                //
                                invoiceDetailReference.orderByChild("invoiceId").equalTo(invoice.getId())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot item : snapshot.getChildren()) {
                                                    final InvoiceDetail invoiceDetail = item.getValue(InvoiceDetail.class);
                                                    if (invoiceDetail != null) {
                                                        invoiceDetail.setId(item.getKey());
                                                        //
                                                        productReference.child(invoiceDetail.getProductId())
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        Product product = snapshot.getValue(Product.class);
                                                                        if (product != null) {
                                                                            product.setId(snapshot.getKey());
                                                                            productCount[0] += invoiceDetail.getQuantity();
                                                                            allTotal[0] += invoiceDetail.getQuantity() * product.getPrice();
                                                                            //
                                                                            if (invoice.getStatus().equalsIgnoreCase("cancelled")) {
                                                                                cancelledProductCount[0] += invoiceDetail.getQuantity();
                                                                                cancelledTotal[0] += invoiceDetail.getQuantity() * product.getPrice();
                                                                            } else if (invoice.getStatus().equalsIgnoreCase("ordered")) {
                                                                                orderedProductCount[0] += invoiceDetail.getQuantity();
                                                                                orderedTotal[0] += invoiceDetail.getQuantity() * product.getPrice();
                                                                            } else if (invoice.getStatus().equalsIgnoreCase("delivered")) {
                                                                                deliveredProductCount[0] += invoiceDetail.getQuantity();
                                                                                deliveredTotal[0] += invoiceDetail.getQuantity() * product.getPrice();
                                                                            }
                                                                            //
                                                                            if (product.getType().equalsIgnoreCase("drink")) {
                                                                                productCount[1] += invoiceDetail.getQuantity();
                                                                                allTotal[1] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                //
                                                                                if (invoice.getStatus().equalsIgnoreCase("cancelled")) {
                                                                                    cancelledProductCount[1] += invoiceDetail.getQuantity();
                                                                                    cancelledTotal[1] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                } else if (invoice.getStatus().equalsIgnoreCase("ordered")) {
                                                                                    orderedProductCount[1] += invoiceDetail.getQuantity();
                                                                                    orderedTotal[1] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                } else if (invoice.getStatus().equalsIgnoreCase("delivered")) {
                                                                                    deliveredProductCount[1] += invoiceDetail.getQuantity();
                                                                                    deliveredTotal[1] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                }
                                                                            } else if (product.getType().equalsIgnoreCase("food")) {
                                                                                productCount[2] += invoiceDetail.getQuantity();
                                                                                allTotal[2] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                //
                                                                                if (invoice.getStatus().equalsIgnoreCase("cancelled")) {
                                                                                    cancelledProductCount[2] += invoiceDetail.getQuantity();
                                                                                    cancelledTotal[2] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                } else if (invoice.getStatus().equalsIgnoreCase("ordered")) {
                                                                                    orderedProductCount[2] += invoiceDetail.getQuantity();
                                                                                    orderedTotal[2] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                } else if (invoice.getStatus().equalsIgnoreCase("delivered")) {
                                                                                    deliveredProductCount[2] += invoiceDetail.getQuantity();
                                                                                    deliveredTotal[2] += invoiceDetail.getQuantity() * product.getPrice();
                                                                                }
                                                                            }
                                                                        }
                                                                        statisticInterface.getProductCount(productCount, cancelledProductCount, orderedProductCount, deliveredProductCount);
                                                                        statisticInterface.getProductTotal(allTotal, cancelledTotal, orderedTotal, deliveredTotal);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                    }
                                                }
                                                statisticInterface.getProductCount(productCount, cancelledProductCount, orderedProductCount, deliveredProductCount);
                                                statisticInterface.getProductTotal(allTotal, cancelledTotal, orderedTotal, deliveredTotal);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        }
                        statisticInterface.getAllTotal(total, subTotal, discount, deliveryFee, serviceFee, invoiceCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public interface StatisticInterface {
        void getAllTotal(long[] total, long[] subTotal, long[] discount, long[] deliveryFee, long[] serviceFee, long[] invoiceCount);

        void getProductCount(long[] productCount, long[] cancelledProductCount, long[] orderedProductCount, long[] deliveredProductCount);

        void getProductTotal(long[] allTotal, long[] cancelledTotal, long[] orderedTotal, long[] deliveredTotal);
    }
}
