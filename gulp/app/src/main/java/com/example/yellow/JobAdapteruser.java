package com.example.yellow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class JobAdapteruser extends RecyclerView.Adapter<JobAdapteruser.ViewHolder> {

    private List<Job> jobList;
    private OnApplyClickListener onApplyClickListener; // Interface instance

    // Constructor to set the listener
    public JobAdapteruser(List<Job> jobList, OnApplyClickListener listener) {
        this.jobList = jobList;
        this.onApplyClickListener = listener; // Set the listener
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textJobName;
        private TextView textJobPosition;
        private TextView textJobRequirements;
        private TextView textJobSalaire;
        private Button applyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textJobName = itemView.findViewById(R.id.text_job_name);
            textJobPosition = itemView.findViewById(R.id.text_job_position);
            textJobRequirements = itemView.findViewById(R.id.text_job_requirements);
            textJobSalaire = itemView.findViewById(R.id.text_job_jobsal);
            applyButton = itemView.findViewById(R.id.btn_apply_job);

            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Pass the clicked position to the activity
                    if (onApplyClickListener != null) {
                        onApplyClickListener.onApplyClick(getAdapterPosition());
                    }
                }
            });
        }

        public void bind(Job job) {
//            textJobName.setText(job.getJobName());
//            textJobPosition.setText(job.getJobPosition());
//            textJobRequirements.setText(job.getJobRequirements());
//            textJobSalaire.setText(job.getJobSalaire());
        }
    }

    // Interface for click events
    public interface OnApplyClickListener {
        void onApplyClick(int position);
    }
}