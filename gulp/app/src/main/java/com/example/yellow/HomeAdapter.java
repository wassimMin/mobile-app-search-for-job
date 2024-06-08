package com.example.yellow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<HomeItem> itemList;
    private Context context;

    public HomeAdapter(List<HomeItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final HomeItem item = itemList.get(position);
        holder.titleView.setText(item.getTitle());
        holder.subtitleView.setText(item.getSubtitle());

        if (item.getTitle().equals("Profile")) {
            holder.imageView.setImageResource(R.drawable.profile);
            holder.imageView.setBackgroundResource(R.drawable.circle_background);
        } else {
            holder.imageView.setImageResource(item.getImageResource());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (item.getTitle().equals("Profile")) {
                    intent = new Intent(context, profileuser.class);
                } else if (item.getTitle().equals("Jobs")) {
                    intent = new Intent(context, Showjobs.class);
                } else if (item.getTitle().equals("Application")) {
                    intent = new Intent(context, showResponse.class);
                } else if (item.getTitle().equals("Chat")) {
                    intent = new Intent(context, CompanyListActivity.class);
                }
                if (intent != null) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("yellow", Context.MODE_PRIVATE);
                    int userid = sharedPreferences.getInt("userid", -1);
                    intent.putExtra("userid", userid);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView subtitleView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            titleView = itemView.findViewById(R.id.item_title);
            subtitleView = itemView.findViewById(R.id.item_subtitle);
        }
    }
}
