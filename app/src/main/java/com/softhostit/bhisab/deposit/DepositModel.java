package com.softhostit.bhisab.deposit;

public class DepositModel {
    private int id;
    private int time;
    private String account;
    private int date;
    private int amount;
    private int user_id;
    private int payer;
    private String in_cat;
    private String des;
    private String domain;


    public DepositModel() {
    }

    public DepositModel(int id, int time, String account, int date, int amount, int user_id, int payer, String in_cat, String des, String domain) {
        this.id = id;
        this.time = time;
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.user_id = user_id;
        this.payer = payer;
        this.in_cat = in_cat;
        this.des = des;
        this.domain = domain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPayer() {
        return payer;
    }

    public void setPayer(int payer) {
        this.payer = payer;
    }

    public String getIn_cat() {
        return in_cat;
    }

    public void setIn_cat(String in_cat) {
        this.in_cat = in_cat;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
