package com.seungwoodev.project2;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

public interface RetrofitInterface {
    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("/product")
    Call<ProductResult> getProduct(@Body HashMap<String, String> map);

    @POST("/addCart")
    Call<Void> addCart (@Body HashMap<String, String> map);

    @POST("/checkUser")
    Call<Void> checkUser (@Body HashMap<String, String> map);

    @POST("/basket")
    Call<ProductResult> getBasket(@Body HashMap<String, String> map);

    @GET("/category_main")
    Call<CategoryResult> getMainCategory();

    @POST("/category_sub")
    Call<CategoryResult> getSubCategory(@Body HashMap<String, String> map);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @POST("/product_name")
    Call<SimpleProductResult> getSimpleProduct(@Body HashMap<String, String> map);

    @GET("/product_best")
    Call<AllProductResult> getAllProduct();

    @POST("/image")
    @Streaming
    Call<ResponseBody> getImage(@Body HashMap<String, String> map);

    @POST("/payment")
    Call<Void> buyProduct(@Body HashMap<String, String> map);

    @POST("/cash")
    Call<UserInfoResult> getCash(@Body HashMap<String, String> map);

}
