package com.example.chikaapp.model;

import java.io.Serializable;

public class DeviceScript implements Serializable {
    private String id;
    private String name;
    private String topic;
    private String type;
    private int switchButton;
    private boolean state;

    public DeviceScript(String id, String name, String topic, String type, int switchButton) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.type = type;
        this.switchButton = switchButton;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSwitchButton() {
        return switchButton;
    }

    public void setSwitchButton(int switchButton) {
        this.switchButton = switchButton;
    }
}
