package com.example.chikaapp.model;

public class Image {
    public int url_image;
    public String name_image;
    public int id;

    public Image() {
    }

    public Image(int id, int img_Room, String roomName) {
        this.url_image = img_Room;
        this.name_image = roomName;
        this.id=id;
    }

    public int getUrl_image() {
        return url_image;
    }

    public void setUrl_image(int url_image) {
        this.url_image = url_image;
    }

    public String getName_image() {
        return name_image;
    }

    public void setName_image(String name_image) {
        this.name_image = name_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
