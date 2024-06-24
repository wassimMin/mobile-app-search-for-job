package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Showprofile extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private TextView location, description, experience, education;
    private GridLayout skillsGrid;
    Button backhome;

    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofile);

        sharedPreferences = getSharedPreferences("yellow",MODE_PRIVATE);
        userid = sharedPreferences.getInt("user_id",0);
        backhome = findViewById(R.id.back_to_home_button);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        experience = findViewById(R.id.expriencetext);
        education = findViewById(R.id.education_level);
        skillsGrid = findViewById(R.id.skills_grid);
        fetchUserProfile();
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Showprofile.this,Receiveapplicationdetails.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void fetchUserProfile() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.29.101/memoire/fetch_profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("null")) {
                            try {
                                JSONObject profileData = new JSONObject(response);
                                location.setText(profileData.getString("location"));
                                description.setText(profileData.getString("description"));
                                experience.setText(profileData.getString("experience_level"));
                                education.setText(profileData.getString("education_level"));
                                String userSkills = profileData.getString("required_skills");
                                String[] skillsArray = userSkills.split(",");
                                skillsGrid.removeAllViews();
                                for(String skill : skillsArray){
                                    TextView skillTextView = new TextView(Showprofile.this);
                                    skillTextView.setText(skill.trim());
                                    skillTextView.setPadding(8,8,8,8);
                                    skillsGrid.addView(skillTextView);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textViewError.setText(error.getLocalizedMessage());
//                textViewError.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", String.valueOf(userid));
                return params;
            }
        };
        queue.add(stringRequest);
    }
}