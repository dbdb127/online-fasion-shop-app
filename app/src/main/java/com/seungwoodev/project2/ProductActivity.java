package com.seungwoodev.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public ArrayList<String> names;
    public ArrayList<Integer> prices;
    public ArrayList<Integer> qty;
    public ArrayList<Bitmap> mImages;
    private ProductAdapter adapter;
    private String subCategory;
    private FloatingActionButton button;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        button = findViewById(R.id.fab);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        subCategory = intent.getStringExtra("subCategory");

        mRecyclerView = findViewById(R.id.recyclerview);

        names = new ArrayList<>();
        prices = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();
        mImages = new ArrayList<Bitmap>();

        //get titles, prices, qty from database
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("subCategory", subCategory);
        Call<ProductResult> call = retrofitInterface.getProduct(map);

        call.enqueue(new Callback<ProductResult>(){
            @Override
            public void onResponse(Call<ProductResult> call, retrofit2.Response<ProductResult> response) {
                ProductResult result = response.body();
                if(result.getCode()==200){
                    names = result.getName();   //ArrayList
                    prices = result.getPrice();
                    qty = result.getQty();

//                    Log.d("image0","22");
                    for(int i=0;i<names.size();i++){
                        final int j=i;
                        HashMap<String, String> map = new HashMap<>();

                        map.put("name", names.get(i));
//                        Log.d("image1",names.get(i));
                        Call<ResponseBody> callImage = retrofitInterface.getImage(map);

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        callImage.enqueue(new Callback<ResponseBody>(){
                            @Override
                            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                InputStream is = response.body().byteStream();
//                                URL url = is.to
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                mImages.add(bitmap);
                                if(j == names.size()-1) {
                                    adapter = new ProductAdapter(ProductActivity.this, names, prices, qty, mImages);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductActivity.this, 1, GridLayoutManager.VERTICAL, false);
                                    mRecyclerView.setAdapter(adapter);
                                    mRecyclerView.setLayoutManager(gridLayoutManager);
                                    mRecyclerView.setHasFixedSize(true);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t){
                                Log.d("bitmapfail", "String.valueOf(bitmap)");
                                Toast.makeText(ProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }else if(result.getCode()==404){
                    Toast.makeText(ProductActivity.this.getApplicationContext(),"No Products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(ProductActivity.this.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}