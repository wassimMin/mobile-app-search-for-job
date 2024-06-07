package com.example.yellow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class recieveapplication extends AppCompatActivity {

    private static final String TAG = "recieveapplication";
    private RecyclerView recyclerView;
    private ApplicationAdapter applicationAdapter;
    private List<AplicationItem> applicationList;
    SharedPreferences sharedPreferences;
    int companyid;
    int notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieveapplication);

        recyclerView = findViewById(R.id.applicationitem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        applicationList = new ArrayList<>();
        applicationAdapter = new ApplicationAdapter(this, applicationList);
        recyclerView.setAdapter(applicationAdapter);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);
        companyid = sharedPreferences.getInt("userid", -1);

        // Log the initial intent
        if (getIntent() != null && getIntent().getAction() != null) {
            Log.d(TAG, "Initial Intent Action: " + getIntent().getAction());
            if ("DELETE_NOTIFICATION".equals(getIntent().getAction())) {
                notificationId = getIntent().getIntExtra("NOTIFICATION_ID", -1);
                Log.d(TAG, "Initial Notification ID: " + notificationId);
                if (notificationId != -1) {
                    Log.d(TAG, "Received notification ID: " + notificationId);
                    try {
                        deleteNotificationFromDatabase(String.valueOf(notificationId));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.d(TAG, "Notification ID is invalid");
                }
            }
        }

        fetchApplications();
    }

    private void deleteNotificationFromDatabase(String notificationId) throws JSONException {
        String url = "http://192.168.1.52/memoire/delete_notification.php?id="+ notificationId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", notificationId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Toast.makeText(recieveapplication.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(recieveapplication.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(recieveapplication.this, "Error deleting notification: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void fetchApplications() {
        String url = "http://192.168.1.52/memoire/get_application.php?userid="+ companyid;

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
                                    String appid = String.valueOf(jsonObject.getInt("id"));
                                    String userName = jsonObject.getString("user_name");
                                    String jobName = jsonObject.getString("job_name");
                                    byte[] cvPdfData = Base64.decode(jsonObject.getString("cv_pdf"), Base64.DEFAULT);
                                    String cvPdfDataBase64 = Base64.encodeToString(cvPdfData, Base64.DEFAULT);
                                    int userid = jsonObject.getInt("userid");
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("user_id", userid);
                                    editor.putInt("userid", companyid);
                                    editor.putString("cv_pdf", cvPdfDataBase64);
                                    editor.putString("username", userName);
                                    editor.putString("jobtitle", jobName);
                                    editor.putString("applicationid", appid);
                                    editor.apply();
                                    AplicationItem application = new AplicationItem(appid, userName, jobName, cvPdfData, String.valueOf(userid));
                                    applicationList.add(application);
                                }
                                applicationAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(recieveapplication.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Show message indicating no applications found
                            Toast.makeText(recieveapplication.this, "There are no applications added", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error message for debugging
                        Log.e("Volley Error", "Error fetching applications: " + error.getMessage());
                        Toast.makeText(recieveapplication.this, "Error fetching applications", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

}
