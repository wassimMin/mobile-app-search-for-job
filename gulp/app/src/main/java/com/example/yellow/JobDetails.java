package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class JobDetails extends AppCompatActivity {
    private TextView jobTitle, jobCompanyName, jobSalary, jobExperience, jobLocation, jobEducation, jobEmployment, jobRequiredSkills;
    private Button editButton, deleteButton;
    String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        jobTitle = findViewById(R.id.job_title);
        jobCompanyName = findViewById(R.id.job_companyname);
        jobSalary = findViewById(R.id.job_salary);
        jobExperience = findViewById(R.id.job_experience);
        jobLocation = findViewById(R.id.job_location);
        jobEducation = findViewById(R.id.job_education);
        jobEmployment = findViewById(R.id.job_employment);
        jobRequiredSkills = findViewById(R.id.job_requiredskills);

        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);

        Intent intent = getIntent();
        String title = intent.getStringExtra("job_title");
        String companyName = intent.getStringExtra("job_companyname");
        String salary = intent.getStringExtra("job_salary");
        String experience = intent.getStringExtra("job_experience");
        String location = intent.getStringExtra("job_location");
        String education = intent.getStringExtra("job_education");
        String employment = intent.getStringExtra("job_employment");
        String requiredSkills = intent.getStringExtra("job_requiredskills");
        jobId = intent.getStringExtra("jobid");
        System.out.println("this is job id: "+jobId);
        jobTitle.setText(title);
        jobCompanyName.setText(companyName);
        jobSalary.setText(salary);
        jobExperience.setText(experience);
        jobLocation.setText(location);
        jobEducation.setText(education);
        jobEmployment.setText(employment);
        jobRequiredSkills.setText(requiredSkills);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(JobDetails.this,editjob.class);
                intent1.putExtra("jobid",jobId);
                intent1.putExtra("job_title", title);
                intent1.putExtra("job_companyname",companyName);
                intent1.putExtra("job_salary", salary);
                intent1.putExtra("job_experience", experience);
                intent1.putExtra("job_location", location);
                intent1.putExtra("job_education", education);
                intent1.putExtra("job_employment", employment);
                intent1.putExtra("job_requiredskills", requiredSkills);
                startActivity(intent1);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJob();
                Intent intent = new Intent(JobDetails.this,Status.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void deleteJob(){
        String url = "http://192.168.29.101/memoire/delete_job.php?job_id=" + jobId;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(JobDetails.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JobDetails.this, "Error deleting job", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}