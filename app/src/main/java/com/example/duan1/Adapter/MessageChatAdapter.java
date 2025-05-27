package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Activity.MessageChatActivity;
import com.example.duan1.Model.Message;
import com.example.duan1.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageChatAdapter extends RecyclerView.Adapter {

    List<Message> messageChatModelList;
    MessageChatActivity messageChatActivity;
    Context context;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    public static String ID = null;
    SimpleDateFormat simpleDateFormat;

    public MessageChatAdapter(List<Message> messageChatModelList, Context context, MessageChatActivity messageChatActivity) {
        this.messageChatModelList = messageChatModelList;
        this.context = context;
        this.messageChatActivity = messageChatActivity;
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messageChatModelList.get(position);
        if (message.getViewType_userId().equalsIgnoreCase("Customer")){
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layout, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_layout, parent, false);
            return new ReceivedMessageHolder(view);
        }else{
            return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Message message = messageChatModelList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageChatModelList.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView message;
        TextView time;
        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.message);

            itemView.setOnClickListener(this);
        }

        void bind(Message messageModel) {
            message.setText(messageModel.getText());
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyyy HH:mm:ss");
            time.setText(simpleDateFormat.format(new Date(messageModel.getTime())));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(messageChatActivity, "Click Sent", Toast.LENGTH_SHORT).show();
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView message;
        TextView time;
        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.message);

            itemView.setOnClickListener(this);
        }

        void bind(Message messageModel){
            message.setText(messageModel.getText());
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyyy HH:mm:ss");
            time.setText(simpleDateFormat.format(new Date(messageModel.getTime())));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(messageChatActivity, "Click Rent", Toast.LENGTH_SHORT).show();
        }
    }
}
