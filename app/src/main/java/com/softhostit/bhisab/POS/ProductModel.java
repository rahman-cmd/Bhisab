package com.softhostit.bhisab.POS;

public class ProductModel {
    private int id;
    private String name;
    private int sell_price;
    private int buy_price;
    private String openstock;
    private String images;
    private String barcode;

    public ProductModel() {}

    public ProductModel(int id, String name, int sell_price, int buy_price, String openstock, String images, String barcode) {
        this.id = id;
        this.name = name;
        this.sell_price = sell_price;
        this.buy_price = buy_price;
        this.openstock = openstock;
        this.images = images;
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSell_price() {
        return sell_price;
    }

    public void setSell_price(int sell_price) {
        this.sell_price = sell_price;
    }

    public int getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(int buy_price) {
        this.buy_price = buy_price;
    }

    public String getOpenstock() {
        return openstock;
    }

    public void setOpenstock(String openstock) {
        this.openstock = openstock;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}


