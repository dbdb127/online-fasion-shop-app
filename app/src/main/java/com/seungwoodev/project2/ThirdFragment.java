package com.seungwoodev.project2;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThirdFragment extends Fragment {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";
    public List<String> image;
    private int cash;

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

        else {
            Glide.with(getActivity().getApplicationContext())
                    .load(MainActivity_Tab.getImage())
                    .into(image_view);
        }
        Log.d("name", name);
        Log.d("email", email);



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
                    textView.setText("Cash  "+String.valueOf(cash));
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


    }

}