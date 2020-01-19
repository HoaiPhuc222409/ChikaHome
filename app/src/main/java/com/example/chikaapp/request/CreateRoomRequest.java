package com.example.chikaapp.request;

public class CreateRoomRequest {
    private int logo;
    private String name;

    public CreateRoomRequest(int logo, String name) {
        this.logo = logo;
        this.name = name;
    }

    public CreateRoomRequest() {
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
