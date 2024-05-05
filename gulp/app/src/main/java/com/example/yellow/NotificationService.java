package com.example.yellow;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationService extends Service {

    private static final String SERVER_URL = "http://192.168.215.101/memoire/get_notifications.php";
    private static final String CHANNEL_ID = "job_notification_channel";
    private int notificationId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkForNotifications();
        return START_STICKY;
    }

    private void checkForNotifications() {
        // Retrieve user_id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userid", 0);

        // Asynchronously make HTTP request to server to check for notifications
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return makeHttpRequest(SERVER_URL);
            }

            @Override
            protected void onPostExecute(String response) {
                // Handle response here
                if (response != null) {
                    // Parse the response JSON and filter notifications by user_id
                    // Assuming the JSON response has a "user_id" field for each notification
                    // You may need to adjust this based on the actual structure of your JSON response
                    try {
                        // Parse the JSON array
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int notificationUserId = jsonObject.getInt("user_id");
                            if (notificationUserId == userId) {
                                // Customize the notification message
                                String notificationMessage = "New Job added";

                                // Show a toast with the customized message
                                Toast.makeText(NotificationService.this, notificationMessage, Toast.LENGTH_SHORT).show();

                                // Create a system notification
                                createNotification(notificationMessage);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private void createNotification(String message) {
        // Create a notification channel if necessary (for Android Oreo and above)
        createNotificationChannel();
        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setContentTitle("New Offer , Check Your Noti Box :) !");
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Job Notification Channel";
            String description = "Channel for job notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private String makeHttpRequest(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            conn.disconnect();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
