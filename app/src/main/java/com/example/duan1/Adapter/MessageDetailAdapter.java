package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Model.Message;
import com.example.duan1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageDetailAdapter extends RecyclerView.Adapter<MessageDetailAdapter.MessageDetailViewHolder> {

    Context context;
    ArrayList<Message> messages;
    String role;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:ss:mm");

    public MessageDetailAdapter(ArrayList<Message> messages, Context context, String role) {
        this.context = context;
        this.messages = messages;
        this.role = role;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getViewType_userId().equalsIgnoreCase(role)) {
            return 1;
        } else {
            return 2;
        }
    }


    @NonNull
    @Override
    public MessageDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layout, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_layout, parent, false);
        }
        return new MessageDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageDetailViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvMessage.setText(message.getText());
        holder.tvTime.setText(sdf.format(message.getTime()));
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MessageDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;

        public MessageDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

}
