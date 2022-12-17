package com.softhostit.bhisab.supplier;

public class SupplierModel {
    private String name;
    private String phone;
    private String address;
    private String cname;
    private String group;
    private int id;

    public SupplierModel() {
    }

    public SupplierModel(String name, String phone, String address, String cname, String group, int id) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.cname = cname;
        this.group = group;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
