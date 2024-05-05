package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Edituser extends AppCompatActivity {
    TextInputEditText textinputname,textinputemail,textinputpassword;
    Button editbtn;
    TextView textViewError;
    ProgressBar progressBar;
    int userid,usertype;
    String username,useremail,userpassword;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);
        editbtn = findViewById(R.id.edit);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        ImageButton backButton = findViewById(R.id.back_button);

        textinputname = findViewById(R.id.user_name);
        textinputemail = findViewById(R.id.user_email);
        textinputpassword = findViewById(R.id.user_password);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);
        userid = sharedPreferences.getInt("userid",0);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        userpassword = sharedPreferences.getString("password","");
        usertype = sharedPreferences.getInt("usertype",0);
        textinputname.setText(username);
        textinputemail.setText(useremail);
        textinputpassword.setText(userpassword);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edituser();
            }
        });
        if(usertype == 0){
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), profileuser.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), profile.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    private void edituser() {
        String userNameo = textinputname.getText().toString().trim();
        String userEmailo = textinputemail.getText().toString().trim();
        String userPasswordo = textinputpassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.52/memoire/edituser.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.equals("success")) {
                            Toast.makeText(Edituser.this, "User details updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            textViewError.setText(response);
                            textViewError.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                textViewError.setText(error.getMessage());
                textViewError.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", String.valueOf(userid));
                params.put("name", userNameo);
                params.put("email", userEmailo);
                params.put("password", userPasswordo);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}