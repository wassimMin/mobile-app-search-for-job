package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Receiveapplicationdetails extends AppCompatActivity {
    TextView jobtitle,username,application_date;
    EditText editext;
    Button profile,opencv,accepte,refuse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveapplicationdetails);
        jobtitle =findViewById(R.id.job_title);
        username = findViewById(R.id.user_name);
        application_date = findViewById(R.id.application_date);

        editext = findViewById(R.id.response_message);

        profile = findViewById(R.id.btn_view_profile);
        opencv = findViewById(R.id.btn_view_cv);

        accepte = findViewById(R.id.btn_accept);
        refuse = findViewById(R.id.btn_refuse);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                open user profile
            }
        });
        opencv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                open cv pdf of user
            }
        });
        
    }
}