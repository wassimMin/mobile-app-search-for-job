package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Receiveapplicationdetails extends AppCompatActivity {
    TextView jobtitle,username,application_date;
    EditText editext;
    Button profile,opencv,accepte,refuse;
    SharedPreferences sharedPreferences;
    String message,applicationId;
    int companyId,userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveapplicationdetails);
        jobtitle =findViewById(R.id.job_title);
        username = findViewById(R.id.user_name);
        application_date = findViewById(R.id.application_date);
        sharedPreferences = getSharedPreferences("yellow",MODE_PRIVATE);
        String nameuser = sharedPreferences.getString("username",null);
        String titlejob = sharedPreferences.getString("jobtitle",null);
        String cvPdfDataBase64 = sharedPreferences.getString("cv_pdf",null);
        companyId = sharedPreferences.getInt("userid",-1);
        userId = sharedPreferences.getInt("user_id",-1);
        applicationId = sharedPreferences.getString("applicationid",null);

        profile = findViewById(R.id.btn_view_profile);
        opencv = findViewById(R.id.btn_view_cv);

        accepte = findViewById(R.id.btn_accept);
        refuse = findViewById(R.id.btn_refuse);
        username.setText(nameuser);
        jobtitle.setText(titlejob);
        accepte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "You Have Been Accepted To The Job";
                setResponse("Accepte");
            }
        });
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = "Sorry, We Cannt Accepte For The Job";
                setResponse("Refuse");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Receiveapplicationdetails.this,Showprofile.class);
                startActivity(intent);
            }
        });
        opencv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cvPdfDataBase64 != null) {
                    byte[] cvPdfData = Base64.decode(cvPdfDataBase64, Base64.DEFAULT);

                    try {
                        File pdfFile = new File(getFilesDir(), "cv.pdf");
                        FileOutputStream fos = new FileOutputStream(pdfFile);
                        fos.write(cvPdfData);
                        fos.close();

                        // Get the absolute file path of the PDF
                        String authority = getApplicationContext().getPackageName() + ".fileprovider";
                        Uri pdfUri = FileProvider.getUriForFile(Receiveapplicationdetails.this, authority, pdfFile);

                        // Create an intent to open the PDF using a viewer
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(pdfUri, "application/*");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        // Verify that there is an app available to handle the intent
                        PackageManager packageManager = getPackageManager();
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(Receiveapplicationdetails.this, "No app found to open PDF", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Receiveapplicationdetails.this, "Failed to open PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Receiveapplicationdetails.this, "No PDF data found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setResponse(String status){
        if (message.isEmpty() || applicationId.isEmpty() || companyId == -1 || userId == -1) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://192.168.29.101/memoire/set_response.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Receiveapplicationdetails.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Receiveapplicationdetails.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("message", message);
                params.put("status", status);
                params.put("applicationid", applicationId);
                params.put("companyid", String.valueOf(companyId));
                params.put("userid", String.valueOf(userId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    }
