package com.example.chikaapp.request;

public class EditInfoUserRequest {
    private String phone;
    private String email;
    private String birthday;
    private String address;

    public EditInfoUserRequest(String phone, String email, String birthday, String address) {
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
