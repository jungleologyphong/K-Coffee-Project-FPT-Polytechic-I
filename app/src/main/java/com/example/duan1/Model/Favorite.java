package com.example.duan1.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Favorite implements Serializable {
    private String id;
    private String userId;
    private String productId;
    private String userId_productId;

    public Favorite() {
    }

    public Favorite(String userid, String productId) {
        this.userId = userid;
        this.productId = productId;
        this.userId_productId = userid + "_" + productId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId_productId() {
        return userId_productId;
    }

    public void setUserId_productId(String userId_productId) {
        this.userId_productId = userId_productId;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", userId_productId='" + userId_productId + '\'' +
                '}';
    }
}
