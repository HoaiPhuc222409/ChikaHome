package com.example.chikaapp.model;

public class Room {
    private String id;
    private String logo;
    private String name;
    private String createAt;


    public Room(String id, String logo, String name, String createAt) {
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.createAt = createAt;
    }

    public Room() {
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
}
