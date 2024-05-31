package com.example.yellow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

public class CompanyListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompanyListAdapter adapter;
    private List<JobResponse> jobResponseList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobResponseList = new ArrayList<>();
        adapter = new CompanyListAdapter(this, jobResponseList);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchAcceptedOffers();
    }

    private void fetchAcceptedOffers() {
        String url = "http://192.168.1.52/memoire/fetch_responses.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String status = jsonObject.getString("status");
                        if ("accepted".equals(status)) {
                            int companyid = jsonObject.getInt("companyid");
                            int userid = jsonObject.getInt("userid");
                            String jobTitle = jsonObject.getString("job_name");
                            String message = jsonObject.getString("message");

                            JobResponse jobResponse = new JobResponse(jobTitle, message, status, companyid, userid);
                            jobResponseList.add(jobResponse);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CompanyListActivity.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(CompanyListActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
}