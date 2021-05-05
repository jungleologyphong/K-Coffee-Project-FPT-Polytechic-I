package com.example.duan1.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String type;
    private String name;
    private String code;
    private long price;
    private String describe;
    private String image;

    public Product() {
    }

    public Product(String type, String name, String code, long price, String describe, String image) {
        this.type = type;
        this.name = name;
        this.code = code;
        this.price = price;
        this.describe = describe;
        this.image = image;
    }

    public Product(String type, String name, String code, long price, String describe) {
        this.type = type;
        this.name = name;
        this.code = code;
        this.price = price;
        this.describe = describe;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", price=" + price +
                ", describe='" + describe + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
