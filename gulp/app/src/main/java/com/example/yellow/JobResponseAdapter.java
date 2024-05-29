package com.example.yellow;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobResponseAdapter extends RecyclerView.Adapter<JobResponseAdapter.ViewHolder> {

    private List<JobResponse> jobResponseList;
    private Context context;

    public JobResponseAdapter(Context context, List<JobResponse> jobResponseList) {
        this.context = context;
        this.jobResponseList = jobResponseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobResponse jobResponse = jobResponseList.get(position);
        holder.textViewJobTitle.setText(jobResponse.getJobTitle());
        holder.textViewMessage.setText(jobResponse.getMessage());

        String status = jobResponse.getStatus();
        holder.buttonStatus.setText(status);

        if (status.equalsIgnoreCase("accepte")) {
            holder.buttonStatus.setBackgroundColor(Color.GREEN);
            holder.buttonStatus.setText("Accepted");
        } else if (status.equalsIgnoreCase("refuse")) {
            holder.buttonStatus.setBackgroundColor(Color.RED);
            holder.buttonStatus.setText("Refused");
        } else {
            holder.buttonStatus.setBackgroundColor(Color.GRAY); 
        }

        holder.imageViewIcon.setImageResource(R.drawable.ic_job);

        holder.buttonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You can handle button click here if needed
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewIcon;
        public TextView textViewJobTitle;
        public TextView textViewMessage;
        public Button buttonStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewJobTitle = itemView.findViewById(R.id.textViewJobTitle);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            buttonStatus = itemView.findViewById(R.id.buttonStatus);
        }
    }
}
