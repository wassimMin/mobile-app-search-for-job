package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class profile extends AppCompatActivity {

    private TextView userType;
    private TextView userName;
    private TextView emailuser;

    private SharedPreferences sharedPreferences;
    Button btnEdit,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userType = findViewById(R.id.user_type);
        userName = findViewById(R.id.user_name);
        emailuser = findViewById(R.id.user_email);
        btnEdit = findViewById(R.id.btnEdit);
        btnBack = findViewById(R.id.btnBack);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);

        retrieveUserData();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this,Edituser.class);
                startActivity(intent);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void retrieveUserData() {
        String userNameValue = sharedPreferences.getString("name", "");
        String userEmailValue = sharedPreferences.getString("email", "");

        if (!userNameValue.isEmpty() && !userEmailValue.isEmpty()) {
            userType.setText("Company");
            userName.setText(userNameValue);
            emailuser.setText(userEmailValue);
        } else {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
        }
    }
}