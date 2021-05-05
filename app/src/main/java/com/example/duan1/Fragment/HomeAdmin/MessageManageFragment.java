package com.example.duan1.Fragment.HomeAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.Adapter.MessageAdapter;
import com.example.duan1.DAO.MessageDAO;
import com.example.duan1.R;

import java.util.ArrayList;

public class MessageManageFragment extends Fragment implements MessageDAO.GetAllMessageInterface, MessageAdapter.MessageInterface {
    ArrayList<String> userIdList;
    MessageDAO messageDAO;
    MessageAdapter messageAdapter;
    RecyclerView rcvMessage;

    public MessageManageFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_manage, container, false);

        rcvMessage = view.findViewById(R.id.rcvMessage);
        rcvMessage.setLayoutManager(new LinearLayoutManager(getContext()));

        userIdList = new ArrayList<>();
        messageDAO = new MessageDAO(getContext(), this);
        messageAdapter = new MessageAdapter(userIdList, getContext(), this);

        rcvMessage.setAdapter(messageAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageDAO.getAllMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAdminActivity) getActivity()).currentMenu = R.menu.blank;
        getActivity().invalidateOptionsMenu();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Tin nhắn hỗ trợ");
        ((HomeAdminActivity) getActivity()).changeBackButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeAdminActivity) getActivity()).currentMenu = R.menu.message;
        getActivity().invalidateOptionsMenu();
    }


    @Override
    public void showMessageDetail(String userId, String fullName) {
        ((HomeAdminActivity) getActivity()).switchFragment(new MessageDetailAdminFragment(userId, fullName));
    }

    @Override
    public void addMessage(String userId) {
        userIdList.add(userId);
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateMessage(String userId) {

    }
}