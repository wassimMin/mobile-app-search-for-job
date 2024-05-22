package com.example.yellow;

import static com.example.yellow.R.id.action_post_job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class userhome extends AppCompatActivity{
    private RecyclerView recyclerView;
    private List<Job> jobList;
    private JobAdapteruser JobAdapteruser;
    private int userId;
    private String name;
    private String email;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);

        // Start the NotificationService
        startNotificationService();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
//        JobAdapteruser = new JobAdapteruser(jobList, this);
        recyclerView.setAdapter(JobAdapteruser);
        fetchJobs();
        Intent intent = getIntent();
        userId = intent.getIntExtra("userid",0);
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("userid",userId);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        ImageView postjobs = findViewById(R.id.postjobs);
        NavigationView navigationView = findViewById(R.id.nav_view);
        postjobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_notification){
                    startActivity(new Intent(userhome.this,notificationActivity.class));
                }else if(id == R.id.nav_application){
                    startActivity(new Intent(userhome.this,application.class));
                }else if(id == R.id.nav_logout){
                    startActivity(new Intent(userhome.this,Login.class));
                    finish();
                }else if(id == R.id.nav_profile){
                    startActivity(new Intent(userhome.this,profileuser.class));
                }else if(id == R.id.chat){
                    startActivity(new Intent(userhome.this,ChatActivity.class));
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

    }

    private void startNotificationService() {
        // Start the NotificationService
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }
    public void showPopupMenu(View view){
        PopupMenu popup = new PopupMenu(this,view);
        popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                int postjobs = R.id.action_post_job;
                int consultjobs = R.id.action_consult_job;
                if(id == postjobs){
                    startActivity(new Intent(userhome.this,postjobs.class));
                    return true;
                }else if(id == consultjobs){
                    startActivity(new Intent(userhome.this,consultjobs.class));
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }
    private void fetchJobs() {
        String url = "http://192.168.1.52/memoire/getsjob.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null && response.length() > 0) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jobObject = response.getJSONObject(i);
                                    String jobId = jobObject.getString("job_id");
                                    String jobName = jobObject.getString("jobname");
                                    String jobPosition = jobObject.getString("jobpos");
                                    String jobRequirements = jobObject.getString("jobreq");
                                    String jobSalaire = jobObject.getString("jobsal");

//                                    Job job = new Job(jobId,jobName, jobPosition, jobRequirements,jobSalaire);
//                                    jobList.add(job);
                                }
                                JobAdapteruser.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(userhome.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(userhome.this, "No jobs found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(userhome.this, "Error fetching jobs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

//    @Override
//    public void onApplyClick(int position) {
//        // Get the corresponding job object
//        Job job = jobList.get(position);
//
//        // Store job details in SharedPreferences
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("jobId", job.getId());
//        editor.putString("jobName", job.getJobName());
//        editor.putString("jobPosition", job.getJobPosition());
//        editor.putString("jobRequirements", job.getJobRequirements());
//        editor.putString("jobSalaire", job.getJobSalaire());
//        editor.apply();
//
//        // Start the applyjob activity
//        Intent intent = new Intent(this, applyjob.class);
//        startActivity(intent);
//    }
}
