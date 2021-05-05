package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonFragment extends Fragment {
    CircleImageView civAvatar;
    TextView tvFullName, tvPhoneNumber, tvEmail;
    LinearLayout llUser, llInviteFriends, llSettings, llPaymentMethod, llSupportCenter;
    Button btnSignOut;

    public PersonFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person2, container, false);

        civAvatar = view.findViewById(R.id.civAvatar);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmail);
        llUser = view.findViewById(R.id.llUser);
        llInviteFriends = view.findViewById(R.id.llInviteFriends);
        llSettings = view.findViewById(R.id.llSettings);
        llPaymentMethod = view.findViewById(R.id.llPaymentMethod);
        llSupportCenter = view.findViewById(R.id.llSupportCenter);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeUserActivity) getActivity()).switchFragment(new UpdateInfoFragment());
            }
        });
        llInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tính năng chưa được hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        });
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tính năng chưa được hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        });
        llPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tính năng chưa được hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        });
        llSupportCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeUserActivity) getActivity()).switchFragment(new MessageDetailFragment());
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeUserActivity) getActivity()).signOut();
            }
        });
    }

    private void getUser() {
        User user = HomeUserActivity.user;
        tvPhoneNumber.setText(user.getPhoneNumber());
        tvFullName.setText(user.getFullName());
        tvEmail.setText(user.getEmail());
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Picasso.with(getContext()).load(user.getImage()).into(civAvatar);
        } else {
            Picasso.with(getContext()).load(R.drawable.testlogo).into(civAvatar);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Cá nhân");
    }

}