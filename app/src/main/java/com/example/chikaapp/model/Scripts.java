package com.example.chikaapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Scripts implements Serializable {
    private String id;
    private String logo;
    private String name;
    private String time;
    private String days;
    private String userId;
    private ArrayList<DeviceActive> devices;

    public Scripts(String id, String logo, String name, String time, String days, String userId, ArrayList<DeviceActive> devices) {
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.time = time;
        this.days = days;
        this.userId = userId;
        this.devices = devices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<DeviceActive> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<DeviceActive> devices) {
        this.devices = devices;
    }
}
