package com.example.eventcounter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private EditText etButton1Name, etButton2Name, etButton3Name, etMaxEvents;
    private Button btnSave;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Setup action bar with up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        initializeViews();
        loadSettings();
        setupSaveButton();
    }

    private void initializeViews() {
        etButton1Name = findViewById(R.id.etButton1Name);
        etButton2Name = findViewById(R.id.etButton2Name);
        etButton3Name = findViewById(R.id.etButton3Name);
        etMaxEvents = findViewById(R.id.etMaxEvents);
        btnSave = findViewById(R.id.btnSave);
    }

    private void loadSettings() {
        Settings settings = sharedPreferenceHelper.getSettings();

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

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> saveSettings());
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

        switchToDisplayMode();
        Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Button names must contain only letters and spaces", Toast.LENGTH_SHORT).show();
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
                switchToDisplayMode();
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
        btnSave.setVisibility(Button.VISIBLE);
        invalidateOptionsMenu();
    }

    private void switchToDisplayMode() {
        isEditMode = false;
        etButton1Name.setEnabled(false);
        etButton2Name.setEnabled(false);
        etButton3Name.setEnabled(false);
        etMaxEvents.setEnabled(false);
        btnSave.setVisibility(Button.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.action_edit);
        editItem.setTitle(isEditMode ? "Cancel" : "Edit");
        return super.onPrepareOptionsMenu(menu);
    }
}