package com.softhostit.bhisab.POS;

public class CartModel {
    private String id;
    private String name;
    private String sell_price;
    private String CART_ID;

    public CartModel() {
    }

    public CartModel(String id, String name, String sell_price, String CART_ID) {
        this.id = id;
        this.name = name;
        this.sell_price = sell_price;
        this.CART_ID = CART_ID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getCART_ID() {
        return CART_ID;
    }

    public void setCART_ID(String CART_ID) {
        this.CART_ID = CART_ID;
    }
}
