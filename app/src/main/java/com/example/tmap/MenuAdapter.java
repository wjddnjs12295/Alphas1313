package com.example.tmap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private List<OrderMenuData> mDataset;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TextViewTitle;
        public TextView TextViewContent;
        public TextView Optionstirng;
        public SimpleDraweeView ImageViewtitle;
        public LinearLayout Cell;

        public MyViewHolder(View v) {
            super(v);
            Cell = v.findViewById(R.id.cell);
            TextViewTitle = v.findViewById(R.id.menuname);
            TextViewContent = v.findViewById(R.id.menuprice);
            ImageViewtitle = v.findViewById(R.id.menuimage);
            Optionstirng = v.findViewById(R.id.optionitem);
        }
    }


    public MenuAdapter(List<OrderMenuData> myDataset, Context context) {
        mDataset = myDataset;
        Fresco.initialize(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderMenuData news = mDataset.get(position);
        String price;
        price = news.getPrice();
        price = price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        holder.TextViewTitle.setText(news.getName());
        holder.TextViewContent.setText(price);
        Uri uri = Uri.parse(news.getUrlToImage());
        holder.ImageViewtitle.setImageURI(uri);
        holder.Optionstirng.setText(news.getOption());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@", "" + news.getOption());
                Intent intent = new Intent(holder.itemView.getContext(), OrdercheckPage.class);
                intent.putExtra("titlekey", holder.TextViewTitle.getText());
                intent.putExtra("contentkey", holder.TextViewContent.getText());
                intent.putExtra("imagekey", uri.toString());
                intent.putExtra("optionkey", holder.Optionstirng.getText());
                ContextCompat.startActivity(holder.itemView.getContext(), intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        //삼항 연상자
        return mDataset == null ? 0 : mDataset.size();
    }
}
