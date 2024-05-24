package com.example.yellow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

public class Status extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Job> jobList;
    private JobAdapter jobAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(this,jobList);
        recyclerView.setAdapter(jobAdapter);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        intent = getIntent();fetchJobs();
    }

    private void fetchJobs() {
        String url = "http://192.168.1.52/memoire/getsjob.php";

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
                                    String jobId = jobObject.getString("id");
                                    String jobName = jobObject.getString("job_title");
                                    String companyName = jobObject.getString("company_name");
                                    String employmenttype = jobObject.getString("employment_type");
                                    String location = jobObject.getString("location");
                                    String requiredskills = jobObject.getString("required_skills");
                                    String jobSalaire = jobObject.getString("salary_range");
                                    String experienceLevel = jobObject.getString("experience_level");
                                    String educationlevel = jobObject.getString("education_level");

                                    Job job = new Job(Integer.valueOf(jobId), jobName, companyName, employmenttype, location, requiredskills, experienceLevel, educationlevel, jobSalaire, "", 0);
                                    jobList.add(job);
                                }
                                jobAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Status.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Status.this, "No jobs found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Status.this, "Error fetching jobs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }
}
