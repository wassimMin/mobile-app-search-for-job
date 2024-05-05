package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class Editpostjobs extends AppCompatActivity {

    EditText editJobTitle, editJobDescription, editCompanyName, editLocation, editJobCategory, editSalaryRange;
    Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpostjobs);

        editJobTitle = findViewById(R.id.edit_job_title);
        editJobDescription = findViewById(R.id.edit_job_description);
        editCompanyName = findViewById(R.id.edit_company_name);
        editLocation = findViewById(R.id.edit_location);
        editJobCategory = findViewById(R.id.edit_job_category);
        editSalaryRange = findViewById(R.id.edit_salary_range);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        Intent intent = getIntent();
        String jobId = intent.getStringExtra("id");

        editJobTitle.setText(intent.getStringExtra("job_title"));
        editJobDescription.setText(intent.getStringExtra("job_description"));
        editCompanyName.setText(intent.getStringExtra("company_name"));
        editLocation.setText(intent.getStringExtra("location"));
        editJobCategory.setText(intent.getStringExtra("job_category"));
        editSalaryRange.setText(intent.getStringExtra("salary_range"));

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJobPost(jobId);
            }
        });
    }

    private void updateJobPost(String jobId) {
        String url = "http://192.168.1.52/memoire/update_job_post.php";

        StringRequest updateRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Editpostjobs.this, "Job post updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity after updating
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Editpostjobs.this, "Error updating job post", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", jobId);
                params.put("job_title", editJobTitle.getText().toString());
                params.put("job_description", editJobDescription.getText().toString());
                params.put("company_name", editCompanyName.getText().toString());
                params.put("location", editLocation.getText().toString());
                params.put("job_category", editJobCategory.getText().toString());
                params.put("salary_range", editSalaryRange.getText().toString());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(updateRequest);
    }
}
