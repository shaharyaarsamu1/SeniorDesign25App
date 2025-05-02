package com.example.seniordesignapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusPage extends AppCompatActivity {

    private TextView[] portStatus = new TextView[4];
    private Switch[] portSwitch = new Switch[4];

    private static final String TOGGLE_URL = "http://54.152.160.12:5000/send";
    private static final String STATUS_URL = "http://54.152.160.12:3000/status";
    private static final String QUEUE_NAME = "mqtt-subscription-89620qos0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_page);

        Context context = this;

        for (int i = 0; i < 4; i++) {
            int statusId = getResources().getIdentifier("status_port_" + (i + 1), "id", getPackageName());
            int switchId = getResources().getIdentifier("switch_port_" + (i + 1), "id", getPackageName());
            portStatus[i] = findViewById(statusId);
            portSwitch[i] = findViewById(switchId);
        }

        fetchStatusFromServer(context);

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            portSwitch[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                portStatus[finalI].setText(isChecked ? "Active" : "Inactive");
                sendToggleToESP(context, finalI + 1, isChecked);
            });
        }
    }

    private void fetchStatusFromServer(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                STATUS_URL,
                null,
                response -> {
                    try {
                        for (int i = 0; i < 4; i++) {
                            String key = "outlet_" + (i + 1);
                            String status = response.getString(key);
                            boolean isActive = status.equalsIgnoreCase("Active");
                            portSwitch[i].setChecked(isActive);
                            portStatus[i].setText(isActive ? "Active" : "Inactive");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Failed to parse data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Failed to fetch status", Toast.LENGTH_LONG).show();
                }
        );

        queue.add(request);
    }

    private void sendToggleToESP(Context context, int outletNumber, boolean isActive) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();
        try {
            JSONObject message = new JSONObject();
            message.put("outlet_" + outletNumber, isActive ? 1 : 0);

            body.put("queue", QUEUE_NAME);
            body.put("message", message.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to create JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                TOGGLE_URL,
                body,
                response -> Toast.makeText(context, "Toggled outlet " + outletNumber, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Toggle failed: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }
}
