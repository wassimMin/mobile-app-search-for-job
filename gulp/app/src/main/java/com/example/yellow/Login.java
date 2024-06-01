package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class Login extends AppCompatActivity {
    TextInputEditText textinputedittextemail, textinputedittextpassword;
    Button buttonsumbit;
    TextView textviewerror, textviewregisternow;
    ProgressBar progressbar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textinputedittextemail = findViewById(R.id.email);
        textinputedittextpassword = findViewById(R.id.password);
        textviewerror = findViewById(R.id.error);
        textviewregisternow = findViewById(R.id.register_now);
        buttonsumbit = findViewById(R.id.submit);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);

        buttonsumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textviewerror.setVisibility(View.GONE);
                String email = String.valueOf(textinputedittextemail.getText()).trim();
                String password = String.valueOf(textinputedittextpassword.getText()).trim();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.52/memoire/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("success")) {
                                        int userid = jsonObject.getInt("userid");
                                        String token = jsonObject.getString("token");
                                        int userType = jsonObject.getInt("usertype");
                                        String name = jsonObject.getString("name");
                                        String email = jsonObject.getString("email");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("userid", jsonObject.getInt("userid"));
                                        editor.putString("token", jsonObject.getString("token"));
                                        editor.putString("name", jsonObject.getString("name"));
                                        editor.putString("email", jsonObject.getString("email"));
                                        editor.putInt("usertype", jsonObject.getInt("usertype"));
                                        editor.putString("password",jsonObject.getString("password"));
                                        editor.apply();

                                        if (userType == 1) {
                                            Intent intent = new Intent(Login.this, Dashboard.class);
                                            intent.putExtra("usertype", "Company");
                                            intent.putExtra("name", name);
                                            intent.putExtra("email", email);
                                            intent.putExtra("userid",userid);
                                            startActivity(intent);
                                        }else if(userType == 0){
                                            Intent intent = new Intent(Login.this, Homeuser.class);
                                            startActivity(intent);
                                            intent.putExtra("name", name);
                                            intent.putExtra("email", email);
                                            intent.putExtra("userid",userid);
                                            finish();
                                        }
                                        else {
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.putExtra("token", token); // Pass token to next activity
                                            startActivity(intent);
                                        }
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
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };
                // Add the string request to the RequestQueue
                queue.add(stringRequest);
            }
        });

        textviewregisternow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
