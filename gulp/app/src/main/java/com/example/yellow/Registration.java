package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    TextInputEditText textInputEditTextname, textInputEditTextemail, textInputEditTextpassword;
    Button buttonSubmit;
    String name, email, password, userType;
    TextView textviewerror;
    ProgressBar progressbar;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textInputEditTextname = findViewById(R.id.name);
        textInputEditTextemail = findViewById(R.id.email);
        textInputEditTextpassword = findViewById(R.id.password);
        buttonSubmit = findViewById(R.id.submit);
        textviewerror = findViewById(R.id.error);
        radioGroup = findViewById(R.id.radioGroup);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textviewerror.setVisibility(View.GONE);
                name = String.valueOf(textInputEditTextname.getText());
                email = String.valueOf(textInputEditTextemail.getText());
                password = String.valueOf(textInputEditTextpassword.getText());

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(getApplicationContext(), "Please select a user type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedId == R.id.radio_company) {
                    userType = "1";
                } else if(selectedId == R.id.radio_user) {
                    userType = "0";
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.29.101/memoire/register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("success")) {
                                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Registration.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        String message = jsonObject.getString("message");
                                        textviewerror.setText(message);
                                        textviewerror.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textviewerror.setText(error.getLocalizedMessage());
                        textviewerror.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("name", name);
                        paramV.put("email", email);
                        paramV.put("password", password);
                        paramV.put("usertype", userType);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}
