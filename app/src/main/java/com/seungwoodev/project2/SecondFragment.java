package com.seungwoodev.project2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<Product_Best> bestList;
    private ArrayList<Bitmap> mImages;
    private BestAdapter adapter;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";

    //이미지 뷰 5장
//    ImageView image1, image2, image3, image4, image5;

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_product, container, false);
    }

    public void sort(ArrayList<Product_Best> arg){

        for(int i=0; i<arg.size(); i++){
            Product_Best temp;
            for(int j=0; j<i; j++){
                if(Product_Best.compare(arg.get(j), arg.get(i))){
                    temp = arg.get(i);
                    arg.set(i, arg.get(j));
                    arg.set(j, temp);
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview);

        bestList = new ArrayList<Product_Best>();
        mImages = new ArrayList<Bitmap>();

        //db에서 all products 가져오기
        Retrofit retrofit;
        RetrofitInterface retrofitInterface;
        String BASE_URL = "http:192.249.18.167:80";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<AllProductResult> call = retrofitInterface.getAllProduct();

        call.enqueue(new Callback<AllProductResult>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<AllProductResult> call, retrofit2.Response<AllProductResult> response) {
                if(response.code()==200){
                    AllProductResult result = response.body();
                    bestList = result.getProduct_best_arr();   //ArrayList

                    //sorting
                    sort(bestList);

                    for(int i=0;i<bestList.size();i++){
                        final int j=i;
                        HashMap<String, String> map = new HashMap<>();

                        map.put("name", bestList.get(i).name);
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
                                if(j == bestList.size()-1) {
                                    adapter = new BestAdapter(getActivity().getApplicationContext(), bestList, mImages);

                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);

                                    mRecyclerView.setAdapter(adapter);

                                    mRecyclerView.setLayoutManager(gridLayoutManager);
                                    mRecyclerView.setHasFixedSize(true);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t){
                                Log.d("bitmapfail", "String.valueOf(bitmap)");
                                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }else if(response.code()==404){
                    Toast.makeText(getActivity().getApplicationContext(),"No Products", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AllProductResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}