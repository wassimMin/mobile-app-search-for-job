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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationAddJobService extends Service {

    private static final String TAG = "NotificationAddJobService";
    private static final String CHANNEL_ID = "JobNotifications";
    private static final String CHANNEL_NAME = "Job Notifications";
    private static final int NOTIFICATION_ID = 1;
    private RequestQueue requestQueue;
    private int jobsearcher_id;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("userid")) {
            jobsearcher_id = intent.getIntExtra("userid", -1);
            Log.e(TAG,"userid: "+ jobsearcher_id);
            if (jobsearcher_id != -1) {
                fetchJobNotifications();
            } else {
                Log.e(TAG, "Invalid jobsearcher_id received in intent");
            }
        } else {
            Log.e(TAG, "No jobsearcher_id found in intent");
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void fetchJobNotifications() {
        String url = "http://192.168.1.52/memoire/get_addjobnotification.php?jobsearcher_id=" + jobsearcher_id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int notificationId = jsonObject.getInt("id");
                                String message = jsonObject.getString("message");
                                showNotification(message, notificationId);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching notifications: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("MissingPermission")
    private void showNotification(String message, int notificationId) {

        createNotificationChannel();
        Intent intent = new Intent(this, Showjobs.class);
        intent.putExtra("notification_id", notificationId);
        intent.putExtra("userid",jobsearcher_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New Job Added")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "Notification channel created with ID: " + CHANNEL_ID);
        }
    }

}
