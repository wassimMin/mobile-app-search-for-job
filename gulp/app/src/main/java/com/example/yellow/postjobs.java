package com.example.yellow;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class postjobs extends AppCompatActivity {
    TextInputEditText jobtitletext, jobdescriptiontext, companynametext, locationtext, salaryrange;
    Spinner jobcategoryspinner;
    Button addjob;
    private static final String TAG = "postjobs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postjobs);

        jobtitletext = findViewById(R.id.job_name_input);
        jobdescriptiontext = findViewById(R.id.job_description_input);
        companynametext = findViewById(R.id.company_name_input);
        locationtext = findViewById(R.id.location_input);
        jobcategoryspinner = findViewById(R.id.job_category_spinner);
        salaryrange = findViewById(R.id.salary_range_input);
        addjob = findViewById(R.id.add_job_button);

        String[] jobCategories = getResources().getStringArray(R.array.job_categories_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, jobCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobcategoryspinner.setAdapter(adapter);

        addjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobTitle = jobtitletext.getText().toString();
                String jobDescription = jobdescriptiontext.getText().toString();
                String companyName = companynametext.getText().toString();
                String location = locationtext.getText().toString();
                String jobCategory = jobcategoryspinner.getSelectedItem().toString();
                String salaryRange = salaryrange.getText().toString();
                postJob(jobTitle, jobDescription, companyName, location, jobCategory, salaryRange);
            }
        });
    }

    private void postJob(final String jobTitle, final String jobDescription, final String companyName,
                         final String location, final String jobCategory, final String salaryRange) {

        String url = "http://192.168.1.52/memoire/postjob.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        if ("New record created successfully".equals(response)) {
                            Toast.makeText(postjobs.this, "Job posted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(postjobs.this, "Failed to post job", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error here
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(postjobs.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("job_title", jobTitle);
                params.put("job_description", jobDescription);
                params.put("company_name", companyName);
                params.put("location", location);
                params.put("job_category", jobCategory);
                params.put("salary_range", salaryRange);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
