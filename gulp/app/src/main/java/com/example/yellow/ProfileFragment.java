package com.example.yellow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    TextView nameprofile;
    TextView emailprofile;
    SharedPreferences sharedPreferences;
    Button logout,filter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameprofile = view.findViewById(R.id.profile_name);
        emailprofile = view.findViewById(R.id.profile_email);
        logout = view.findViewById(R.id.btn_logout);
        filter = view.findViewById(R.id.btn_job);

        sharedPreferences = requireActivity().getSharedPreferences("yellow", getContext().MODE_PRIVATE);
        String username = sharedPreferences.getString("name", "");
        String useremail = sharedPreferences.getString("email", "");
        int userid = sharedPreferences.getInt("userid",0);
        nameprofile.setText(username);
        emailprofile.setText(useremail);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(getActivity(),JobFilter.class);
                intent1.putExtra("userid",userid);
                startActivity(intent1);
                requireActivity().finish();
            }
        });
    }
}
