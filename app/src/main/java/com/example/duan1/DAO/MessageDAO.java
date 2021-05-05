package com.example.duan1.DAO;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Model.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageDAO {

    Context context;
    GetAllMessageInterface getAllMessageInterface;
    GetAllMessageDetailInterface getAllMessageDetailInterface;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Message");

    public MessageDAO(Context context, GetAllMessageInterface getAllMessageInterface) {
        this.context = context;
        this.getAllMessageInterface = getAllMessageInterface;
    }

    public MessageDAO(Context context, GetAllMessageDetailInterface getAllMessageDetailInterface) {
        this.context = context;
        this.getAllMessageDetailInterface = getAllMessageDetailInterface;
    }

    public void getAllMessage() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String userId = snapshot.getKey();
                getAllMessageInterface.addMessage(userId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String userId = snapshot.getKey();
                getAllMessageInterface.updateMessage(userId);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllMessageDetailCustomer(String userId) {
        databaseReference.child(userId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Message message = snapshot.getValue(Message.class);
                        if (message != null) {
                            message.setId(snapshot.getKey());
                            getAllMessageDetailInterface.addMessage(message);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public interface GetAllMessageInterface {
        void addMessage(String userId);

        void updateMessage(String userId);
    }

    public interface GetAllMessageDetailInterface {
        void addMessage(Message message);

        void updateMessage(Message message);
    }
}
