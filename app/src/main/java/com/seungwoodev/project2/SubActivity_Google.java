package com.seungwoodev.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
        person_img = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        strEmail = intent.getStringExtra("email");

        TextView tv_nick = findViewById(R.id.tv_nickName);
        TextView tv_email = findViewById(R.id.tv_email);
        ImageView iv_profile = findViewById(R.id.iv_profile);

        // set nickname
        tv_nick.setText(strNick);
        // set email
        tv_email.setText(strEmail);
        // set profile image

        iv_profile.setImageBitmap(person_img);

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
                startActivity(intent);
            }
        });

    }
}