package com.example.yellow;

import android.os.Bundle;
import android.util.Base64;
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

public class recieveapplication extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApplicationAdapter applicationAdapter;
    private List<AplicationItem> applicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieveapplication);

        recyclerView = findViewById(R.id.applicationitem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        applicationList = new ArrayList<>();
        applicationAdapter = new ApplicationAdapter(this, applicationList);
        recyclerView.setAdapter(applicationAdapter);

        fetchApplications();
    }

    private void fetchApplications() {
        String url = "http://192.168.1.52/memoire/get_application.php";

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

                                    AplicationItem application = new AplicationItem(appid,userName, jobName, cvPdfData);
                                    applicationList.add(application);
                                }
                                applicationAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(recieveapplication.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(recieveapplication.this, "No applications found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(recieveapplication.this, "Error fetching applications: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }
}
