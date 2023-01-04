package com.softhostit.bhisab.invoice;

public class ClientDetailsModel {
    private String name;
    private String cname;
    private String phone1;
    private int pre_due;
    private String address;

    public ClientDetailsModel() {
    }

    public ClientDetailsModel(String name, String cname, String phone1, int pre_due, String address) {
        this.name = name;
        this.cname = cname;
        this.phone1 = phone1;
        this.pre_due = pre_due;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPre_due() {
        return pre_due;
    }

    public void setPre_due(int pre_due) {
        this.pre_due = pre_due;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
