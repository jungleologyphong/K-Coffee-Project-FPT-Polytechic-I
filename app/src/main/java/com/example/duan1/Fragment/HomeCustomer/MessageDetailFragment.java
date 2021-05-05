package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.MessageDetailAdapter;
import com.example.duan1.DAO.MessageDAO;
import com.example.duan1.LoginActivity;
import com.example.duan1.Model.Message;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class MessageDetailFragment extends Fragment implements MessageDAO.GetAllMessageDetailInterface {

    RecyclerView rcvMessage;
    MessageDetailAdapter messageDetailAdapter;
    ArrayList<Message> messages;

    ImageView ivSend;
    EditText edtMessage;

    MessageDAO messageDAO;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Message");
    LinearLayoutManager linearLayoutManager;
    User user;

    public MessageDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = LoginActivity.getSavedObjectFromPreference(getContext(), "User", "User", User.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_detail, container, false);

        rcvMessage = view.findViewById(R.id.rcvMessage);
        ivSend = view.findViewById(R.id.ivSend);
        edtMessage = view.findViewById(R.id.edtMessage);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rcvMessage.setLayoutManager(linearLayoutManager);
        messages = new ArrayList<>();
        messageDetailAdapter = new MessageDetailAdapter(messages, getContext(), "customer");
        rcvMessage.setAdapter(messageDetailAdapter);

        messageDAO = new MessageDAO(getContext(), this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageDAO.getAllMessageDetailCustomer(user.getId());
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = edtMessage.getText().toString();
                Date today = new Date();
                Long time = today.getTime();
                Message message = new Message(content, time, user.getRole(), user.getFullName());
                if (!content.isEmpty()) {
                    databaseReference.child(user.getId()).push().setValue(message);
                    edtMessage.setText(null);
                    linearLayoutManager.scrollToPosition(messages.size() - 1);
                }
            }
        });
    }

    @Override
    public void onResume() {
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Trung tâm trợ giúp");
        ((HomeUserActivity) getActivity()).changeBackButton();
        super.onResume();
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        messageDetailAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(messages.size() - 1);
    }

    @Override
    public void updateMessage(Message message) {
    }
}