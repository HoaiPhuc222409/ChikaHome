package com.example.chikaapp.model;

import java.io.Serializable;

public class Image implements Serializable {

    public String name_image;
    public int id;

    public Image() {
    }

    public Image(String name_image, int id) {
        this.name_image = name_image;

        this.id=id;

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
