package com.example.yellow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    EditText editTextMessage;
    Button buttonSend;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    int id;
    int usertype;
    MessageAdapter messageAdapter;
    List<Message> messageList;
    Intent intent;
    int idreceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);
        recyclerView = findViewById(R.id.recycler_view_chat);
        sharedPreferences = getSharedPreferences("yellow", MODE_PRIVATE);
        id = sharedPreferences.getInt("userid", 0);
        usertype = sharedPreferences.getInt("usertype", 0);
        intent = getIntent();
        idreceiver = intent.getIntExtra("userid",0);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = editTextMessage.getText().toString().trim();
                if (!messageContent.isEmpty()) {
                    sendMessage(messageContent);
                } else {
                    Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        fetchMessages();
    }

    private void sendMessage(final String messageContent) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.52/memoire/send_message.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(ChatActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                            editTextMessage.setText("");
                            fetchMessages();
                        } else {
                            Toast.makeText(ChatActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sender_id", String.valueOf(id));
                params.put("receiver_id",String.valueOf(idreceiver));
                params.put("message_content", messageContent);
                params.put("message_usertype", String.valueOf(usertype));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void fetchMessages() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.52/memoire/fetch_messages.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            messageList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int messageId = jsonObject.getInt("message_id");
                                String content = jsonObject.getString("content");
                                int senderId = jsonObject.getInt("sender_id");
                                String userType = jsonObject.getString("message_usertype");

                                Message message = new Message(messageId, content, senderId, userType);
                                messageList.add(message);
                            }

                            messageAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(ChatActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                            Log.e("JsonParseError", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sender_id", String.valueOf(id));
                params.put("receiver_id", String.valueOf(idreceiver));
                return params;
            }
        };

        queue.add(stringRequest);
    }

}