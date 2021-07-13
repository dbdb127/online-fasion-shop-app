package com.seungwoodev.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ThirdAdapter extends RecyclerView.Adapter<ThirdAdapter.MyViewHolder> {

    private static Context context;
    private ArrayList<String> titles;
    private ArrayList<Integer> qty;
    private ArrayList<String> timeStamp;
    private ArrayList<Bitmap> images;


    public ThirdAdapter(Context context, ArrayList<String> titles, ArrayList<Integer> qty, ArrayList<String> timeStamp, ArrayList<Bitmap> images){
        this.context = context;
        this.titles = titles;
        this.qty = qty;
        this.timeStamp = timeStamp;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextView_title.setText(titles.get(position));
        holder.mTextView_qty.setText(String.valueOf(qty.get(position)));
        holder.mTextView_time.setText((CharSequence) timeStamp.get(position));
//        holder.mImageView.setImageBitmap(images.get(position));

        //db에서 image 불러오기
        Retrofit retrofit;
        RetrofitInterface retrofitInterface;
        String BASE_URL = "http:192.249.18.167:80";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();

        map.put("name", titles.get(position));
        Call<ResponseBody> callImage = retrofitInterface.getImage(map);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        callImage.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                InputStream is = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                images.add(position, bitmap);
                holder.mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                Log.d("bitmapfail", "String.valueOf(bitmap)");
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView_title, mTextView_qty, mTextView_time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.purchase_imageview);
            mTextView_title = itemView.findViewById(R.id.purchase_title);
            mTextView_qty = itemView.findViewById(R.id.purchase_qty);
            mTextView_time = itemView.findViewById(R.id.purchase_time);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context ,ImageFullActivity.class);
//                    int position = getAdapterPosition();
//                    Bitmap sendBitmap = images.get(position);
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//
//                    intent.putExtra("image",byteArray);
//                    intent.putExtra("title", titles.get(position));
//                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
