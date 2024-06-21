package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.yellow.databinding.ActivityProfileBinding;

public class profile extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private ActivityProfileBinding binding;
    private Intent intent;
    private String name, email, password;
    private int userid;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);
        intent = getIntent();
        name = sharedPreferences.getString("name","");
        email =  sharedPreferences.getString("email","");
        password =  sharedPreferences.getString("password","");
        userid = sharedPreferences.getInt("userid",0);

        viewModel.setUserData(name, email);

        binding.btnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(profile.this, Edituser.class);
            editIntent.putExtra("userid", userid);
            editIntent.putExtra("name", name);
            editIntent.putExtra("email", email);
            editIntent.putExtra("password", password);
            startActivity(editIntent);
            finish();
        });

        binding.btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(profile.this, Dashboard.class);
            startActivity(backIntent);
            finish();
        });
    }
}
