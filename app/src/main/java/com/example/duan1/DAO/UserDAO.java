package com.example.duan1.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Model.User;
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

public class UserDAO {

    Context context;
    GetAllUserInterface getAllUserInterface;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");

    public UserDAO(Context context, GetAllUserInterface getAllUserInterface) {
        this.context = context;
        this.getAllUserInterface = getAllUserInterface;
    }

    public UserDAO(Context context) {
        this.context = context;
    }

    public void getALlUser() {
        final ArrayList<User> users = new ArrayList<>();
        databaseReference.orderByChild("role").equalTo("customer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            User user = item.getValue(User.class);
                            if (user != null) {
                                user.setId(item.getKey());
                                users.add(user);
                            }
                        }
                        getAllUserInterface.getAllUser(users);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference.orderByChild("role").equalTo("customer")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            user.setId(snapshot.getKey());
                            getAllUserInterface.changeUser(user);
                        }

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

    public void getAllByStatus(final String status) {
        databaseReference.orderByChild("role_status").equalTo("customer_" + status)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            user.setId(snapshot.getKey());
                            getAllUserInterface.addUser(user);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            user.setId(snapshot.getKey());
                            getAllUserInterface.removeUser(user);
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


    public void updateUser(final User user, final String type) {
        databaseReference.child(user.getId()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (type.equalsIgnoreCase("active")) {
                            Toast.makeText(context, "Mở khóa thành công", Toast.LENGTH_SHORT).show();
                        } else if (type.equalsIgnoreCase("blocked")) {
                            Toast.makeText(context, "Khóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Có lỗi xảy ra...", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public interface GetAllUserInterface {
        void getAllUser(ArrayList<User> userList);

        void addUser(User user);

        void changeUser(User user);

        void removeUser(User user);
    }
}
