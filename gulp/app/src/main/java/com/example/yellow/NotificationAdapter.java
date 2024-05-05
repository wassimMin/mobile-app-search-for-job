package com.example.yellow;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<MyNotification> notificationList;
    private Context context;

    public NotificationAdapter(Context context, List<MyNotification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        MyNotification notification = notificationList.get(position);
        holder.notificationDescription.setText(notification.getDescription());
        holder.notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, notificationList.size());
                removeNotificationFromDatabase(parseInt(notification.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationDescription;
        Button notificationButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationDescription = itemView.findViewById(R.id.notificationDescription);
            notificationButton = itemView.findViewById(R.id.notificationButton);
        }
    }
    private void removeNotificationFromDatabase(int notificationId) {
        String url = "http://192.168.1.52/memoire/remove_notification.php?notification_id=" + notificationId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle success
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);
    }

}
