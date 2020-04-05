package com.example.chikaapp.request;

public class CreateRoomRequest {
    private String logo;
    private String name;

    public CreateRoomRequest(String logo, String name) {
        this.logo = logo;
        this.name = name;
    }

    public CreateRoomRequest() {
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
