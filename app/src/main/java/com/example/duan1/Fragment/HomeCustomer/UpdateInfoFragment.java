package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.LoginActivity;
import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateInfoFragment extends Fragment {
    CheckBox cbChangePassword;
    LinearLayout llChangePassword;
    EditText edtFullName, edtPhoneNumber, edtAddress, edtEmail, edtOldPassword, edtNewPassword, edtReNewPassword;
    Button btnUpdate;
    User user;

    public UpdateInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        cbChangePassword = view.findViewById(R.id.cbChangePassword);
        llChangePassword = view.findViewById(R.id.llChangePassword);
        edtFullName = view.findViewById(R.id.edtFullName);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        edtReNewPassword = view.findViewById(R.id.edtReNewPassword);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUser();
        cbChangePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llChangePassword.setVisibility(View.VISIBLE);
                } else {
                    llChangePassword.setVisibility(View.GONE);
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (cbChangePassword.isChecked()) {
                        reAuthenticate(user.getEmail(), edtOldPassword.getText().toString(), edtNewPassword.getText().toString());
                    } else {
                        updateUser();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Cập nhật thông tin");
    }

    public void getUser() {
        user = HomeUserActivity.user;
        edtPhoneNumber.setText(user.getPhoneNumber());
        edtFullName.setText(user.getFullName());
        edtEmail.setText(user.getEmail());
        edtAddress.setText(user.getAddress());
    }


    private void reAuthenticate(String email, String oldPassword, final String newPassword) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPassword);
        final FirebaseUser userX = FirebaseAuth.getInstance().getCurrentUser();

        userX.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userX.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Đổi mật khẩu thành công, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                                        updateUser();
                                    } else {
                                        Toast.makeText(getContext(), "Có lỗi xảy ra...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUser() {
        String fullName = edtFullName.getText().toString();
        String address = edtAddress.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        user.setFullName(fullName);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        databaseReference.child(user.getId()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (cbChangePassword.isChecked()) {
                            Toast.makeText(getContext(), "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                            ((HomeUserActivity) getActivity()).signOut();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                            LoginActivity.saveObjectToSharedPreference(getContext(), "User", "User", user);
                            ((HomeUserActivity) getActivity()).back();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Có lỗi xảy ra...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean validate() {
        String fullName = edtFullName.getText().toString();
        String address = edtAddress.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String oldPassword = edtOldPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String reNewPassword = edtReNewPassword.getText().toString();
        if (fullName.trim().isEmpty() || address.trim().isEmpty() || phoneNumber.trim().isEmpty()) {
            Toast.makeText(getContext(), "Thông tin không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cbChangePassword.isChecked()) {
            if (oldPassword.trim().isEmpty() || newPassword.trim().isEmpty() || reNewPassword.trim().isEmpty()) {
                Toast.makeText(getContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return false;
            } else if (newPassword.length() < 6 || reNewPassword.length() < 6) {
                Toast.makeText(getContext(), "Mật khẩu mới phải từ 6 kí tự trở lên", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!newPassword.equals(reNewPassword)) {
                Toast.makeText(getContext(), "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //
        return true;
    }
}