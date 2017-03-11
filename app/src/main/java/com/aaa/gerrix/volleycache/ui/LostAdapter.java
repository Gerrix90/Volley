package com.aaa.gerrix.volleycache.ui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.gerrix.volleycache.R;
import com.aaa.gerrix.volleycache.model.Lost;

import java.util.ArrayList;

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.DataObjectHolder>{

    private ArrayList<Lost> mDataset;
    private Context context;

    public LostAdapter(ArrayList<Lost> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_item_row, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        context = parent.getContext();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.title.setText(mDataset.get(position).getTitle());
        holder.body.setText(String.valueOf(mDataset.get(position).getId()));

        byte[] imageData = mDataset.get(position).getImage().getBlob();
        Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        holder.image.setImageBitmap(image);
//        Glide
//                .with(context)
//                .load(mDataset.get(position).getImage())
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher)
//                .crossFade()
//                .into(holder.image);



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView title, body;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.id);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}




































