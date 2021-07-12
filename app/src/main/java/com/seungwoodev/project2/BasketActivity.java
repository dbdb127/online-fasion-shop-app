package com.seungwoodev.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Integer> mImages;
    private BasketAdapter adapter;
    public final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        mRecyclerView = findViewById(R.id.recyclerView);

        names = new ArrayList<>();
        prices = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();
        mImages = new ArrayList<>();

        //product list
//        mImages.add(R.drawable.ic_baseline_checkroom_24);
//        names.add("temp");
//        qty.add(1);
//        prices.add(12345);

        //db에서 basket 가져오기
        Retrofit retrofit;
        RetrofitInterface retrofitInterface;
        String BASE_URL = "http:192.249.18.167:80";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();

        map.put("email", MainActivity_Tab.getUser());

        Call<ProductResult> call = retrofitInterface.getBasket(map);

        call.enqueue(new Callback<ProductResult>(){
            @Override
            public void onResponse(Call<ProductResult> call, retrofit2.Response<ProductResult> response) {
                if(response.code()==200){
                    ProductResult result = response.body();
                    names = result.getName();   //ArrayList
                    prices = result.getPrice();
                    qty = result.getQty();

                    for(int i=0;i<names.size();i++){
                        mImages.add(R.drawable.a);
//                        Log.d("kyung", names.get(i));
                    }

                    adapter = new BasketAdapter(BasketActivity.this, names, prices, qty, mImages);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(BasketActivity.this, 1, GridLayoutManager.VERTICAL, false);

                    mRecyclerView.setAdapter(adapter);

                    mRecyclerView.setLayoutManager(gridLayoutManager);
                    mRecyclerView.setHasFixedSize(true);

                }else if(response.code()==404){
                    Toast.makeText(BasketActivity.this,"No Products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(BasketActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    //이미지 가져오기
//    public void getPickImageChooserIntent() {
//
//
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = getPackageManager();
//        ArrayList<Image> images = new ArrayList<>();
//        //Intent intent = new Intent(Intent.ACTION_PICK);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(Intent.createChooser(intent, "이미지 다중 선택"), REQUEST_CODE);
//
//    }
//
//    //getPickImageChooserIntent()에서 startActivitiForResult하고 난 결과를 받는 메소드
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        String imagePath = null;
//        ArrayList<String> imageListUri = new ArrayList<>();
//        ArrayList<Uri> realUri = new ArrayList<>();
//
//        if (requestCode == REQUEST_CODE) {
//
//            Toast.makeText(this, "여기 드가욤", Toast.LENGTH_SHORT).show();
//
//            // photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
//            if (data.getClipData() == null) {
//
//                Toast.makeText(this, "다중 선택이 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
//
//            } else { //다중 선택 했을 경우
//
//                ClipData clipData = data.getClipData();
//                //Log.i("clipdata1 : ", String.valueOf(clipData.getItemCount()));
//                //Log.i("clipdata2 : ", String.valueOf(clipData.getItemCount()));
//                //Log.i("clipdata3 : ", String.valueOf(clipData.getItemCount()));
//
//
//                if (clipData.getItemCount() > 5) {
//                    Toast.makeText(TestUploadImage.this, "사진은 5장까지 선택 가능합니다", Toast.LENGTH_SHORT).show();
//                } else if (clipData.getItemCount() == 1) {
//
//                    Uri tempUri;
//                    tempUri = clipData.getItemAt(0).getUri();
//                    imagePath = tempUri.toString();
//
//                    mBitmap.add(BitmapFactory.decodeFile(imagePath));
//                    image1.setImageBitmap(mBitmap.get(0));
//                    //File file = new File(imagePath);
//                    //imageView1.setImageURI(Uri.fromFile(file));
//                } else if ((clipData.getItemCount() > 1) && (clipData.getItemCount() <= 5)) {
//                    for (int i = 0; i < clipData.getItemCount(); i++) {
//                        Uri tempUri;
//                        tempUri = clipData.getItemAt(i).getUri();
//                        Log.i("temp: ", i + " " + tempUri.toString());
//                        imageListUri.add(tempUri.toString());
//                        realUri.add(tempUri);
//                        //mBitmap.add(i, BitmapFactory.decodeFile(imageListUri.get(i)));
//
//                        try {
//
//                            mBitmap.add(i,BitmapFactory.decodeStream(getContentResolver().openInputStream(realUri.get(i))));
//
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if(clipData.getItemCount() == 2) {
//                        image1.setImageBitmap(mBitmap.get(0));
//                        image2.setImageBitmap(mBitmap.get(1));
//
//                    } if (clipData.getItemCount() == 3) {
//                        image1.setImageBitmap(mBitmap.get(0));
//                        image2.setImageBitmap(mBitmap.get(1));
//                        image3.setImageBitmap(mBitmap.get(2));
//
//                    } if(clipData.getItemCount() == 4) {
//                        image1.setImageBitmap(mBitmap.get(0));
//                        image2.setImageBitmap(mBitmap.get(1));
//                        image3.setImageBitmap(mBitmap.get(2));
//                        image4.setImageBitmap(mBitmap.get(3));
//
//                    } if(clipData.getItemCount() == 5) {
//                        image1.setImageBitmap(mBitmap.get(0));
//                        image2.setImageBitmap(mBitmap.get(1));
//                        image3.setImageBitmap(mBitmap.get(2));
//                        image4.setImageBitmap(mBitmap.get(3));
//                        image5.setImageBitmap(mBitmap.get(4));
//                    }
//                }
//            }
//        }
//    }
//
//    private String getImageFromFilePath(Intent data) {
//        return getPathFromUri(data.getData());
//    }
//
//    public String getImageFilePath(Intent data) {
//        return getImageFromFilePath(data);
//    }
//
//    private String getPathFromUri(Uri contentUri) {
//        String[] proj = {MediaStore.Audio.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }


}