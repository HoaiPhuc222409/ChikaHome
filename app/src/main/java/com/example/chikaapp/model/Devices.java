package com.example.chikaapp.model;

public class Devices {
    private String id;
    private String name;
    private String logo;
    private String roomId;
    private String buttonId;

    public Devices(String id, String name, String logo, String roomid, String buttonId) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.roomId = roomid;
        this.buttonId = buttonId;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }
}
