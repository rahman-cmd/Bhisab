package com.softhostit.bhisab.Login;

public class User {

    private int id;
    private String domain;
    private String username;
    private String password;

    public User(int id, String domain, String username, String password) {
        this.id = id;
        this.domain = domain;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
