package com.softhostit.bhisab.coustomer;

public class CustomerModel {
    private int id;
    private String fname;
    private String cname;
    private String phone1;
    private String address;
    private int user_id;
    private String photo;
    private String domain;

    public CustomerModel() {
    }

    public CustomerModel(int id, String fname, String cname, String phone1, String address, int user_id, String photo, String domain) {
        this.id = id;
        this.fname = fname;
        this.cname = cname;
        this.phone1 = phone1;
        this.address = address;
        this.user_id = user_id;
        this.photo = photo;
        this.domain = domain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
