package com.softhostit.bhisab.register;

public class RegisterModel {
    private String mobileNumber;
    private String username;
    private String company_name;
    private String password;

    public RegisterModel(String mobileNumber, String username, String company_name, String password) {
        this.mobileNumber = mobileNumber;
        this.username = username;
        this.company_name = company_name;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
