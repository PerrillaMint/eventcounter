package com.example.eventcounter;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private static final String PREF_NAME = "EventCounterPrefs";
    private static final String KEY_BUTTON1_NAME = "button1_name";
    private static final String KEY_BUTTON2_NAME = "button2_name";
    private static final String KEY_BUTTON3_NAME = "button3_name";
    private static final String KEY_MAX_EVENTS = "max_events";
    private static final String KEY_BUTTON1_COUNT = "button1_count";
    private static final String KEY_BUTTON2_COUNT = "button2_count";
    private static final String KEY_BUTTON3_COUNT = "button3_count";
    private static final String KEY_EVENT_HISTORY = "event_history";

    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Settings methods
    public void saveSettings(Settings settings) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_BUTTON1_NAME, settings.getButton1Name());
        editor.putString(KEY_BUTTON2_NAME, settings.getButton2Name());
        editor.putString(KEY_BUTTON3_NAME, settings.getButton3Name());
        editor.putInt(KEY_MAX_EVENTS, settings.getMaxEvents());
        editor.apply();
    }

    public Settings getSettings() {
        String button1Name = sharedPreferences.getString(KEY_BUTTON1_NAME, null);
        String button2Name = sharedPreferences.getString(KEY_BUTTON2_NAME, null);
        String button3Name = sharedPreferences.getString(KEY_BUTTON3_NAME, null);
        int maxEvents = sharedPreferences.getInt(KEY_MAX_EVENTS, 100);

        return new Settings(button1Name, button2Name, button3Name, maxEvents);
    }

    public boolean hasSettings() {
        return sharedPreferences.getString(KEY_BUTTON1_NAME, null) != null;
    }

    // Counter methods
    public void incrementCounter(int buttonNumber) {
        String key = getCounterKey(buttonNumber);
        int currentCount = sharedPreferences.getInt(key, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, currentCount + 1);

        // Add to event history
        String history = sharedPreferences.getString(KEY_EVENT_HISTORY, "");
        if (!history.isEmpty()) {
            history += ",";
        }
        history += buttonNumber;
        editor.putString(KEY_EVENT_HISTORY, history);

        editor.apply();
    }

    public int getCounter(int buttonNumber) {
        String key = getCounterKey(buttonNumber);
        return sharedPreferences.getInt(key, 0);
    }

    public int getTotalCount() {
        return getCounter(1) + getCounter(2) + getCounter(3);
    }

    public String getEventHistory() {
        return sharedPreferences.getString(KEY_EVENT_HISTORY, "");
    }

    private String getCounterKey(int buttonNumber) {
        switch (buttonNumber) {
            case 1: return KEY_BUTTON1_COUNT;
            case 2: return KEY_BUTTON2_COUNT;
            case 3: return KEY_BUTTON3_COUNT;
            default: throw new IllegalArgumentException("Invalid button number");
        }
    }
}