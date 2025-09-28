package com.example.eventcounter;

public class Settings {
    private String button1Name;
    private String button2Name;
    private String button3Name;
    private int maxEvents;

    public Settings() {
        this.button1Name = "";
        this.button2Name = "";
        this.button3Name = "";
        this.maxEvents = 100; // default value
    }

    public Settings(String button1Name, String button2Name, String button3Name, int maxEvents) {
        this.button1Name = button1Name;
        this.button2Name = button2Name;
        this.button3Name = button3Name;
        this.maxEvents = maxEvents;
    }

    // Getters
    public String getButton1Name() { return button1Name; }
    public String getButton2Name() { return button2Name; }
    public String getButton3Name() { return button3Name; }
    public int getMaxEvents() { return maxEvents; }

    // Setters
    public void setButton1Name(String button1Name) { this.button1Name = button1Name; }
    public void setButton2Name(String button2Name) { this.button2Name = button2Name; }
    public void setButton3Name(String button3Name) { this.button3Name = button3Name; }
    public void setMaxEvents(int maxEvents) { this.maxEvents = maxEvents; }

    // Validation methods
    public boolean isValidButtonName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 20
                && name.matches("[a-zA-Z\\s]+");
    }

    public boolean isValidMaxEvents(int max) {
        return max >= 5 && max <= 200;
    }
}