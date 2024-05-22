package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JobFilter extends AppCompatActivity {

    private Spinner employmentTypeSpinner, experienceTypeSpinner, educationTypeSpinner;
    private TextInputEditText description,locationEditText, skillsEditText;
    private Button submitButton;
    TextView textViewError;
    Intent intent;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_filter);
        intent = getIntent();
        userId= intent.getIntExtra("userid",0);
        employmentTypeSpinner = findViewById(R.id.employment_type_spinner);
        experienceTypeSpinner = findViewById(R.id.experience_type_spinner);
        educationTypeSpinner = findViewById(R.id.education_type_spinner);
        description = findViewById(R.id.descriptiontext);
        locationEditText = findViewById(R.id.locationtext);
        skillsEditText = findViewById(R.id.requiredskills);
        submitButton = findViewById(R.id.submit);
        textViewError = findViewById(R.id.error);
        ArrayAdapter<CharSequence> employmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.employment_types, android.R.layout.simple_spinner_item);
        employmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employmentTypeSpinner.setAdapter(employmentAdapter);

        ArrayAdapter<CharSequence> experienceAdapter = ArrayAdapter.createFromResource(this,
                R.array.experience_types, android.R.layout.simple_spinner_item);
        experienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experienceTypeSpinner.setAdapter(experienceAdapter);

        ArrayAdapter<CharSequence> educationAdapter = ArrayAdapter.createFromResource(this,
                R.array.education_types, android.R.layout.simple_spinner_item);
        educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        educationTypeSpinner.setAdapter(educationAdapter);
        fetchUserProfile();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFilter();
            }
        });
    }
    private void fetchUserProfile() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.52/memoire/fetch_profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("null")) {
                            try {
                                JSONObject profileData = new JSONObject(response);

                                employmentTypeSpinner.setSelection(getIndex(employmentTypeSpinner, profileData.getString("employment_type")));
                                experienceTypeSpinner.setSelection(getIndex(experienceTypeSpinner, profileData.getString("experience_level")));
                                educationTypeSpinner.setSelection(getIndex(educationTypeSpinner, profileData.getString("education_level")));
                                locationEditText.setText(profileData.getString("location"));
                                skillsEditText.setText(profileData.getString("skills"));
                                description.setText(profileData.getString("description"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                params.put("userid", String.valueOf(userId));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void submitFilter() {
        textViewError.setVisibility(View.GONE);
        String description1 = description.getText().toString().trim();
        String employmentType = employmentTypeSpinner.getSelectedItem().toString();
        String experienceType = experienceTypeSpinner.getSelectedItem().toString();
        String educationType = educationTypeSpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString().trim();
        String skills = skillsEditText.getText().toString().trim();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.52/memoire/profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(getApplicationContext(), "ThankYou", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Homeuser.class);
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
                params.put("description",description1);
                params.put("location", location);
                params.put("employment_type", employmentType);
                params.put("required_skills", skills);
                params.put("experience_level", experienceType);
                params.put("education_level", educationType);
                params.put("userid", String.valueOf(userId));
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        return 0;
    }
    }

