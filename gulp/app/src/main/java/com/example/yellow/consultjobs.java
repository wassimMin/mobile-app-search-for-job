package com.example.yellow;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class consultjobs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobpostAdapter adapter;
    private List<JobPost> jobList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultjobs);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        adapter = new JobpostAdapter(this, jobList);
        recyclerView.setAdapter(adapter);
        fetchJobs();
    }

    private void fetchJobs() {
        String url = "http://192.168.1.52/memoire/fetch_jobs.php";

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
                                    JSONObject jobObject = response.getJSONObject(i);
                                    int job_id = jobObject.getInt("id");
                                    String job_title = jobObject.getString("job_title");
                                    String job_description = jobObject.getString("job_description");
                                    String company_name = jobObject.getString("company_name");
                                    String location = jobObject.getString("location");
                                    String job_category = jobObject.getString("job_category");
                                    String salary_range = jobObject.getString("salary_range");

                                    JobPost job = new JobPost(job_id,job_title,job_description,company_name,location,job_category,salary_range);
                                    jobList.add(job);
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(consultjobs.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(consultjobs.this, "No jobs found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(consultjobs.this, "Error fetching jobs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }





}
