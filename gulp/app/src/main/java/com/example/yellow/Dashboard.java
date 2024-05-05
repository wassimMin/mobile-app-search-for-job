package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    TextView textUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        textUserName = findViewById(R.id.text_user_name);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        textUserName.setText("Welcome, "+ userName);
        findViewById(R.id.btn_add_job).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,addjob.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,Status.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,profile.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_applications).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, recieveapplication.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}