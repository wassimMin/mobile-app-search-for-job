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

public class application extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CustomResponse> responseList;
    private ResponseAdapter responseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        recyclerView = findViewById(R.id.recycler_view_responses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        responseList = new ArrayList<>();
        responseAdapter = new ResponseAdapter(this,responseList);
        recyclerView.setAdapter(responseAdapter);

        fetchResponses();
    }

    private void fetchResponses() {
        String url = "http://192.168.29.101/memoire/fetch_responses.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        if (responseArray != null && responseArray.length() > 0) {
                            try {
                                List<CustomResponse> fetchedResponses = new ArrayList<>();

                                // Parse JSON response array
                                for (int i = 0; i < responseArray.length(); i++) {
                                    JSONObject responseObject = responseArray.getJSONObject(i);
                                    int applicationId = responseObject.getInt("application_id");
                                    String responseText = responseObject.getString("response");
                                    String message = responseObject.getString("message");

                                    CustomResponse response = new CustomResponse(applicationId, responseText, message);
                                    fetchedResponses.add(response);
                                }

                                // Update the adapter with fetched data
                                responseList.clear();
                                responseList.addAll(fetchedResponses);
                                responseAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(application.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(application.this, "No responses found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(application.this, "Error fetching responses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }
}
