package com.softhostit.bhisab.Dashboard;

public class DashboardModel {
    private String domain;
    private String username;
    private String today_sales;
    private String today_expense;
    private String today_receive;
    private String today_balance;

    public DashboardModel(String domain, String username, String today_sales, String today_expense, String today_receive, String today_balance) {
        this.domain = domain;
        this.username = username;
        this.today_sales = today_sales;
        this.today_expense = today_expense;
        this.today_receive = today_receive;
        this.today_balance = today_balance;
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

    public String getToday_sales() {
        return today_sales;
    }

    public void setToday_sales(String today_sales) {
        this.today_sales = today_sales;
    }

    public String getToday_expense() {
        return today_expense;
    }

    public void setToday_expense(String today_expense) {
        this.today_expense = today_expense;
    }

    public String getToday_receive() {
        return today_receive;
    }

    public void setToday_receive(String today_receive) {
        this.today_receive = today_receive;
    }

    public String getToday_balance() {
        return today_balance;
    }

    public void setToday_balance(String today_balance) {
        this.today_balance = today_balance;
    }
}
