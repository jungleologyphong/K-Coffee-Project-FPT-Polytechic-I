package com.example.duan1.Fragment.HomeAdmin.User;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Adapter.UserAdapter;
import com.example.duan1.DAO.UserDAO;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BlockedUserFragment extends Fragment implements UserDAO.GetAllUserInterface {
    AutoCompleteTextView actvUser;
    RecyclerView rcvUser;
    ArrayList<User> users;
    UserAdapter userAdapter;
    DatabaseReference databaseReference;
    UserDAO userDAO;

    public BlockedUserFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        actvUser = view.findViewById(R.id.actvUser);
        rcvUser = view.findViewById(R.id.rcvUser);

        userDAO = new UserDAO(getContext(), this);

        rcvUser.setLayoutManager(new LinearLayoutManager(getContext()));

        users = new ArrayList<>();
        userAdapter = new UserAdapter(users, getContext());
        rcvUser.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUser();
        actvUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    actvUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    actvUser.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            userAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    actvUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
                }
            }
        });
    }

    private void loadUser() {
        userDAO.getAllByStatus("blocked");
    }

    @Override
    public void getAllUser(ArrayList<User> userList) {
        users.clear();
        users.addAll(userList);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeUser(User user) {
    }

    @Override
    public void removeUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(user.getId())) {
                users.remove(i);
                userAdapter.notifyDataSetChanged();
            }
        }
    }
}