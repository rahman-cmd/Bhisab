package com.softhostit.bhisab.register;

public class registerModel {
    private String mobileNumber;
    private String username;
    private String password;

    public registerModel(String mobileNumber, String username, String password) {
        this.mobileNumber = mobileNumber;
        this.username = username;
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
