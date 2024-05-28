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

public class JobUserAdapter extends RecyclerView.Adapter<JobUserAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobList;

    public JobUserAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jobuseritem, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTitle.setText(job.getJobTitle());
        holder.salaryRange.setText("Salaire: " + job.getSalaryRange());
        holder.experienceLevel.setText("Experience Level: " + job.getExperienceLevel());

        holder.jobImage.setImageResource(R.drawable.applyjob);

        holder.readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,applyjob.class);
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

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, companyName, location, salaryRange, experienceLevel;
        ImageView jobImage;
        Button readMoreButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.text_job_name);
            salaryRange = itemView.findViewById(R.id.text_job_salaire);
            experienceLevel = itemView.findViewById(R.id.text_job_experience);
            jobImage = itemView.findViewById(R.id.image_job);
            readMoreButton = itemView.findViewById(R.id.btn_read_more);
        }
    }
}
