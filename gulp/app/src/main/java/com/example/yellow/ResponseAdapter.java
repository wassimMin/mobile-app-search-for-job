package com.example.yellow;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ViewHolder> {

    private Context context;
    private List<CustomResponse> responseList;

    public ResponseAdapter(Context context, List<CustomResponse> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.response_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind response data to ViewHolder
        CustomResponse response = responseList.get(position);
        holder.bind(response);

        // Handle button click
        holder.checkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the application ID of the clicked response
                int applicationId = response.getApplicationId();

                // Make a request to remove the response from the database
                removeResponse(applicationId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView applicationIdTextView;
        private TextView responseTextView;
        private TextView messageTextView;
        private Button checkedButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            applicationIdTextView = itemView.findViewById(R.id.application_id);
            responseTextView = itemView.findViewById(R.id.text_response);
            messageTextView = itemView.findViewById(R.id.text_message);
            checkedButton = itemView.findViewById(R.id.btn_checked_btn);
        }

        public void bind(CustomResponse response) {
            applicationIdTextView.setText(String.valueOf(response.getApplicationId()));
            responseTextView.setText(response.getResponseText());
            messageTextView.setText(response.getMessage());
        }
    }

    // Method to remove response from the database
    private void removeResponse(int applicationId) {
        String url = "http://192.168.29.101/memoire/remove_response.php";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from PHP script
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        // Optionally, remove the response from the RecyclerView
                        // responseList.remove(position);
                        // notifyItemRemoved(position);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parameters to send to the PHP script
                Map<String, String> params = new HashMap<>();
                params.put("application_id", String.valueOf(applicationId));
                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }
}
