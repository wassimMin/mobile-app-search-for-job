package com.example.yellow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yellow.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class editjob extends AppCompatActivity {

    TextInputEditText textInputEditTextJobName, textInputEditTextJobPosition, textInputEditTextJobRequirements, textInputEditTextJobSalaire;
    Button editButton;
    TextView textViewError;
    ProgressBar progressBar;
    String jobId,jobName,jobPosition,jobRequirements,jobSalaire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editjob);
        editButton = findViewById(R.id.edit);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Status.class);
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
             jobId = intent.getStringExtra("job_id");
             jobName = intent.getStringExtra("job_name");
             jobPosition = intent.getStringExtra("job_position");
             jobRequirements = intent.getStringExtra("job_requirements");
             jobSalaire = intent.getStringExtra("job_salaire");

            textInputEditTextJobName = findViewById(R.id.job_name);
            textInputEditTextJobPosition = findViewById(R.id.job_position);
            textInputEditTextJobRequirements = findViewById(R.id.job_requirements);
            textInputEditTextJobSalaire = findViewById(R.id.job_salaire);

            textInputEditTextJobName.setText(jobName);
            textInputEditTextJobPosition.setText(jobPosition);
            textInputEditTextJobRequirements.setText(jobRequirements);
            textInputEditTextJobSalaire.setText(jobSalaire);
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editJob();
            }
        });
    }

    private void editJob() {
        String jobName = textInputEditTextJobName.getText().toString().trim();
        String jobPosition = textInputEditTextJobPosition.getText().toString().trim();
        String jobRequirements = textInputEditTextJobRequirements.getText().toString().trim();
        String jobSalaire = textInputEditTextJobSalaire.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.52/memoire/editjob.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.equals("success")) {
                            Toast.makeText(editjob.this, "Job details updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            textViewError.setText(response);
                            textViewError.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                textViewError.setText(error.getMessage());
                textViewError.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("job_id", jobId);
                params.put("jobname", jobName);
                params.put("jobpos", jobPosition);
                params.put("jobreq", jobRequirements);
                params.put("jobsal", jobSalaire);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
