package com.softhostit.bhisab.model;

import com.google.gson.annotations.SerializedName;

public class Login {


    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    @SerializedName("domain")
    private String domain;

    @SerializedName("username")
    private String username;

    @SerializedName("apiKey")
    private String apiKey;

    @SerializedName("cell")
    private String cell;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("user_type")
    private String userType;



    @SerializedName("password")
    private String password;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    @SerializedName("shop_name")
    private String shopName;

    @SerializedName("shop_address")
    private String shopAddress;

    @SerializedName("shop_email")
    private String shopEmail;


    @SerializedName("shop_contact")
    private String shopContact;

    @SerializedName("tax")
    private String tax;
    @SerializedName("currency_symbol")
    private String currencySymbol;
    @SerializedName("shop_status")
    private String shopStatus;

    @SerializedName("shop_id")
    private String shopId;

    @SerializedName("owner_id")
    private String ownerID;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDomain() {
        return domain;
    }

    public String getUsername() {
        return username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getCell() {
        return cell;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getUserType() {
        return userType;
    }

    public String getPassword() {
        return password;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public String getShopContact() {
        return shopContact;
    }

    public String getTax() {
        return tax;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getShopStatus() {
        return shopStatus;
    }

    public String getShopId() {
        return shopId;
    }

    public String getOwnerID() {
        return ownerID;
    }
}
