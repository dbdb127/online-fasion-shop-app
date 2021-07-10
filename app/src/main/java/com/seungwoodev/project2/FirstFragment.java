package com.seungwoodev.project2;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Integer> mImages;
    private FirstAdapter adapter;

    private Retrofit retrofit;
    private ProductInterface productInterface;
    private String BASE_URL = "http:172.10.18.176:80";

    public FirstFragment() {
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
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview);

        names = new ArrayList<>();
        prices = new ArrayList<Integer>();
        mImages = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();

        adapter = new FirstAdapter(getActivity().getApplicationContext(), names, prices, mImages);

        //product list
        mImages.add(R.drawable.ic_baseline_checkroom_24);
        names.add("temp");
        prices.add(12345);

        //get titles, prices, qty from database
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        productInterface = retrofit.create(ProductInterface.class);
        Call<ProductResult> call = productInterface.getProduct();

        call.enqueue(new Callback<ProductResult>(){
            @Override
            public void onResponse(Call<ProductResult> call, retrofit2.Response<ProductResult> response) {
                ProductResult result = response.body();
                if(result.getCode()==200){
                    names = result.getName();
                    prices = result.getPrice();
                    qty = result.getQty();
                    Log.d("GET product list", names+"");

                }else if(result.getCode()==404){
                    Toast.makeText(getActivity().getApplicationContext(),"No Products", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t){
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }
}