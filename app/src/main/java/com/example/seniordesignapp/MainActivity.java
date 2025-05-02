package com.example.seniordesignapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Optional insets padding (keep if needed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button to go to Status Page
        Button statusButton = findViewById(R.id.go_to_status_button);
        statusButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatusPage.class);
            startActivity(intent);
        });

        Button graphsButton = findViewById(R.id.btn_view_graphs);
        graphsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OutletGraphsPage.class);
            startActivity(intent);
        });
    }
}
