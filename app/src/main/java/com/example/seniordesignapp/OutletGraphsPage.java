package com.example.seniordesignapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OutletGraphsPage extends AppCompatActivity {

    private LineChart[] outletCharts = new LineChart[4];
    private static final String BASE_URL = "http://54.152.160.12:3000/powerdata";
    private Spinner filterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_graphs);

        outletCharts[0] = findViewById(R.id.outlet1_chart);
        outletCharts[1] = findViewById(R.id.outlet2_chart);
        outletCharts[2] = findViewById(R.id.outlet3_chart);
        outletCharts[3] = findViewById(R.id.outlet4_chart);

        filterSpinner = findViewById(R.id.filter_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Day", "Week", "Month"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String range = "day";
                if (position == 1) range = "week";
                else if (position == 2) range = "month";
                fetchDataFromServer(range);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void fetchDataFromServer(String range) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BASE_URL + "?range=" + range;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        ArrayList<Entry>[] entries = new ArrayList[4];
                        for (int i = 0; i < 4; i++) entries[i] = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            JSONObject sensorValues = new JSONObject(obj.getString("SensorValues"));

                            for (int j = 0; j < 4; j++) {
                                String key = "outlet_" + (j + 1);
                                float power = (float) sensorValues.getJSONObject(key).getDouble("power");
                                entries[j].add(new Entry(i, power));
                            }
                        }

                        for (int i = 0; i < 4; i++) {
                            if (entries[i].isEmpty()) {
                                outletCharts[i].clear();
                                outletCharts[i].setNoDataText("No data available for selected range");
                                outletCharts[i].invalidate();
                            } else {
                                LineDataSet dataSet = new LineDataSet(entries[i], "Outlet " + (i + 1));
                                LineData lineData = new LineData(dataSet);
                                outletCharts[i].setData(lineData);
                                Description desc = new Description();
                                desc.setText("Outlet " + (i + 1));
                                outletCharts[i].setDescription(desc);
                                outletCharts[i].invalidate();
                            }

                        }

                    } catch (Exception e) {
                        Log.e("GRAPH_PARSE_ERROR", "Parsing error", e);
                    }
                },
                error -> Log.e("GRAPH_FETCH_ERROR", "Fetch failed", error)
        );

        queue.add(request);
    }
}
