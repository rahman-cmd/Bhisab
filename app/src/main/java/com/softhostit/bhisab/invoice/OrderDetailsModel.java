package com.softhostit.bhisab.invoice;

import java.util.List;

public class OrderDetailsModel {
    private int invoice_id;
    private String invoice_label;
    private ClientDetailsModel client_details;
    private List<OrderItemModel> items;
    private int grand_quantity;
    private int subtotal;
    private int transport;
    private int labour_cost;
    private int discount;
    private int vat;
    private String total;
    private String payment;
    private int due;

    public OrderDetailsModel() {
    }

    public OrderDetailsModel(int invoice_id, String invoice_label, ClientDetailsModel client_details, List<OrderItemModel> items, int grand_quantity, int subtotal, int transport, int labour_cost, int discount, int vat, String total, String payment, int due) {
        this.invoice_id = invoice_id;
        this.invoice_label = invoice_label;
        this.client_details = client_details;
        this.items = items;
        this.grand_quantity = grand_quantity;
        this.subtotal = subtotal;
        this.transport = transport;
        this.labour_cost = labour_cost;
        this.discount = discount;
        this.vat = vat;
        this.total = total;
        this.payment = payment;
        this.due = due;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getInvoice_label() {
        return invoice_label;
    }

    public void setInvoice_label(String invoice_label) {
        this.invoice_label = invoice_label;
    }

    public ClientDetailsModel getClient_details() {
        return client_details;
    }

    public void setClient_details(ClientDetailsModel client_details) {
        this.client_details = client_details;
    }

    public List<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.items = items;
    }

    public int getGrand_quantity() {
        return grand_quantity;
    }

    public void setGrand_quantity(int grand_quantity) {
        this.grand_quantity = grand_quantity;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getTransport() {
        return transport;
    }

    public void setTransport(int transport) {
        this.transport = transport;
    }

    public int getLabour_cost() {
        return labour_cost;
    }

    public void setLabour_cost(int labour_cost) {
        this.labour_cost = labour_cost;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }
}
