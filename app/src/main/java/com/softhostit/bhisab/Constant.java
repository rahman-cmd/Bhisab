package com.softhostit.bhisab;

public class Constant {

    public Constant() {

    }

    //For retrofit base url must end with /
    public static final String BASE_URL = "https://dev.bhisab.com/php/mapi/";
    public static final String URL_LOGIN = BASE_URL + "auth.php/";
    public static final String URL_DASHBOARD = BASE_URL + "/dashboard/daily.php/";
    public static final String URL_REGISTER = BASE_URL + "newuser.php/";
    public static final String  URL_SEARCH = BASE_URL + "product/barcode_search.php/";
    public static final String URL_ALL_PRODUCT = BASE_URL + "product/list.php/";

    //For retrofit base url must end with /
//    public static final String PRODUCT_IMAGE_URL = "http://bhishab.palashhossain.com/product_images/";

    //We will use this to store the user token number into shared preference
    public static final String SHARED_PREF_NAME = "com.softhostit.bhisab"; //pcakage name+ id

    public static final String SUCCESS="success";

    public static final String API_KEY = "apiKey";
    public static final String KEY_DOMAIN= "domain";
    public static final String KEY_USERNAME= "username";
    public static final String KEY_PASSWORD= "password";

    public static final String SP_DOMAIN = "domain";
    public static final String SP_USERNAME = "username";
    public static final String SP_PASSWORD = "password";



}
