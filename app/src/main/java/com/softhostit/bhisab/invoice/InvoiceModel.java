package com.softhostit.bhisab.invoice;


import com.google.gson.annotations.SerializedName;

public class InvoiceModel {
    private String currency;
    private String type;
    private int invoice_id_custom;
    private int invoice_id;
    private String date_issue;
    private int client_id;
    private ClientDetailsModel client_details;
    private int discount;
    private String discount_type;
    private int vat;
    private String vat_type;
    private int total;
    private int total_payment;
    private int due;
    private int due_collect_date;

    public InvoiceModel() {
    }

    public InvoiceModel(String currency, String type, int invoice_id_custom, int invoice_id, String date_issue, int client_id, ClientDetailsModel client_details, int discount, String discount_type, int vat, String vat_type, int total, int total_payment, int due, int due_collect_date) {
        this.currency = currency;
        this.type = type;
        this.invoice_id_custom = invoice_id_custom;
        this.invoice_id = invoice_id;
        this.date_issue = date_issue;
        this.client_id = client_id;
        this.client_details = client_details;
        this.discount = discount;
        this.discount_type = discount_type;
        this.vat = vat;
        this.vat_type = vat_type;
        this.total = total;
        this.total_payment = total_payment;
        this.due = due;
        this.due_collect_date = due_collect_date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInvoice_id_custom() {
        return invoice_id_custom;
    }

    public void setInvoice_id_custom(int invoice_id_custom) {
        this.invoice_id_custom = invoice_id_custom;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getDate_issue() {
        return date_issue;
    }

    public void setDate_issue(String date_issue) {
        this.date_issue = date_issue;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public ClientDetailsModel getClient_details() {
        return client_details;
    }

    public void setClient_details(ClientDetailsModel client_details) {
        this.client_details = client_details;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public String getVat_type() {
        return vat_type;
    }

    public void setVat_type(String vat_type) {
        this.vat_type = vat_type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(int total_payment) {
        this.total_payment = total_payment;
    }

    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }

    public int getDue_collect_date() {
        return due_collect_date;
    }

    public void setDue_collect_date(int due_collect_date) {
        this.due_collect_date = due_collect_date;
    }
}
