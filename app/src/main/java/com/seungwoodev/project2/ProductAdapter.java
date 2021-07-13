package com.seungwoodev.project2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private static Context context;
    private List<String> titles;
    private List<Integer> prices;
    private List<Integer> qty;
    private static List<Bitmap> images;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";

    public ProductAdapter(Context context, List<String> titles, List<Integer> prices, List<Integer> qty, List<Bitmap> images){
        this.context = context;
        this.titles = titles;
        this.prices = prices;
        this.qty = qty;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextView1.setText(titles.get(position));
        holder.mTextView2.setText(prices.get(position).toString());
        holder.mImageView.setImageBitmap(images.get(position));

//        //db에서 img 받아오기
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        retrofitInterface = retrofit.create(RetrofitInterface.class);
//
//        HashMap<String, String> map = new HashMap<>();
//
//        map.put("name", titles.get(position));
//        Call<ResponseBody> call = retrofitInterface.getImage(map);
//        call.enqueue(new Callback<ResponseBody>(){
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                InputStream is = response.body().byteStream();
//                Log.d("kyung is", String.valueOf(is));
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                Log.d("kyung bitmap", String.valueOf(bitmap));
//                holder.mImageView.setImageBitmap(bitmap);
////                    Uri uri = Uri.parse("172.10.18.167"+images.get(position));
////                    Glide.with(context).load(uri).into(holder.mImageView);
//
////                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
////                    builder1.setTitle(result.getName());
////                    builder1.setMessage(result.getEmail());
////
////                    builder1.show();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t){
////                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

//        holder.mImageView.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView1, mTextView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageview);
            mTextView1 = itemView.findViewById(R.id.textview1);
            mTextView2 = itemView.findViewById(R.id.textview2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context ,ImageFullActivity.class);
                    int position = getAdapterPosition();
                    Bitmap sendBitmap = images.get(position);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Log.d("imageintent", String.valueOf(byteArray));
                    intent.putExtra("image",byteArray);
                    intent.putExtra("title", titles.get(position));
                    intent.putExtra("price", prices.get(position));
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
}
