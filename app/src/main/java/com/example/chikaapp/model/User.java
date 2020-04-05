package com.example.chikaapp.model;

public class User {

    private String avatar;

    private String birthday;

    private String address;

    private String phone;

    private String role;

    private String name;

    private String createAt;

    private String email;

    public User(String createAt, String avatar, String name, String birthday, String address, String email, String phone, String role ) {
        this.createAt = createAt;
        this.avatar = avatar;
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
