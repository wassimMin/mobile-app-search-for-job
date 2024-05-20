package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    TextInputEditText textInputEditTextjobame, textInputEditTextjobpos, textInputEditTextjobreq, textInputEditTextjobsal;
    Button buttonSubmit;
    String jobname, jobpos, jobreq, jobsal;
    TextView textviewerror;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjob);
        textInputEditTextjobame = findViewById(R.id.job_name);
        textInputEditTextjobpos = findViewById(R.id.job_position);
        textInputEditTextjobreq = findViewById(R.id.job_requirements);
        textInputEditTextjobsal = findViewById(R.id.job_salaire);
        buttonSubmit = findViewById(R.id.submit);
        textviewerror = findViewById(R.id.error);
        progressbar = findViewById(R.id.loading);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textviewerror.setVisibility(View.GONE);
                progressbar.setVisibility(View.VISIBLE);
                jobname = String.valueOf(textInputEditTextjobame.getText());
                jobpos = String.valueOf(textInputEditTextjobpos.getText());
                jobreq = String.valueOf(textInputEditTextjobreq.getText());
                jobsal = String.valueOf(textInputEditTextjobsal.getText());
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.120.101/memoire/addjob.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressbar.setVisibility(View.GONE);
                                if (response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Adding job successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    textviewerror.setText(response);
                                    textviewerror.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        textviewerror.setText(error.getLocalizedMessage());
                        textviewerror.setVisibility(View.VISIBLE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("jobname", jobname);
                        params.put("jobpos", jobpos);
                        params.put("jobreq", jobreq);
                        params.put("jobsal", jobsal);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}
