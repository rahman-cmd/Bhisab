package com.softhostit.bhisab;

public class Constant {

    public Constant() {

    }

    //For retrofit base url must end with /
    public static final String BASE_URL = "https://dev.bhisab.com/php/mapi/";
    public static final String URL_LOGIN = BASE_URL + "auth.php/";
    public static final String URL_DASHBOARD = BASE_URL + "/dashboard/daily.php/";
    public static final String URL_REGISTER = BASE_URL + "newuser.php/";
    public static final String URL_SEARCH = BASE_URL + "product/barcode_search.php/";
    public static final String URL_ALL_PRODUCT = BASE_URL + "product/list.php/";
    public static final String CATEGORY_ITEM = BASE_URL + "deposit/category.php";
    public static final String CATEGORY_CREATE = BASE_URL + "deposit/cre_cat.php";
    public static final String CLIENT_LIST = BASE_URL + "client/list.php";
    public static final String CLIENT_GROUP = BASE_URL + "client/cre_crient_group.php";
    public static final String URL_ADD_CUSTOMER = BASE_URL + "client/client_add.php";
    public static final String customer_group = BASE_URL + "client/group.php";
    public static final String DEPOSIT_LIST = BASE_URL + "deposit/list.php";

    //For retrofit base url must end with /
//    public static final String PRODUCT_IMAGE_URL = "http://bhishab.palashhossain.com/product_images/";

    //We will use this to store the user token number into shared preference
    public static final String SHARED_PREF_NAME = "com.softhostit.bhisab"; //pcakage name+ id

    public static final String SUCCESS = "success";

    public static final String API_KEY = "apiKey";
    public static final String KEY_DOMAIN = "domain";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    public static final String SP_DOMAIN = "domain";
    public static final String SP_USERNAME = "username";
    public static final String SP_PASSWORD = "password";


    public static final String TAX = "tax";
    public static final String DISCOUNT = "discount";

    // table name
    public static String productCart = "product_cart";

    // table column
    public static final String CART_ID = "cart_id";

    // product list
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_SELL_PRICE = "sell_price";
    public static final String PRODUCT_BUY_PRICE= "buy_price";
    public static final String PRODUCT_STOCK = "openstock";
    public static final String PRODUCT_IMAGE = "images";
    public static final String PRODUCT_BARCODE = "barcode";
    public static final String PRODUCT_DOMAIN = "domain";


}
