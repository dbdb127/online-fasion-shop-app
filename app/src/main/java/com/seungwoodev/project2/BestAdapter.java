package com.seungwoodev.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class BestAdapter extends RecyclerView.Adapter<BestAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Product_Best> list;
    private ArrayList<Bitmap> images;

    public BestAdapter(Context context, ArrayList<Product_Best> list, ArrayList<Bitmap> images){
        this.context = context;
        this.list = list;
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
        holder.mTextView_title.setText(list.get(position).name);
        holder.mTextView_price.setText(String.valueOf(list.get(position).price));
        holder.mImageView.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView_title, mTextView_price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageview);
            mTextView_title = itemView.findViewById(R.id.textview1);
            mTextView_price = itemView.findViewById(R.id.textview2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context ,ImageFullActivity.class);
                    int position = getAdapterPosition();
                    Bitmap sendBitmap = images.get(position);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    intent.putExtra("image",byteArray);
                    intent.putExtra("title", list.get(position).name);
                    intent.putExtra("price", list.get(position).price);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
