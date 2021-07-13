package com.seungwoodev.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFullActivity extends AppCompatActivity {
    Bitmap image;
    private String strTitle;
    private Integer intPrice, intQty;
    int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);

        Intent intent = getIntent();
        byte[] arr = intent.getByteArrayExtra("image");
        Log.d("imageintent", String.valueOf(arr));
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        ImageView BigImage = findViewById(R.id.imageView);
        BigImage.setImageBitmap(image);

        strTitle = intent.getStringExtra("title");
        intPrice = intent.getIntExtra("price", 10000);

        TextView text_title = findViewById(R.id.text_title);
        TextView text_price = findViewById(R.id.text_price);
        TextView text_qty = findViewById(R.id.text_qty);
        Button btn_minus = findViewById(R.id.button_minus);
        Button btn_plus = findViewById(R.id.button_plus);
        TextView text_num = findViewById(R.id.textView);
        Button cart = findViewById(R.id.button_cart);

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num--;
                if(num<1){
                    Toast.makeText(ImageFullActivity.this,"minumum value is 1", Toast.LENGTH_LONG).show();
                    num=1;
                }
                text_num.setText(Integer.toString(num));
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                if(num>intQty){
                    Toast.makeText(ImageFullActivity.this,"Out of Stock", Toast.LENGTH_LONG).show();
                    num--;
                }
                text_num.setText(Integer.toString(num));
            }
        });

        //intQty db에서 받아오기
        Retrofit retrofit;
        RetrofitInterface retrofitInterface;
        String BASE_URL = "http:192.249.18.167:80";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", strTitle);
        Call<SimpleProductResult> call = retrofitInterface.getSimpleProduct(map);
        call.enqueue(new Callback<SimpleProductResult>(){
            @Override
            public void onResponse(Call<SimpleProductResult> call, retrofit2.Response<SimpleProductResult> response) {
                if(response.code()==200){
                    SimpleProductResult result = response.body();
                    intQty = result.getQty();   //ArrayList

                    text_title.setText(strTitle);
                    text_price.setText(intPrice+"");
                    text_qty.setText(intQty+"");
                    BigImage.setImageBitmap(image);


                }else if(response.code()==404){
                    Toast.makeText(ImageFullActivity.this,"No Products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleProductResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(ImageFullActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*post database information*/

                Retrofit retrofit;
                RetrofitInterface retrofitInterface;
                String BASE_URL = "http:192.249.18.167:80";

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                retrofitInterface = retrofit.create(RetrofitInterface.class);

                HashMap<String, String> map = new HashMap<>();

                map.put("email", MainActivity_Tab.getUser());
                Log.d("email", MainActivity_Tab.getUser());
                map.put("product_name", strTitle);
                map.put("num", Integer.toString(num));
                Call<Void> call = retrofitInterface.addCart(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
//                            Log.d("kyung", "1");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ImageFullActivity.this)
                                    .setMessage("Do you want to see your cart?")
                                    .setTitle("Add to Cart")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //intQty db에서 받아오기
                                            Retrofit retrofit;
                                            RetrofitInterface retrofitInterface;
                                            String BASE_URL = "http:192.249.18.167:80";

                                            retrofit = new Retrofit.Builder()
                                                    .baseUrl(BASE_URL)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            retrofitInterface = retrofit.create(RetrofitInterface.class);
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("name", strTitle);
                                            Call<SimpleProductResult> call = retrofitInterface.getSimpleProduct(map);
                                            call.enqueue(new Callback<SimpleProductResult>(){
                                                @Override
                                                public void onResponse(Call<SimpleProductResult> call, retrofit2.Response<SimpleProductResult> response) {
                                                    if(response.code()==200){
                                                        SimpleProductResult result = response.body();
                                                        intQty = result.getQty();   //ArrayList

                                                        text_title.setText(strTitle);
                                                        text_price.setText(intPrice+"");
                                                        text_qty.setText(intQty+"");
                                                        BigImage.setImageBitmap(image);

                                                    }else if(response.code()==404){
                                                        Toast.makeText(ImageFullActivity.this,"No Products", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<SimpleProductResult> call, Throwable t){
                                                    Log.d("failed", "connection "+call);
                                                    Toast.makeText(ImageFullActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                            //장바구니로 넘어가기
                                            Intent intent = new Intent(ImageFullActivity.this, BasketActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //intQty db에서 받아오기
                                            Retrofit retrofit;
                                            RetrofitInterface retrofitInterface;
                                            String BASE_URL = "http:192.249.18.167:80";

                                            retrofit = new Retrofit.Builder()
                                                    .baseUrl(BASE_URL)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            retrofitInterface = retrofit.create(RetrofitInterface.class);
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("name", strTitle);
                                            Call<SimpleProductResult> call = retrofitInterface.getSimpleProduct(map);
                                            call.enqueue(new Callback<SimpleProductResult>(){
                                                @Override
                                                public void onResponse(Call<SimpleProductResult> call, retrofit2.Response<SimpleProductResult> response) {
                                                    if(response.code()==200){
                                                        SimpleProductResult result = response.body();
                                                        intQty = result.getQty();   //ArrayList

                                                        text_title.setText(strTitle);
                                                        text_price.setText(intPrice+"");
                                                        text_qty.setText(intQty+"");
                                                        BigImage.setImageBitmap(image);

                                                    }else if(response.code()==404){
                                                        Toast.makeText(ImageFullActivity.this,"No Products", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<SimpleProductResult> call, Throwable t){
                                                    Log.d("failed", "connection "+call);
                                                    Toast.makeText(ImageFullActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                            builder.create();
                            builder.show();

                        }else if(response.code() ==404){
                            Toast.makeText(ImageFullActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ImageFullActivity.this, t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}