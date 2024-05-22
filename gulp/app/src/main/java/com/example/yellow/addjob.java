package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class addjob extends AppCompatActivity {
    TextInputEditText textInputEditTextJobTitle, textInputEditTextCompanyName, textInputEditTextLocation, textInputEditTextSalaryRange;
    Spinner employmentTypeSpinner, categoryTypeSpinner, educationTypeSpinner, experienceLevelSpinner, educationLevelSpinner;
    TextInputEditText requiredSkillsTextView;
    Button buttonSubmit;
    String jobTitle, companyName, employmentType, location, requiredSkills, experienceLevel, educationLevel, salaryRange;
    TextView textViewError;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjob);
        intent = getIntent();
        int userId = intent.getIntExtra("userid",0);
        textInputEditTextJobTitle = findViewById(R.id.jobtitle);
        textInputEditTextCompanyName = findViewById(R.id.companyname);
        textInputEditTextLocation = findViewById(R.id.locationtext);
        textInputEditTextSalaryRange = findViewById(R.id.salary_range_input);

        employmentTypeSpinner = findViewById(R.id.employment_type_spinner);
        educationTypeSpinner = findViewById(R.id.education_type_spinner);
        experienceLevelSpinner = findViewById(R.id.experience_type_spinner);

        requiredSkillsTextView = findViewById(R.id.requiredskills);

        buttonSubmit = findViewById(R.id.submit);
        textViewError = findViewById(R.id.error);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewError.setVisibility(View.GONE);

                jobTitle = textInputEditTextJobTitle.getText().toString();
                companyName = textInputEditTextCompanyName.getText().toString();
                location = textInputEditTextLocation.getText().toString();
                salaryRange = textInputEditTextSalaryRange.getText().toString();
                requiredSkills = requiredSkillsTextView.getText().toString();

                employmentType = employmentTypeSpinner.getSelectedItem().toString();
                experienceLevel = experienceLevelSpinner.getSelectedItem().toString();

                if (educationTypeSpinner.getSelectedItem() != null) {
                    educationLevel = educationTypeSpinner.getSelectedItem().toString();
                } else {
                    textViewError.setText("Please select Education Level");
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                if (jobTitle == null || jobTitle.isEmpty() || companyName == null || companyName.isEmpty() || location == null || location.isEmpty() || salaryRange == null || salaryRange.isEmpty()) {
                    textViewError.setText("All fields are required");
                    System.out.println(jobTitle+" "+companyName+" "+location+" "+ salaryRange);
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                if(requiredSkills == null || requiredSkills.isEmpty()){
                    textViewError.setText("problem rah f required skills");
                    textViewError.setVisibility(View.VISIBLE);
                }

                // Proceed with form submission
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.52/memoire/addjob.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Adding job successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    textViewError.setText(response);
                                    textViewError.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewError.setText(error.getLocalizedMessage());
                        textViewError.setVisibility(View.VISIBLE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("job_title", jobTitle);
                        params.put("company_name", companyName);
                        params.put("location", location);
                        params.put("employment_type", employmentType);
                        params.put("required_skills", requiredSkills);
                        params.put("experience_level", experienceLevel);
                        params.put("education_level", educationLevel);
                        params.put("salary_range", salaryRange);
                        params.put("userid", String.valueOf(userId));
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}
