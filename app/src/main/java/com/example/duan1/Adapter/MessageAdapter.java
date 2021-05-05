package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Model.User;
import com.example.duan1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    ArrayList<String> userIdList;
    Context context;
    MessageInterface messageInterface;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
    String fullName = "";

    public MessageAdapter(ArrayList<String> userIdList, Context context, MessageInterface messageInterface) {
        this.userIdList = userIdList;
        this.context = context;
        this.messageInterface = messageInterface;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String userId = userIdList.get(position);
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User item = snapshot.getValue(User.class);
                if (item != null) {
                    item.setId(snapshot.getKey());
                    fullName = item.getFullName();
                    holder.tvFullName.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvId.setText(userId);
    }

    @Override
    public int getItemCount() {
        return userIdList.size();
    }

    public interface MessageInterface {
        void showMessageDetail(String messageId, String fullName);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvFullName, tvId;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvId = itemView.findViewById(R.id.tvId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            messageInterface.showMessageDetail(userIdList.get(getAdapterPosition()), fullName);
        }
    }
}
