package com.seungwoodev.project2;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubActivity_Google extends AppCompatActivity {

    private String strNick, strProfileImg, strEmail;
    private Button tab_button, logout_button, revoke_button;
    private Bitmap person_img;
    private GoogleSignInClient mGoogleSignInClient;

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BASE_URL = "http:192.249.18.167:80";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        byte[] arr = intent.getByteArrayExtra("person_img");
        Uri img_url = intent.getParcelableExtra("img_url");
        strEmail = intent.getStringExtra("email");

        Intent newIntent = new Intent(SubActivity_Google.this, MainActivity_Tab.class);

        TextView tv_nick = findViewById(R.id.tv_nickName);
        TextView tv_email = findViewById(R.id.tv_email);
        ImageView iv_profile = findViewById(R.id.iv_profile);

        // set nickname
        tv_nick.setText(strNick);
        // set email
        tv_email.setText(strEmail);
        // set profile image
        if(arr != null){
            person_img = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            iv_profile.setImageBitmap(person_img);
            newIntent.putExtra("image", person_img);
        }
        else{
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(this).load(img_url).apply(options).into(iv_profile);

            //url을 bitmap으로
//            Bitmap bitmap = getBitmapFromURL(img_url.toString());
//            newIntent.putExtra("image", bitmap);
            newIntent.putExtra("image", img_url);
        }


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SubActivity_Google.this);
        Log.d("Google revoke access account", acct+"");
        if (acct != null) {
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        // log out
        logout_button = (Button)findViewById(R.id.btn_logout);
        logout_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(SubActivity_Google.this,"Logout", Toast.LENGTH_LONG).show();
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SubActivity_Google.this);
                if (acct != null) {
                    signOut();
                }
                Intent intent = new Intent(SubActivity_Google.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // when click, go to the tab
        tab_button = (Button)findViewById(R.id.btn_tab);
        tab_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newIntent.putExtra("email", strEmail);
                newIntent.putExtra("name", strNick);
                startActivity(newIntent);

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                retrofitInterface = retrofit.create(RetrofitInterface.class);

                HashMap<String, String> map = new HashMap<>();
                map.put("email", strEmail);
                map.put("name", strNick);
                Call<Void> call = retrofitInterface.checkUser(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){

                        }else if(response.code() ==400){
                            Toast.makeText(SubActivity_Google.this, "Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SubActivity_Google.this, t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // sign out


        // revoke Access 계정 연결 해
        revoke_button = (Button)findViewById(R.id.btn_revoke);
        revoke_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SubActivity_Google.this);
                if (acct != null) {
                    revokeAccess();
                    Toast.makeText(SubActivity_Google.this,"Revoke Google Access", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubActivity_Google.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SubActivity_Google.this, "You are not signed in by Google Account", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            // Log exception
//            return null;
//        }
//    }
}