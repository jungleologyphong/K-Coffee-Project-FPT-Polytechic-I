package com.example.duan1.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Adapter.MessageChatAdapter;
import com.example.duan1.LoginActivity;
import com.example.duan1.Model.Message;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MessageChatActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<Message> listMessageChat;
    EditText messageET;
    TextView txtFullName;
    ImageView btnSend;
    RecyclerView recyclerMessageChat;
    Message messageChatModel;
    User user;
    String id,name;
    MessageChatAdapter messageChatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerMessageChat = findViewById(R.id.recycler_view);
        messageET = findViewById(R.id.messageET);
        btnSend = findViewById(R.id.btnSendMessage);
        txtFullName = findViewById(R.id.txtFullName);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = new User();
        messageChatModel = new Message();
        listMessageChat = new ArrayList<>();
        user = LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "User", "User", User.class);

        txtFullName.setText("Trung tâm trợ giúp".toUpperCase());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getRole().equalsIgnoreCase("Customer")){
                    String message = messageET.getText().toString();
                    Date today = new Date();
                    Long time = today.getTime();
                    messageChatModel = new Message(message,time,"customer",user.getFullName());
                    databaseReference.child("Message").child(user.getId()).push().setValue(messageChatModel);
                    messageET.setText(null);
                }else{
                    Bundle extras = getIntent().getExtras();
                    id = extras.getString("id");
                    String message = messageET.getText().toString();
                    Date today = new Date();
                    Long time = today.getTime();
                    messageChatModel = new Message(message,time,"admin",user.getFullName());
                    databaseReference.child("Message").child(id).push().setValue(messageChatModel);
                    messageET.setText(null);
                }
            }
        });

    }

    public void loadRecycylerMessageInCustomer(){
        listMessageChat.clear();
        databaseReference.child("Message").child(user.getId()).orderByChild("viewType_userId").equalTo("customer").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message messageChatModel = snapshot.getValue(Message.class);
                messageChatModel.setId(snapshot.getKey());
                listMessageChat.add(messageChatModel);
                Collections.reverse(listMessageChat);
                recyclerMessageChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                MessageChatAdapter messageChatAdapter = new MessageChatAdapter(listMessageChat,getApplicationContext(), MessageChatActivity.this);
                recyclerMessageChat.setHasFixedSize(true);
                recyclerMessageChat.setAdapter(messageChatAdapter);
                messageChatAdapter.notifyDataSetChanged();
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
        databaseReference.child("Message").child(user.getId()).orderByChild("viewType_userId").equalTo("admin").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message messageChatModel = snapshot.getValue(Message.class);
                messageChatModel.setId(snapshot.getKey());
                listMessageChat.add(messageChatModel);
                Collections.reverse(listMessageChat);
                recyclerMessageChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                MessageChatAdapter messageChatAdapter = new MessageChatAdapter(listMessageChat,getApplicationContext(), MessageChatActivity.this);
                recyclerMessageChat.setHasFixedSize(true);
                recyclerMessageChat.setAdapter(messageChatAdapter);
                messageChatAdapter.notifyDataSetChanged();
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

    public void loadRecyclerMessageInAdmin(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");
            listMessageChat.clear();
            databaseReference.child("Message").child(id).orderByChild("viewType_userId").equalTo("admin").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Message messageChatModel = snapshot.getValue(Message.class);
                    messageChatModel.setId(snapshot.getKey());
                    listMessageChat.add(messageChatModel);
                    recyclerMessageChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    MessageChatAdapter messageChatAdapter = new MessageChatAdapter(listMessageChat,getApplicationContext(), MessageChatActivity.this);
                    recyclerMessageChat.setHasFixedSize(true);
                    recyclerMessageChat.setAdapter(messageChatAdapter);
                    messageChatAdapter.notifyDataSetChanged();
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
            databaseReference.child("Message").child(id).orderByChild("viewType_userId").equalTo("customer").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Message messageChatModel = snapshot.getValue(Message.class);
                    messageChatModel.setId(snapshot.getKey());
                    listMessageChat.add(messageChatModel);
                    recyclerMessageChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    MessageChatAdapter messageChatAdapter = new MessageChatAdapter(listMessageChat,getApplicationContext(), MessageChatActivity.this);
                    recyclerMessageChat.setHasFixedSize(true);
                    recyclerMessageChat.setAdapter(messageChatAdapter);
                    messageChatAdapter.notifyDataSetChanged();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecyclerMessageInAdmin();
        loadRecycylerMessageInCustomer();
    }
}
