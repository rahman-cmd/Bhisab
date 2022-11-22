package com.softhostit.bhisab.networking;

import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.Login.LoginRespons;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("auth.php/")
    Call<LoginRespons>  loginUser(@Body User user);

}


