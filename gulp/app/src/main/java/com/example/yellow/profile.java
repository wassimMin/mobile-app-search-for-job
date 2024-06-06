package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        userid = intent.getIntExtra("userid", 0);

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
