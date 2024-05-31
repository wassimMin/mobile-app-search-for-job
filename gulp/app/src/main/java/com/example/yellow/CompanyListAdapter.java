package com.example.yellow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.CompanyViewHolder> {

    private Context context;
    private List<JobResponse> jobResponseList;

    public CompanyListAdapter(Context context, List<JobResponse> jobResponseList) {
        this.context = context;
        this.jobResponseList = jobResponseList;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        JobResponse jobResponse = jobResponseList.get(position);
        holder.jobTitle.setText(jobResponse.getJobTitle());
        holder.message.setText(jobResponse.getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("yellow",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userid",jobResponse.getUserid());
                editor.apply();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("companyid", jobResponse.getCompanyid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobResponseList.size();
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle;
        TextView message;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            message = itemView.findViewById(R.id.message);
        }
    }
}
