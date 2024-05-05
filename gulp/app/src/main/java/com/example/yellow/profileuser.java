package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class profileuser extends AppCompatActivity {
    private TextView userType;
    private TextView userName;
    private TextView emailuser;

    private SharedPreferences sharedPreferences;
    private Button editBtn,backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileuser);

        userType = findViewById(R.id.user_type);
        userName = findViewById(R.id.user_name);
        emailuser = findViewById(R.id.user_email);
        editBtn = findViewById(R.id.btnEdit);
        backBtn = findViewById(R.id.btnBack);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);

        retrieveUserData();
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileuser.this,Edituser.class);
                startActivity(intent);
                finish();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileuser.this,userhome.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void retrieveUserData() {
        String userNameValue = sharedPreferences.getString("name", "");
        String userEmailValue = sharedPreferences.getString("email", "");

        if (!userNameValue.isEmpty() && !userEmailValue.isEmpty()) {
            userType.setText("User");
            userName.setText(userNameValue);
            emailuser.setText(userEmailValue);
        } else {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
        }
    }
}
