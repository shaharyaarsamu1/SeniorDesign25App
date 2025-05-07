package com.example.seniordesignapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.next_button);
        signupButton = findViewById(R.id.signup_button);

        // Login button functionality
        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            makeLoginRequest(email, password);
        });

        // Redirect to Signup page
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, SignupPage.class);
            startActivity(intent);
            finish();
        });
    }

    private void makeLoginRequest(String email, String password) {
        String url = "http://3.83.89.175:5000/login";

        // Prepare JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        String status = response.optString("status", "");
                        if ("success".equals(status)) {
                            Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            // Add small delay to allow toast to finish showing
                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(LoginPage.this, LoadingActivity.class);
                                intent.putExtra("destination", "main");
                                startActivity(intent);
                                finish();
                            }, 300); // 0.3 second delay

                        } else {
                            String message = response.optString("message", "Login failed");
                            Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        if (!LoginPage.this.isFinishing()) {
                            Toast.makeText(LoginPage.this, "Error in response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> Toast.makeText(LoginPage.this, "Error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}
