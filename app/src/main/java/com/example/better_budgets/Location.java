package com.example.better_budgets;

public class Location {

    private String name;
    private String address;
    private float latitude;
    private float longitude;

    public Location(String name, String address, float latitude, float longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
