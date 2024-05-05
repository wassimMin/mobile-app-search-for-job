package com.example.yellow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(jobList);
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

        fetchJobs();
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
                                    String jobId = jobObject.getString("job_id");
                                    String jobName = jobObject.getString("jobname");
                                    String jobPosition = jobObject.getString("jobpos");
                                    String jobRequirements = jobObject.getString("jobreq");
                                    String jobSalaire = jobObject.getString("jobsal");

                                    Job job = new Job(jobId,jobName, jobPosition, jobRequirements,jobSalaire);
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
    public void onEditButtonClick(View view) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        int position = recyclerView.getChildLayoutPosition((View) view.getParent().getParent());
        Job jobToEdit = jobList.get(position);

        Intent intent = new Intent(this, editjob.class);
        intent.putExtra("job_id", jobToEdit.getId());
        intent.putExtra("job_name", jobToEdit.getJobName());
        intent.putExtra("job_position", jobToEdit.getJobPosition());
        intent.putExtra("job_requirements", jobToEdit.getJobRequirements());
        intent.putExtra("job_salaire", jobToEdit.getJobSalaire());
        startActivity(intent);
    }

    public void onDeleteButtonClick(View view) {
        int position = recyclerView.getChildAdapterPosition((View) view.getParent().getParent());
        if (position != RecyclerView.NO_POSITION) {
            Job jobToDelete = jobList.get(position);
            deleteJobFromDatabase(jobToDelete);
        }
    }

    private void deleteJobFromDatabase(Job job) {
        String jobId = job.getId();

        String url = "http://192.168.1.52/memoire/deletejob.php?job_id=" + jobId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server if needed
                        // For example, show a success message
                        Toast.makeText(Status.this, "Job deleted successfully", Toast.LENGTH_SHORT).show();
                        // Remove the job from the list and notify the adapter
                        jobList.remove(job);
                        jobAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Status.this, "Error deleting job: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
