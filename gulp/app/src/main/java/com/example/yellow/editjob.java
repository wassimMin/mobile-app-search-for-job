package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import java.util.HashMap;
import java.util.Map;

public class editjob extends AppCompatActivity {
    private TextInputEditText jobTitle, companyName, location, requiredSkills, salaryRange;
    private Spinner employmentType, experienceType, educationType;
    private Button submitButton;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editjob);

        jobTitle = findViewById(R.id.jobtitle);
        companyName = findViewById(R.id.companyname);
        location = findViewById(R.id.locationtext);
        requiredSkills = findViewById(R.id.requiredskills);
        salaryRange = findViewById(R.id.salary_range_input);
        employmentType = findViewById(R.id.employment_type_spinner);
        experienceType = findViewById(R.id.experience_type_spinner);
        educationType = findViewById(R.id.education_type_spinner);
        submitButton = findViewById(R.id.submit);

        Intent intent = getIntent();
        jobId = intent.getStringExtra("jobid");
        jobTitle.setText(intent.getStringExtra("job_title"));
        companyName.setText(intent.getStringExtra("job_companyname"));
        location.setText(intent.getStringExtra("job_location"));
        requiredSkills.setText(intent.getStringExtra("job_requiredskills"));
        salaryRange.setText(intent.getStringExtra("job_salary"));

        setSpinnerSelection(employmentType, intent.getStringExtra("job_employment"), R.array.employment_types);
        setSpinnerSelection(experienceType, intent.getStringExtra("job_experience"), R.array.experience_types);
        setSpinnerSelection(educationType, intent.getStringExtra("job_education"), R.array.education_types);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJob();
            }
        });
    }

    private void setSpinnerSelection(Spinner spinner, String value, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (value != null) {
            int spinnerPosition = adapter.getPosition(value);
            spinner.setSelection(spinnerPosition);
        }
    }

    private void updateJob() {
        final String title = jobTitle.getText().toString().trim();
        final String company = companyName.getText().toString().trim();
        final String jobLocation = location.getText().toString().trim();
        final String skills = requiredSkills.getText().toString().trim();
        final String salary = salaryRange.getText().toString().trim();
        final String employment = employmentType.getSelectedItem().toString();
        final String experience = experienceType.getSelectedItem().toString();
        final String education = educationType.getSelectedItem().toString();

        String url = "http://192.168.1.52/memoire/editjob.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Intent intent = new Intent(editjob.this, Status.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(editjob.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(editjob.this, "Error updating job", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", jobId);
                params.put("job_title", title);
                params.put("job_companyname", company);
                params.put("job_location", jobLocation);
                params.put("job_requiredskills", skills);
                params.put("job_salary", salary);
                params.put("job_employment", employment);
                params.put("job_experience", experience);
                params.put("job_education", education);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
