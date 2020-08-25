package com.example.chikaapp.model;

import java.util.ArrayList;

public class ScriptDevices {
    private String roomName;
    private ArrayList<DeviceScript> devices;

    public ScriptDevices(String roomName, ArrayList<DeviceScript> devicesArrayList) {
        this.roomName = roomName;
        this.devices = devicesArrayList;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<DeviceScript> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<DeviceScript> devices) {
        this.devices = devices;
    }
}
