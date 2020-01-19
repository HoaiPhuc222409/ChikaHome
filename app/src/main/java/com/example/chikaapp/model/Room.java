package com.example.chikaapp.model;

public class Room {
    private int logo;
    private String name_Room;
    private int userId;
    private String id;

    public Room(int logo, String name_Room, int userId, String id) {
        this.logo = logo;
        this.name_Room = name_Room;
        this.userId = userId;
        this.id = id;
    }

    public Room(int logo, String name_Room) {
        this.logo = logo;
        this.name_Room = name_Room;
    }

    public Room() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getName_Room() {
        return name_Room;
    }

    public void setName_Room(String name_Room) {
        this.name_Room = name_Room;
    }
}
