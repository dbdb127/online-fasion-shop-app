package com.seungwoodev.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class SubActivity_Google extends AppCompatActivity {

    private String strNick, strProfileImg, strEmail;
    private Button tab_button, logout_button;
    private Bitmap person_img;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        byte[] arr = intent.getByteArrayExtra("person_img");
        Uri img_url = intent.getParcelableExtra("img_url");
        strEmail = intent.getStringExtra("email");

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
        }
        else{
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(this).load(img_url).apply(options).into(iv_profile);
        }

        // log out
        logout_button = (Button)findViewById(R.id.btn_logout);
        logout_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(SubActivity_Google.this,"Logout", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SubActivity_Google.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // when click, go to the tab
        tab_button = (Button)findViewById(R.id.btn_tab);
        tab_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubActivity_Google.this, MainActivity_Tab.class);
                intent.putExtra("email", strEmail);
                intent.putExtra("name", strNick);
                startActivity(intent);
            }
        });

    }
}