package com.seungwoodev.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Bitmap> mImages;
    private BasketAdapter adapter;
    private TextView totalPrice;
    private Button button;
    public final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);


        mRecyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.text_totalPrice);
        button = findViewById(R.id.btn_buy);

        names = new ArrayList<>();
        prices = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();
        mImages = new ArrayList<>();


        Retrofit retrofit;
        RetrofitInterface retrofitInterface;
        String BASE_URL = "http:192.249.18.167:80";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //request buy query
                HashMap<String, String> map = new HashMap<>();

                //total price 계산하기
                int tmp=0;
                for(int i=0;i<names.size();i++){
                    tmp = tmp + prices.get(i)*qty.get(i);
                }
                map.put("price", String.valueOf(tmp));
                map.put("email", MainActivity_Tab.getUser());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                map.put("time", String.valueOf(sdf.format(timestamp)));

                Call<Void> call = retrofitInterface.buyProduct(map);

                call.enqueue(new Callback<Void>(){
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if(response.code()==200){
                            names = new ArrayList<>();
                            prices = new ArrayList<Integer>();
                            qty = new ArrayList<Integer>();
                            mImages = new ArrayList<>();

                            totalPrice.setText("0 won");

                            adapter = new BasketAdapter(BasketActivity.this, names, prices, qty, mImages);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(BasketActivity.this, 1, GridLayoutManager.VERTICAL, false);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setLayoutManager(gridLayoutManager);
                            mRecyclerView.setHasFixedSize(true);
                            Toast.makeText(BasketActivity.this,"Order has been completed", Toast.LENGTH_SHORT).show();
                        }else if(response.code()==404){
                            Toast.makeText(BasketActivity.this,"Out of Cash", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t){
                        Log.d("failed", "connection "+call);
                        Toast.makeText(BasketActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //db에서 basket 가져오기
        HashMap<String, String> map = new HashMap<>();

        map.put("email", MainActivity_Tab.getUser());

        Call<ProductResult> call = retrofitInterface.getBasket(map);

        call.enqueue(new Callback<ProductResult>(){
            @Override
            public void onResponse(Call<ProductResult> call, retrofit2.Response<ProductResult> response) {
                if(response.code()==200){
                    ProductResult result = response.body();
                    names = result.getName();   //ArrayList
                    prices = result.getPrice();
                    qty = result.getQty();

                    //total price 계산하기
                    int tmp=0;
                    for(int i=0;i<names.size();i++){
                        tmp = tmp + prices.get(i)*qty.get(i);
                    }
                    totalPrice.setText(tmp+" won");

                    //수정 이미지 불러오기
                    for(int i=0;i<names.size();i++){
                        final int j=i;
                        HashMap<String, String> map = new HashMap<>();

                        map.put("name", names.get(i));
                        Call<ResponseBody> callImage = retrofitInterface.getImage(map);

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        callImage.enqueue(new Callback<ResponseBody>(){
                            @Override
                            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                InputStream is = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                mImages.add(bitmap);
                                if(j == names.size()-1) {
                                    adapter = new BasketAdapter(BasketActivity.this, names, prices, qty, mImages);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(BasketActivity.this, 1, GridLayoutManager.VERTICAL, false);
                                    mRecyclerView.setAdapter(adapter);
                                    mRecyclerView.setLayoutManager(gridLayoutManager);
                                    mRecyclerView.setHasFixedSize(true);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t){
                                Log.d("bitmapfail", "String.valueOf(bitmap)");
                                Toast.makeText(BasketActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }else if(response.code()==404){
                    Toast.makeText(BasketActivity.this,"No Products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(BasketActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}