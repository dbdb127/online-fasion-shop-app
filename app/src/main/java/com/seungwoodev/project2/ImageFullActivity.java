package com.seungwoodev.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageFullActivity extends AppCompatActivity {
    Bitmap image;
    private String strTitle;
    private Integer intPrice, intQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);

        Intent intent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("image");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        ImageView BigImage = (ImageView)findViewById(R.id.imageView);
        BigImage.setImageBitmap(image);

        strTitle = intent.getStringExtra("title");
        intPrice = intent.getIntExtra("price", 1000);
        intQty = intent.getIntExtra("qty", 1);

        TextView text_title = findViewById(R.id.text_title);
        TextView text_price = findViewById(R.id.text_price);
        TextView text_qty = findViewById(R.id.text_qty);

        text_title.setText(strTitle);
        text_price.setText(intPrice+"");
        text_qty.setText(intQty+"");
    }
}