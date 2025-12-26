package com.weather.app.data.model;

import com.google.gson.annotations.SerializedName;

public class LocationResult {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("country")
    private String country;

    @SerializedName("state")
    private String state;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("isPrimary")
    private boolean isPrimary;

    @SerializedName("displayOrder")
    private int displayOrder;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getTimezone() { return timezone; }
    public boolean isPrimary() { return isPrimary; }
    public int getDisplayOrder() { return displayOrder; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setCountry(String country) { this.country = country; }
    public void setState(String state) { this.state = state; }
}
