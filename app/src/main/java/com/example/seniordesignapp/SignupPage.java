package com.example.seniordesignapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupPage extends AppCompatActivity {

    String username, email, password;
    TextView tvError;
    EditText tietName, tietEmail, tietPassword;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        tietName = findViewById(R.id.name_input);
        tietEmail = findViewById(R.id.email_input);
        tietPassword = findViewById(R.id.password_input);

        nextButton = findViewById(R.id.next_button);
        tvError = findViewById(R.id.Error);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = String.valueOf(tietName.getText());
                email = String.valueOf(tietEmail.getText());
                password = String.valueOf(tietPassword.getText());

                // Create a JSON object
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", username);
                    jsonBody.put("email", email);
                    jsonBody.put("password", password);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://3.83.89.175:5000/signup";

                // Use JsonObjectRequest to send a JSON object
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, url, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if ("success".equals(status)) {
                                        Toast.makeText(getApplicationContext(), "Sign up successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        tvError.setText(response.getString("message"));
                                        tvError.setVisibility(View.VISIBLE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    tvError.setText("Error in response");
                                    tvError.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                tvError.setText(error.getLocalizedMessage());
                                tvError.setVisibility(View.VISIBLE);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                queue.add(jsonObjectRequest);
            }
        });
    }
}
