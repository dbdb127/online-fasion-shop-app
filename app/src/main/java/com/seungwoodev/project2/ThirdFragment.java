package com.seungwoodev.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThirdFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public List<String> image;
    private ArrayList<String> titles;
    private ArrayList<Integer> prices;
    private ArrayList<Integer> qty;
    private ArrayList<String> timeStamp;
    private ArrayList<Bitmap> mImages;
    private ThirdAdapter adapter;
    private int cash;
    private FloatingActionButton button;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";

    public ThirdFragment() {
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
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = view.findViewById(R.id.fab);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BasketActivity.class);
                startActivity(intent);
            }
        });

        Log.d("onViewCreated:","worked");
        super.onViewCreated(view, savedInstanceState);
        String name = MainActivity_Tab.getUsername();
        TextView name_view = getView().findViewById(R.id.name_txt);
        name_view.setText(name);
        String email = MainActivity_Tab.getUser();
        TextView email_view = getView().findViewById(R.id.email_txt);
        email_view.setText(email);

        ImageView image_view = getView().findViewById(R.id.profile_img);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        if (acct != null) {
            Uri profileUrl = acct.getPhotoUrl();
            Glide.with(getActivity().getApplicationContext()).load(profileUrl).into(image_view);
            Log.d("GoogleProfile", profileUrl+"");
        }
        else if(MainActivity_Tab.getImage() != null){
            Glide.with(getActivity().getApplicationContext())
                    .load(MainActivity_Tab.getImage())
                    .into(image_view);
        }
        else{
            image_view.setImageResource(R.drawable.person);
        }


        //db에서 cash 가져오기
        Retrofit retrofit;
        RetrofitInterface retrofitInterface;
        String BASE_URL = "http:192.249.18.167:80";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<UserInfoResult> call = retrofitInterface.getCash(map);

        call.enqueue(new Callback<UserInfoResult>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<UserInfoResult> call, retrofit2.Response<UserInfoResult> response) {
                if(response.code()==200){
                    UserInfoResult result = response.body();
                    cash = result.getCash();
                    TextView textView = getView().findViewById(R.id.cash_txt);
                    textView.setText(String.valueOf(cash)+"won");
                }else if(response.code()==404){
                    Toast.makeText(getActivity().getApplicationContext(),"No Products", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Log.d("purchasecheck","0");

        mRecyclerView = view.findViewById(R.id.recyclerview_purchase);
        titles = new ArrayList<String>();
        prices = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();
        timeStamp = new ArrayList<String>();
        mImages = new ArrayList<>();

        HashMap<String, String> map0 = new HashMap<>();
        map0.put("email", MainActivity_Tab.getUser());
        Log.d("purchasecheck", MainActivity_Tab.getUser());

        Call<PurchaseResult> call0 = retrofitInterface.getPuchase(map0);

        call0.enqueue(new Callback<PurchaseResult>() {
            @Override
            public void onResponse(Call<PurchaseResult> call, Response<PurchaseResult> response) {
                PurchaseResult result = response.body();
                Log.d("purchasecheck","111");
                if(result.getCode()==200){
                    Log.d("purchasecheck","1");
                    titles = result.getName();
                    qty = result.getQty();
                    timeStamp = result.getTimeStamp();

                    if(titles.size()==0){
                        Toast.makeText(getActivity().getApplicationContext(), "No Purchases", Toast.LENGTH_SHORT).show();
                    }

                    //수정 이미지 불러오기
                    for(int i=0;i<titles.size();i++){
                        final int j=i;
                        HashMap<String, String> map = new HashMap<>();

                        map.put("name", titles.get(i));
                        Call<ResponseBody> callImage = retrofitInterface.getImage(map);

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        callImage.enqueue(new Callback<ResponseBody>(){
                            @Override
                            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                InputStream is = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                mImages.add(bitmap);
                                if(j == titles.size()-1) {
                                    adapter = new ThirdAdapter(getActivity().getApplicationContext(), titles, qty, timeStamp, mImages);
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
                }else if(response.code() ==404){
                    Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PurchaseResult> call, Throwable t) {
                Log.d("purchasecheck","fail");
                Toast.makeText(getActivity().getApplicationContext(), "Errror", Toast.LENGTH_SHORT).show();
            }
        });


    }
}