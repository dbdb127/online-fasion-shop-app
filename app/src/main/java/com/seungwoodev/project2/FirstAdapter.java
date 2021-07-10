package com.seungwoodev.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.MyViewHolder> {

    private static Context context;
    private List<String> titles;
    private List<Integer> prices;
    private static List<Integer> images;

    public FirstAdapter(Context context, List<String> titles, List<Integer> prices, List<Integer> images){
        this.context = context;
        this.titles = titles;
        this.prices = prices;
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
        holder.mImageView.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
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
                    Bitmap sendBitmap = BitmapFactory.decodeResource(context.getResources(), images.get(position));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("image",byteArray);
                    context.startActivity(intent);
                }
            });
        }
    }
}
