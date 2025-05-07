package com.example.seniordesignapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Get the "destination" passed into the Loading screen
        String destination = getIntent().getStringExtra("destination");

        new Handler().postDelayed(() -> {
            Intent intent = null;

            switch (destination) {
                case "main":
                    intent = new Intent(LoadingActivity.this, MainActivity.class);
                    break;
                case "graph":
                    intent = new Intent(LoadingActivity.this, OutletGraphsPage.class);
                    break;
                case "toggle":
                    intent = new Intent(LoadingActivity.this, StatusPage.class);
                    break;
                default:
                    finish(); // nothing to do
                    return;
            }

            startActivity(intent);
            finish();
        }, 1000); // 1 second
    }
}
