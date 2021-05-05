package com.example.duan1.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String role;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String image;
    private String status;
    private String role_status;

    public User() {
    }

    public User(String role, String email, String fullName, String phoneNumber, String address, String image, String status) {
        this.role = role;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.image = image;
        this.status = status;
        this.role_status = role + "_" + status;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole_status() {
        return role_status;
    }

    public void setRole_status(String role_status) {
        this.role_status = role_status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", role_status='" + role_status + '\'' +
                '}';
    }
}
