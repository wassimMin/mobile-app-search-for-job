package com.example.yellow;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    private static final String CHANNEL_ID = "job_application_channel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int userId = intent.getIntExtra("user_id", -1);
        if (userId != -1) {
            Log.d(TAG, "Received user ID: " + userId);
            checkNotifications(userId);
        } else {
            Log.e(TAG, "Invalid user ID received");
        }
        return START_STICKY;
    }

    private void checkNotifications(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.1.52/memoire/notifications.php?user_id=" + userId;
        Log.d(TAG, "Notification URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                int notificationId = response.getJSONObject(0).getInt("id");
                                String notificationMessage = response.getJSONObject(0).getString("message");
                                Log.d(TAG, "Notification ID: " + notificationId);
                                Log.d(TAG, "Notification Message: " + notificationMessage);
                                showNotification(notificationId, notificationMessage);
                            } else {
                                Log.w(TAG, "Empty response received");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG, "Volley error: " + error.getMessage());
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }

    @SuppressLint("MissingPermission")
    private void showNotification(int notificationId, String message) {
        createNotificationChannel();

        Intent intent = new Intent(this, recieveapplication.class);
        intent.setAction("DELETE_NOTIFICATION");
        intent.putExtra("NOTIFICATION_ID", notificationId);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Job Application")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Job Application Channel";
            String description = "Channel for job application notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
