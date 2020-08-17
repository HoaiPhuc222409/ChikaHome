package com.example.chikaapp.model;

import java.util.ArrayList;

public class DeviceScript {
    private String roomName;
    private ArrayList<DeviceActive> devices;

    public DeviceScript(String roomName, ArrayList<DeviceActive> devices) {
        this.roomName = roomName;
        this.devices = devices;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<DeviceActive> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<DeviceActive> devices) {
        this.devices = devices;
    }
}
