package com.example.chikaapp.model;

public class DeviceActive {
    private String id;
    private String deviceId;
    private String name;
    private String type;
    private String topic;
    private boolean state;
    private int switchButton;
    private String scriptId;

    public DeviceActive(String id, String deviceId, String name, String type, String topic, boolean state, int switchButton, String scriptId) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.type = type;
        this.topic = topic;
        this.state = state;
        this.switchButton = switchButton;
        this.scriptId = scriptId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getSwitchButton() {
        return switchButton;
    }

    public void setSwitchButton(int switchButton) {
        this.switchButton = switchButton;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }
}
