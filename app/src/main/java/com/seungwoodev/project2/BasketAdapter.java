package com.seungwoodev.project2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.MyViewHolder> {

    private static Activity activity;
    private static Context context;
    private List<String> names;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Bitmap> images;


    public BasketAdapter(Context context, List<String> names, List<Integer> prices, List<Integer> qty, List<Bitmap> images){
//        this.activity = activity;
        this.context = context;
        this.names = names;
        this.prices = prices;
        this.qty = qty;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextView_title.setText(names.get(position));
        holder.mTextView_price.setText(prices.get(position).toString());
        holder.mTextView_qty.setText(qty.get(position).toString());
        holder.mImageView.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView_title, mTextView_price, mTextView_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Retrofit retrofit;
            RetrofitInterface retrofitInterface;
            String BASE_URL = "http:192.249.18.167:80";

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);

            mImageView = itemView.findViewById(R.id.product_imageview);
            mTextView_title = itemView.findViewById(R.id.text_title);
            mTextView_price = itemView.findViewById(R.id.text_price);
            mTextView_qty = itemView.findViewById(R.id.text_qty);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ImageFullActivity.class);
                    int position = getAdapterPosition();
                    Bitmap sendBitmap = images.get(position);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    intent.putExtra("image",byteArray);
                    intent.putExtra("title", names.get(position));
                    intent.putExtra("price", prices.get(position));
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //삭제 alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setMessage("Do you want to delete the product?")
                            .setTitle("Delete")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context.getApplicationContext(), "Completed\nPlease update.", Toast.LENGTH_SHORT).show();
                                    HashMap<String, String> map = new HashMap<>();

                                    int position = getAdapterPosition();
                                    map.put("name", names.get(position));
                                    map.put("qty", qty.get(position).toString());
                                    map.put("email", MainActivity_Tab.getUser());

                                    Call<Void> call = retrofitInterface.deleteProduct(map);
                                    call.enqueue(new Callback<Void>(){
                                        @Override
                                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                            if(response.code()==200){
                                            }else if(response.code()==404){
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t){
                                            Log.d("failed", "connection "+call);
                                            Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });

                    builder.create();
                    builder.show();

                    return true;
                }
            });
        }
    }
}
