package com.example.chikaapp.model;

public class Devices {
    private String createAt;
    private String id;
    private String logo;
    private String name;
    private boolean state;
    private String roomId;
    private String type;
    private String topic;
    private int switchButton;

    public Devices(String createAt, String id, String logo, String name, boolean state, String roomId, String type, String topic, int switchButton) {
        this.createAt = createAt;
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.state = state;
        this.roomId = roomId;
        this.type = type;
        this.topic = topic;
        this.switchButton = switchButton;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public int getSwitchButton() {
        return switchButton;
    }

    public void setSwitchButton(int switchButton) {
        this.switchButton = switchButton;
    }
}
