package com.example.yellow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

public class JobpostAdapter extends RecyclerView.Adapter<JobpostAdapter.JobViewHolder> {

    private Context context;
    private List<JobPost> jobList;

    public JobpostAdapter(Context context, List<JobPost> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobPost jobPost = jobList.get(position);
        holder.textJobTitle.setText(jobPost.getTitle());
        holder.textJobDescription.setText(jobPost.getDescription());
        holder.textCompanyName.setText(jobPost.getCompanyName());
        holder.textLocation.setText(jobPost.getLocation());
        holder.textJobCategory.setText(jobPost.getCategory());
        holder.textSalaryRange.setText(jobPost.getSalaryRange());

        // Handle edit button click
        holder.btnEditJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Editpostjobs.class);
                intent.putExtra("id", String.valueOf(jobPost.getId()));
                intent.putExtra("job_title",jobPost.getTitle());
                intent.putExtra("job_description",jobPost.getDescription());
                intent.putExtra("company_name",jobPost.getCompanyName());
                intent.putExtra("location",jobPost.getLocation());
                intent.putExtra("job_category",jobPost.getCategory());
                intent.putExtra("salary_range",jobPost.getSalaryRange());
                context.startActivity(intent);
            }
        });

        // Handle delete button click
        holder.btnDeleteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Remove the item from the list
                    jobList.remove(adapterPosition);
                    // Notify adapter that item is removed
                    notifyItemRemoved(adapterPosition);
                    // Optionally, you can also notify for range change to update the remaining items
                    // notifyItemRangeChanged(adapterPosition, jobList.size());

                    // Call a method to delete the job post from the backend
                    deleteJobPostFromBackend(String.valueOf(jobPost.getId())); // Pass the job post ID to delete
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView textJobTitle, textJobDescription, textCompanyName, textLocation, textJobCategory, textSalaryRange;
        Button btnEditJob, btnDeleteJob;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            textJobTitle = itemView.findViewById(R.id.text_job_title);
            textJobDescription = itemView.findViewById(R.id.text_job_description);
            textCompanyName = itemView.findViewById(R.id.text_company_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textJobCategory = itemView.findViewById(R.id.text_job_category);
            textSalaryRange = itemView.findViewById(R.id.text_salary_range);
            btnEditJob = itemView.findViewById(R.id.btn_edit_job);
            btnDeleteJob = itemView.findViewById(R.id.btn_delete_job);
        }
    }

    // Method to delete the job post from the backend
    private void deleteJobPostFromBackend(String jobId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = "http://192.168.29.101/memoire/deletepostjob.php";

        // Create the request
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response, if needed
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response, if needed
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Pass the job ID as a parameter
                Map<String, String> params = new HashMap<>();
                params.put("id", jobId);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(deleteRequest);
    }
}
