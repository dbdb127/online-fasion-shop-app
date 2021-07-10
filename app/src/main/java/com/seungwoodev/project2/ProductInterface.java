package com.seungwoodev.project2;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductInterface {
    @GET("/product")
    Call<ProductResult> getProduct();
}
