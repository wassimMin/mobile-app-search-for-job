package com.example.yellow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profileuser extends AppCompatActivity {

    private TextView name, location, email, description, experience, education;
    SharedPreferences sharedPreferences;
    private GridLayout skillsGrid;
    int userId;
    Button backhome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileuser);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);
        String username = sharedPreferences.getString("name", "");
        String useremail = sharedPreferences.getString("email", "");
        userId= sharedPreferences.getInt("userid",0);
        backhome = findViewById(R.id.back_to_home_button);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        email = findViewById(R.id.email);
        description = findViewById(R.id.description);
        experience = findViewById(R.id.expriencetext);
        education = findViewById(R.id.education_level);
        name.setText(username);
        email.setText(useremail);
        skillsGrid = findViewById(R.id.skills_grid);
        fetchUserProfile();
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(profileuser.this,Homeuser.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void fetchUserProfile() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.52/memoire/fetch_profile.php";

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
                                populateSkillsGrid(userSkills);
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
                params.put("userid", String.valueOf(userId));
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void populateSkillsGrid(String skills) {
        String[] skillsArray = skills.split(",");

        skillsGrid.removeAllViews();

        for (String skill : skillsArray) {
            TextView skillTextView = new TextView(this);
            skillTextView.setText(skill.trim());
            skillTextView.setTextSize(14);
            skillTextView.setTextColor(getResources().getColor(R.color.black));
            skillTextView.setPadding(8, 8, 8, 8);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);
            skillTextView.setLayoutParams(params);

            skillsGrid.addView(skillTextView);
        }
    }
}
