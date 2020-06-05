package com.example.chikaapp.request;

public class CreateDeviceRequest {
    private String logo;
    private String name;
    private String roomId;
    private int switchButton;
    private String topic;
    private String type;

    public CreateDeviceRequest(String logo, String name, String roomId, int switchButton, String topic, String type) {
        this.logo = logo;
        this.name = name;
        this.roomId = roomId;
        this.switchButton = switchButton;
        this.topic = topic;
        this.type = type;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getSwitchButton() {
        return switchButton;
    }

    public void setSwitchButton(int switchButton) {
        this.switchButton = switchButton;
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
}
