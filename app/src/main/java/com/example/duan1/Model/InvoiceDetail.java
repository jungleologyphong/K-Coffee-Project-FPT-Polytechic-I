package com.example.duan1.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class InvoiceDetail implements Serializable {
    private String id;
    private String invoiceId;
    private String productId;
    private int quantity;

    public InvoiceDetail() {
    }

    public InvoiceDetail(String invoiceId, String productId, int quantity) {
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "id='" + id + '\'' +
                ", invoiceId='" + invoiceId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}