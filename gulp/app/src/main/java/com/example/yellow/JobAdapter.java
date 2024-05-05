package com.example.yellow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textJobName;
        private TextView textJobPosition;
        private TextView textJobRequirements;
        private TextView textJobSalaire;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textJobName = itemView.findViewById(R.id.text_job_name);
            textJobPosition = itemView.findViewById(R.id.text_job_position);
            textJobRequirements = itemView.findViewById(R.id.text_job_requirements);
            textJobSalaire = itemView.findViewById(R.id.text_job_jobsal);
        }

        public void bind(Job job) {
            textJobName.setText(job.getJobName());
            textJobPosition.setText(job.getJobPosition());
            textJobRequirements.setText(job.getJobRequirements());
            textJobSalaire.setText(job.getJobSalaire());
        }
    }
}
