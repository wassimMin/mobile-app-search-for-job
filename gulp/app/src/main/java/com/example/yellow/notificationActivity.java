package com.example.yellow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class notificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<MyNotification> notificationList;
    private SharedPreferences sharedPreferences;
    private int userlogging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(notificationAdapter);
        sharedPreferences = getSharedPreferences("yellow",MODE_PRIVATE);
        userlogging = sharedPreferences.getInt("userid",0);
        fetchNotifications();
    }

    private void fetchNotifications() {
        String url = "http://192.168.1.52/memoire/get_notifications.php?user_id=" + userlogging;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null && response.length() > 0) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    int noti_id = jsonObject.getInt("notification_id");
                                    int user_id = jsonObject.getInt("user_id");
                                    String message = jsonObject.getString("message");

                                    MyNotification notification = new MyNotification(String.valueOf(noti_id), String.valueOf(user_id), message);
                                    notificationList.add(notification);
                                }
                                notificationAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(notificationActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(notificationActivity.this, "No notifications found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(notificationActivity.this, "Error fetching notifications: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }
}
