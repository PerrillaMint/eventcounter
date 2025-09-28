package com.example.eventcounter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private TextView tvEvent1Count, tvEvent2Count, tvEvent3Count, tvTotalEvents;
    private ListView lvEventHistory;
    private Settings settings;
    private boolean showEventNames = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        // Setup action bar with up navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        settings = sharedPreferenceHelper.getSettings();

        initializeViews();
        loadData();
    }

    private void initializeViews() {
        tvEvent1Count = findViewById(R.id.tvEvent1Count);
        tvEvent2Count = findViewById(R.id.tvEvent2Count);
        tvEvent3Count = findViewById(R.id.tvEvent3Count);
        tvTotalEvents = findViewById(R.id.tvTotalEvents);
        lvEventHistory = findViewById(R.id.lvEventHistory);
    }

    private void loadData() {
        updateCountDisplays();
        updateEventHistory();
    }

    private void updateCountDisplays() {
        int count1 = sharedPreferenceHelper.getCounter(1);
        int count2 = sharedPreferenceHelper.getCounter(2);
        int count3 = sharedPreferenceHelper.getCounter(3);
        int totalCount = sharedPreferenceHelper.getTotalCount();

        String event1Name = showEventNames ? settings.getButton1Name() : "Counter 1";
        String event2Name = showEventNames ? settings.getButton2Name() : "Counter 2";
        String event3Name = showEventNames ? settings.getButton3Name() : "Counter 3";

        tvEvent1Count.setText(event1Name + ": " + count1 + " events");
        tvEvent2Count.setText(event2Name + ": " + count2 + " events");
        tvEvent3Count.setText(event3Name + ": " + count3 + " events");
        tvTotalEvents.setText("Total events: " + totalCount);
    }

    private void updateEventHistory() {
        String historyString = sharedPreferenceHelper.getEventHistory();
        List<String> historyList = new ArrayList<>();

        if (!historyString.isEmpty()) {
            String[] events = historyString.split(",");
            for (String event : events) {
                try {
                    int eventNumber = Integer.parseInt(event.trim());
                    String eventName = getEventName(eventNumber);
                    historyList.add(eventName);
                } catch (NumberFormatException e) {
                    // Skip invalid entries
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, historyList);
        lvEventHistory.setAdapter(adapter);
    }

    private String getEventName(int eventNumber) {
        if (showEventNames) {
            switch (eventNumber) {
                case 1: return settings.getButton1Name();
                case 2: return settings.getButton2Name();
                case 3: return settings.getButton3Name();
                default: return "Unknown Event";
            }
        } else {
            return String.valueOf(eventNumber);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_names) {
            showEventNames = !showEventNames;
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}