package com.softhostit.bhisab.expense;

public class ExpenseModel {
    private int id;
    private int account;
    private int date;
    private String des;
    private int in_cat;
    private int amount;
    private int user_id;

    public ExpenseModel() {
    }

    public ExpenseModel(int id, int account, int date, String des, int in_cat, int amount, int user_id) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.des = des;
        this.in_cat = in_cat;
        this.amount = amount;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getIn_cat() {
        return in_cat;
    }

    public void setIn_cat(int in_cat) {
        this.in_cat = in_cat;
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
}
