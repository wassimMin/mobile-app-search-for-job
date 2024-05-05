package com.example.yellow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class applyjob extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST_CODE = 123;
    private List<Job> jobList;
    private Integer userId;
    private String jobId;
    private byte[] pdfByteArray;
    private TextView error_message;
    private SharedPreferences sharedPreferences;
    private String jobname1,jobpos1,jobreq1,jobsal1,idjob1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyjob);
        jobList = new ArrayList<>();
        Intent intent = getIntent();
        error_message = findViewById(R.id.text_error_message);
        sharedPreferences = getSharedPreferences("yellow",MODE_PRIVATE);
        idjob1 = sharedPreferences.getString("jobId", "");
        jobname1 = sharedPreferences.getString("jobName", "");
        jobpos1 = sharedPreferences.getString("jobPosition", "");
        jobreq1 = sharedPreferences.getString("jobRequirements", "");
        jobsal1 = sharedPreferences.getString("jobSalaire", "");
        userId = sharedPreferences.getInt("userid",0);
        displayJob(idjob1,jobname1,jobpos1,jobreq1,jobsal1);
        if (userId != -1) {
            Toast.makeText(this, "User ID is not Null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
            finish();
        }
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), userhome.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void displayJob(String idjob1, String jobname1, String jobpos1,String jobreq1,String jobsal1) {
        jobId = idjob1;
        TextView jobNameTextView = findViewById(R.id.text_job_name);
        jobNameTextView.setText(jobname1);

        TextView jobPositionTextView = findViewById(R.id.text_job_position);
        jobPositionTextView.setText(jobpos1);

        TextView jobRequirementsTextView = findViewById(R.id.text_job_requirements);
        jobRequirementsTextView.setText(jobreq1);

        TextView jobSalaireTextView = findViewById(R.id.text_job_jobsal);
        jobSalaireTextView.setText(jobsal1);
    }

    public void onUploadCVButtonClick(View view) {
        pickPDFFile();
    }

    private void pickPDFFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri pdfUri = data.getData();

            if (pdfUri != null) {
                pdfByteArray = getByteArrayFromUri(pdfUri); // Save the byte array
                if (pdfByteArray != null) {
                    Toast.makeText(this, "Selected PDF", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to retrieve PDF byte array", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Selected file URI is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] getByteArrayFromUri(Uri uri) {
        byte[] byteArray = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4 * 1024]; // 4 KB buffer
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byteArray = outputStream.toByteArray();
            Log.d("ByteArraySize", "Byte array size: " + byteArray.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }
    public void onApplyButtonClick(View view) {
        if (userId != null && jobId != null && pdfByteArray != null) {
            submitApplication(userId, Integer.parseInt(jobId), pdfByteArray);
        } else {
            Toast.makeText(this, "Please select PDF and fetch job details first", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitApplication(int userId, int jobId, byte[] pdfByteArray) {
        String url = "http://192.168.1.52/memoire/application.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(userId));
        params.put("job_id", String.valueOf(jobId));

        VolleyMultipartRequest multipartRequest;
        multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject result = new JSONObject(resultResponse);
                            String message = result.getString("message");
                            Toast.makeText(applyjob.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ServerResponse", "Error parsing server response: " + resultResponse);
                            Toast.makeText(applyjob.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(applyjob.this, "Error uploading file: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        error_message.setText("Error uploading file: " + error.getMessage());
                        error_message.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long millis = System.currentTimeMillis();
                params.put("cv_pdf", new DataPart(millis + ".pdf", pdfByteArray));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }

        };

        // Add the request to the RequestQueue
        queue.add(multipartRequest);
    }
}
