package com.seungwoodev.project2;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @GET("/product/")
    Call<ProductResult> getProduct();

    @POST("/addCart")
    Call<Void> addCart (@Body HashMap<String, String> map);

    @POST("/checkUser")
    Call<Void> checkUser (@Body HashMap<String, String> map);

    @POST("/basket")
    Call<ProductResult> getBasket(@Body HashMap<String, String> map);
}
