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
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.MyViewHolder> {

    private static Context context;
    private List<String> titles;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Bitmap> images;

    public BasketAdapter(Context context, List<String> titles, List<Integer> prices, List<Integer> qty, List<Bitmap> images){
        this.context = context;
        this.titles = titles;
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
        holder.mTextView_title.setText(titles.get(position));
        holder.mTextView_price.setText(prices.get(position).toString());
        holder.mTextView_qty.setText(qty.get(position).toString());
        holder.mImageView.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView_title, mTextView_price, mTextView_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

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
                    intent.putExtra("title", titles.get(position));
                    intent.putExtra("price", prices.get(position));
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
