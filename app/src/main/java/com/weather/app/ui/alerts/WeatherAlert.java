package com.weather.app.ui.alerts;

public class WeatherAlert {
    private String id;
    private String title;
    private String description;
    private String severity; // warning, caution, info
    private String validUntil;

    public WeatherAlert(String id, String title, String description, String severity, String validUntil) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.validUntil = validUntil;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }
    public String getValidUntil() { return validUntil; }
}
