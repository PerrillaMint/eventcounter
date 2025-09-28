package com.example.eventcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private LinearLayout btnEvent1Container, btnEvent2Container, btnEvent3Container;
    private TextView tvTotalCount, tvEvent1Name, tvEvent2Name, tvEvent3Name;
    private TextView tvEvent1Count, tvEvent2Count, tvEvent3Count;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        initializeViews();
        setupClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        settings = sharedPreferenceHelper.getSettings();

        if (!sharedPreferenceHelper.hasSettings()) {
            goToSettingsActivity();
        } else {
            updateUI();
        }
    }

    private void initializeViews() {
        btnEvent1Container = findViewById(R.id.btnEvent1Container);
        btnEvent2Container = findViewById(R.id.btnEvent2Container);
        btnEvent3Container = findViewById(R.id.btnEvent3Container);

        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvEvent1Name = findViewById(R.id.tvEvent1Name);
        tvEvent2Name = findViewById(R.id.tvEvent2Name);
        tvEvent3Name = findViewById(R.id.tvEvent3Name);
        tvEvent1Count = findViewById(R.id.tvEvent1Count);
        tvEvent2Count = findViewById(R.id.tvEvent2Count);
        tvEvent3Count = findViewById(R.id.tvEvent3Count);
    }

    private void setupClickListeners() {
        MaterialButton btnSettings = findViewById(R.id.btnSettings);
        MaterialButton btnShowCounts = findViewById(R.id.btnShowCounts);

        btnSettings.setOnClickListener(v -> goToSettingsActivity());
        btnShowCounts.setOnClickListener(v -> goToDataActivity());

        btnEvent1Container.setOnClickListener(v -> incrementCounter(1));
        btnEvent2Container.setOnClickListener(v -> incrementCounter(2));
        btnEvent3Container.setOnClickListener(v -> incrementCounter(3));
    }

    private void incrementCounter(int buttonNumber) {
        // Add visual feedback
        addRippleEffect(buttonNumber);

        sharedPreferenceHelper.incrementCounter(buttonNumber);
        updateUI();

        // Show feedback snackbar
        String eventName = getEventName(buttonNumber);
        Snackbar.make(findViewById(android.R.id.content),
                        eventName + " counted!",
                        Snackbar.LENGTH_SHORT)
                .setAnchorView(findViewById(R.id.btnShowCounts))
                .show();
    }

    private void addRippleEffect(int buttonNumber) {
        View container = null;
        switch (buttonNumber) {
            case 1: container = btnEvent1Container; break;
            case 2: container = btnEvent2Container; break;
            case 3: container = btnEvent3Container; break;
        }

        if (container != null) {
            container.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        container.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100);
                    });
        }
    }

    private void updateUI() {
        updateButtonTexts();
        updateCounts();
        updateTotalCount();
    }

    private void updateButtonTexts() {
        if (settings != null) {
            tvEvent1Name.setText(settings.getButton1Name());
            tvEvent2Name.setText(settings.getButton2Name());
            tvEvent3Name.setText(settings.getButton3Name());
        }
    }

    private void updateCounts() {
        int count1 = sharedPreferenceHelper.getCounter(1);
        int count2 = sharedPreferenceHelper.getCounter(2);
        int count3 = sharedPreferenceHelper.getCounter(3);

        tvEvent1Count.setText(count1 + " events");
        tvEvent2Count.setText(count2 + " events");
        tvEvent3Count.setText(count3 + " events");
    }

    private void updateTotalCount() {
        int totalCount = sharedPreferenceHelper.getTotalCount();
        tvTotalCount.setText(String.valueOf(totalCount));
    }

    private String getEventName(int buttonNumber) {
        if (settings == null) return "Event " + buttonNumber;

        switch (buttonNumber) {
            case 1: return settings.getButton1Name();
            case 2: return settings.getButton2Name();
            case 3: return settings.getButton3Name();
            default: return "Event " + buttonNumber;
        }
    }

    private void goToSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void goToDataActivity() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }
}