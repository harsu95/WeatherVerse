package com.weather.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Location search result model.
 * Matches the Android app's LocationResult class.
 */
public class LocationResult {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("latitude")
    private double latitude;
    
    @JsonProperty("longitude")
    private double longitude;
    
    @JsonProperty("country")
    private String country;
    
    @JsonProperty("state")
    private String state;
    
    @JsonProperty("timezone")
    private String timezone;
    
    @JsonProperty("isPrimary")
    private boolean isPrimary;
    
    @JsonProperty("displayOrder")
    private int displayOrder;

    // Default constructor
    public LocationResult() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
    
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
