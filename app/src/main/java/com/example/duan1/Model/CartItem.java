package com.example.duan1.Model;

public class CartItem {
    private String id;
    private long price;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String id, long price, int quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
