package com.example.yellow;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.textViewMessageContent.setText(message.getContent());

        String userType = message.getUserType();
        if (userType != null && userType.equals("0")) {
            holder.textViewMessageContent.setBackgroundColor(getColor(R.color.user_message_background, holder.itemView));
            holder.textViewMessageType.setText("User");
        } else {
            holder.textViewMessageContent.setBackgroundColor(getColor(R.color.company_message_background, holder.itemView));
            holder.textViewMessageType.setText("Company");
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessageContent;
        TextView textViewMessageType;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessageContent = itemView.findViewById(R.id.text_view_message_content);
            textViewMessageType = itemView.findViewById(R.id.text_view_message_type);
        }
    }

    private int getColor(int colorResId, View itemView) {
        Context context = itemView.getContext();
        return ContextCompat.getColor(context, colorResId);
    }
}
