package com.example.tmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skt.Tmap.TMapPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class EssentialAdapter extends RecyclerView.Adapter<EssentialAdapter.MyViewHolder> {
    private List<EssentialData> mDataset;
    Bitmap bitmap;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView ETitle;
        public TextView EPrice;
        public ImageView check01;
        public LinearLayout essentiallayout;

        public MyViewHolder(View v){
            super(v);
            ETitle = v.findViewById(R.id.etitle);
            EPrice = v.findViewById(R.id.eprice);
            check01 = v.findViewById(R.id.check01);
            essentiallayout = v.findViewById(R.id.essentiallayout);
        }
    }


    public EssentialAdapter(List<EssentialData> myDataset, Context context) {
        mDataset = myDataset;
        Fresco.initialize(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.essential_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EssentialData Eoption = mDataset.get(position);

        String price;
        price = Eoption.getEPrice();
        price = price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        holder.ETitle.setText(Eoption.getETitle());
        holder.EPrice.setText("+"+price+"원");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("클릭","클릭"+Eoption.getETitle());
            }
        });
    }

    @Override
    public int getItemCount() {

        //삼항 연상자
        return mDataset == null ? 0 : mDataset.size();
    }
}
