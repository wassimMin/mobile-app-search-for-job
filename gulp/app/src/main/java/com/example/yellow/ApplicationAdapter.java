package com.example.yellow;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {

    private List<AplicationItem> applicationList;
    private Context context;

    public ApplicationAdapter(Context context, List<AplicationItem> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.application_item, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AplicationItem application = applicationList.get(position);
        holder.userNameTextView.setText(application.getUserName());
        holder.jobNameTextView.setText(application.getJobName());
        holder.viewCvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCvPdf(application.getCvPdfData());
            }
        });
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResponse(application, "Accepted", "See you soon");
                deleteApplicationFromDatabase(application);
            }
        });
        holder.refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResponse(application, "Refused", "Sorry, we can't accept you at this time.");
                deleteApplicationFromDatabase(application);
            }
        });
    }
    private void removeItem(int position) {
        applicationList.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView jobNameTextView;
        Button viewCvButton;
        Button acceptButton;
        Button refuseButton;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.user_text_name);
            jobNameTextView = itemView.findViewById(R.id.job_name);
            viewCvButton = itemView.findViewById(R.id.btn_view_cv);
            acceptButton = itemView.findViewById(R.id.btn_accept);
            refuseButton = itemView.findViewById(R.id.btn_refuse);
        }
    }

    private void openCvPdf(byte[] cvPdfData) {
        try {
            File pdfFile = File.createTempFile("application", ".pdf", context.getCacheDir());
            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(cvPdfData);
            fos.close();

            Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error opening PDF", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteApplicationFromDatabase(AplicationItem application) {
        if (application == null) {
            Toast.makeText(context, "Application is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String applicationId = application.getId();

        String url = "http://192.168.1.52/memoire/delete_application.php?application_id=" + applicationId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server if needed
                        // For example, show a success message
                        Toast.makeText(context, "Application deleted successfully", Toast.LENGTH_SHORT).show();
                        // Remove the application from the list and notify the adapter
                        applicationList.remove(application);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error deleting application: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void updateResponse(AplicationItem application, String response, String message) {
        String url = "http://192.168.1.52/memoire/set_response.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response if needed
                        Toast.makeText(context, "Response updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(context, "Error updating response: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("application_id", String.valueOf(application.getId())); // Replace getId() with your method to get the application ID
                params.put("response", response);
                params.put("message", message);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    }


