package com.seungwoodev.project2;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import retrofit2.Retrofit;

public class ThirdFragment extends Fragment {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";
    public List<String> image;

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
        Glide.with(getActivity().getApplicationContext())
                .load(MainActivity_Tab.getImage())
                .into(image_view);
        Log.d("name", name);
        Log.d("email", email);

    }

}