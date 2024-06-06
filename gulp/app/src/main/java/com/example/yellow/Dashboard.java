package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public class Dashboard extends AppCompatActivity {
    TextView textUserName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        intent = getIntent();
        int userid = intent.getIntExtra("userid",0);
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        CardView btnProfile = findViewById(R.id.card_profile);
        CardView btnAddJob = findViewById(R.id.card_add_job);
        CardView btnStatus = findViewById(R.id.card_status);
        CardView btnApplication = findViewById(R.id.card_application);
        CardView btnChat = findViewById(R.id.card_chat);
        CardView btnLogout = findViewById(R.id.card_logout);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, profile.class);
                intent.putExtra("userid",userid);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, addjob.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Status.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

        btnApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, recieveapplication.class);
                startActivity(intent);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, UserSelection.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
