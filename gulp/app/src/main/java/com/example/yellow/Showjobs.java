package com.example.yellow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Showjobs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobUserAdapter jobUserAdapter;
    private List<Job> jobList;
    ImageButton btnback;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showjobs2);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnback = findViewById(R.id.back_button);
        jobList = new ArrayList<>();
        jobUserAdapter = new JobUserAdapter(this, jobList);
        recyclerView.setAdapter(jobUserAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userid", 0);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Showjobs.this,Homeuser.class);
                startActivity(intent);
            }
        });
        fetchJobs();
    }

    private void fetchJobs() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.52/memoire/fetch_jobss.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            jobList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobObject = jsonArray.getJSONObject(i);
                                Job job = new Job(
                                        jobObject.getInt("id"),
                                        jobObject.getString("job_title"),
                                        jobObject.getString("company_name"),
                                        jobObject.getString("employment_type"),
                                        jobObject.getString("location"),
                                        jobObject.getString("required_skills"),
                                        jobObject.getString("experience_level"),
                                        jobObject.getString("education_level"),
                                        jobObject.getString("salary_range"),
                                        jobObject.getString("created_at"),
                                        jobObject.getInt("userid")
                                );
                                jobList.add(job);
                            }
                            jobUserAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Showjobs.this, "Error parsing job data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Showjobs.this, "Error fetching jobs", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", String.valueOf(userId));
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
