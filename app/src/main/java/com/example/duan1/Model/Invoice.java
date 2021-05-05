package com.example.duan1.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Invoice implements Serializable {
    private String id;
    private String userId;
    private long total;
    private long deliveryFee;
    private long serviceFee;
    private long discount;
    private long subTotal;
    private long createdAt;
    private String method;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String status;
    private String userId_status;

    public Invoice() {
    }

    public Invoice(String userId, long total, long deliveryFee, long serviceFee, long discount, long subTotal, long createdAt, String method, String fullName, String address, String phoneNumber, String status) {
        this.userId = userId;
        this.total = total;
        this.deliveryFee = deliveryFee;
        this.serviceFee = serviceFee;
        this.discount = discount;
        this.subTotal = subTotal;
        this.createdAt = createdAt;
        this.method = method;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userId_status = userId + "_" + status;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(long serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(long subTotal) {
        this.subTotal = subTotal;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId_status() {
        return userId_status;
    }

    public void setUserId_status(String userId_status) {
        this.userId_status = userId_status;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", total=" + total +
                ", deliveryFee=" + deliveryFee +
                ", serviceFee=" + serviceFee +
                ", discount=" + discount +
                ", subTotal=" + subTotal +
                ", createdAt=" + createdAt +
                ", method='" + method + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status='" + status + '\'' +
                ", userId_status='" + userId_status + '\'' +
                '}';
    }
}
