package com.example.eventcounter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private EditText etButton1Name, etButton2Name, etButton3Name, etMaxEvents;
    private Button btnSave, btnCancel;
    private LinearLayout buttonContainer;
    private boolean isEditMode = false;
    private Settings originalSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Setup action bar with up navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        initializeViews();
        loadSettings();
        setupButtonListeners();
    }

    private void initializeViews() {
        etButton1Name = findViewById(R.id.etButton1Name);
        etButton2Name = findViewById(R.id.etButton2Name);
        etButton3Name = findViewById(R.id.etButton3Name);
        etMaxEvents = findViewById(R.id.etMaxEvents);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        buttonContainer = findViewById(R.id.buttonContainer);
    }

    private void loadSettings() {
        Settings settings = sharedPreferenceHelper.getSettings();
        originalSettings = new Settings(
                settings.getButton1Name(),
                settings.getButton2Name(),
                settings.getButton3Name(),
                settings.getMaxEvents()
        );

        if (!sharedPreferenceHelper.hasSettings()) {
            // Auto-switch to edit mode if no settings exist
            switchToEditMode();
        } else {
            etButton1Name.setText(settings.getButton1Name());
            etButton2Name.setText(settings.getButton2Name());
            etButton3Name.setText(settings.getButton3Name());
            etMaxEvents.setText(String.valueOf(settings.getMaxEvents()));
        }
    }

    private void setupButtonListeners() {
        btnSave.setOnClickListener(v -> saveSettings());
        btnCancel.setOnClickListener(v -> cancelEdit());
    }

    private void saveSettings() {
        String button1Name = etButton1Name.getText().toString().trim();
        String button2Name = etButton2Name.getText().toString().trim();
        String button3Name = etButton3Name.getText().toString().trim();
        String maxEventsStr = etMaxEvents.getText().toString().trim();

        if (!validateInputs(button1Name, button2Name, button3Name, maxEventsStr)) {
            return;
        }

        int maxEvents = Integer.parseInt(maxEventsStr);
        Settings settings = new Settings(button1Name, button2Name, button3Name, maxEvents);
        sharedPreferenceHelper.saveSettings(settings);

        // Update original settings for cancel functionality
        originalSettings = new Settings(button1Name, button2Name, button3Name, maxEvents);

        switchToDisplayMode();
        Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void cancelEdit() {
        // Restore original values
        if (originalSettings != null) {
            etButton1Name.setText(originalSettings.getButton1Name());
            etButton2Name.setText(originalSettings.getButton2Name());
            etButton3Name.setText(originalSettings.getButton3Name());
            etMaxEvents.setText(String.valueOf(originalSettings.getMaxEvents()));
        }
        switchToDisplayMode();
        Toast.makeText(this, "Changes cancelled", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs(String button1Name, String button2Name,
                                   String button3Name, String maxEventsStr) {
        Settings settings = new Settings();

        if (button1Name.isEmpty() || button2Name.isEmpty() || button3Name.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_field), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!settings.isValidButtonName(button1Name) ||
                !settings.isValidButtonName(button2Name) ||
                !settings.isValidButtonName(button3Name)) {
            Toast.makeText(this, "Button names must contain only letters and spaces (max 20 characters)", Toast.LENGTH_LONG).show();
            return false;
        }

        if (maxEventsStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_field), Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int maxEvents = Integer.parseInt(maxEventsStr);
            if (!settings.isValidMaxEvents(maxEvents)) {
                Toast.makeText(this, getString(R.string.error_invalid_max_events), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (isEditMode) {
                cancelEdit();
            } else {
                switchToEditMode();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchToEditMode() {
        isEditMode = true;
        etButton1Name.setEnabled(true);
        etButton2Name.setEnabled(true);
        etButton3Name.setEnabled(true);
        etMaxEvents.setEnabled(true);
        buttonContainer.setVisibility(LinearLayout.VISIBLE);
        invalidateOptionsMenu();
    }

    private void switchToDisplayMode() {
        isEditMode = false;
        etButton1Name.setEnabled(false);
        etButton2Name.setEnabled(false);
        etButton3Name.setEnabled(false);
        etMaxEvents.setEnabled(false);
        buttonContainer.setVisibility(LinearLayout.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.action_edit);
        editItem.setTitle(isEditMode ? "Cancel" : "Edit");
        return super.onPrepareOptionsMenu(menu);
    }
}