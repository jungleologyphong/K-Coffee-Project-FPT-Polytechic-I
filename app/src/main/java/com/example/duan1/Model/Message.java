package com.example.duan1.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Message implements Serializable {
    private String text;
    private Long time;
    private String viewType_userId;
    private String userId;
    private String fullName;
    private String id;


    public Message() {
    }


    public Message(String text, Long time, String viewType, String fullName) {
        this.text = text;
        this.time = time;
        this.viewType_userId = viewType;
        this.fullName = fullName;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getViewType_userId() {
        return viewType_userId;
    }

    public void setViewType_userId(String viewType_userId) {
        this.viewType_userId = viewType_userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullname) {
        this.fullName = fullname;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", time=" + time +
                ", viewType_userId='" + viewType_userId + '\'' +
                ", userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
