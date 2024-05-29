package com.example.yellow;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class showResponse extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobResponseAdapter jobResponseAdapter;
    private List<JobResponse> jobResponseList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_response);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobResponseList = new ArrayList<>();
        jobResponseAdapter = new JobResponseAdapter(this, jobResponseList);
        recyclerView.setAdapter(jobResponseAdapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchJobResponses();
    }

    private void fetchJobResponses() {
        String url = "http://192.168.1.52/memoire/fetch_responses.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String jobTitle = jsonObject.getString("job_name");
                        String message = jsonObject.getString("message");
                        String status = jsonObject.getString("status");

                        JobResponse jobResponse = new JobResponse(jobTitle, message, status);
                        jobResponseList.add(jobResponse);
                    }

                    jobResponseAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(showResponse.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(showResponse.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
}
