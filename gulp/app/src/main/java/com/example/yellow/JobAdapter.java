package com.example.yellow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<Job> jobList;
    private Context context;

    public JobAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view, context);
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

        private TextView textJobStatus;
        private ImageView imageJob;
        private TextView textJobName;
        private TextView textJobSalaire;
        private TextView textJobExperience;
        private Button btnReadMore;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            textJobStatus = itemView.findViewById(R.id.text_job_status);
            imageJob = itemView.findViewById(R.id.image_job);
            textJobName = itemView.findViewById(R.id.text_job_name);
            textJobSalaire = itemView.findViewById(R.id.text_job_salaire);
            textJobExperience = itemView.findViewById(R.id.text_job_experience);
            btnReadMore = itemView.findViewById(R.id.btn_read_more);
        }

        public void bind(Job job) {
            textJobName.setText(job.getJobTitle());
            textJobSalaire.setText("Salary: " + job.getSalaryRange());
            textJobExperience.setText("Experience Level: " + job.getExperienceLevel());
            btnReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetails.class);
                    intent.putExtra("jobid",String.valueOf(job.getId()));
                    intent.putExtra("job_title", job.getJobTitle());
                    intent.putExtra("job_companyname", job.getCompanyName());
                    intent.putExtra("job_salary", job.getSalaryRange());
                    intent.putExtra("job_experience", job.getExperienceLevel());
                    intent.putExtra("job_location", job.getLocation());
                    intent.putExtra("job_education", job.getEducationLevel());
                    intent.putExtra("job_employment", job.getEmploymentType());
                    intent.putExtra("job_requiredskills", job.getRequiredSkills());
                    context.startActivity(intent);
                }
            });
        }
    }
}
