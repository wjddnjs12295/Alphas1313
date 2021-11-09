package com.example.tmap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class CategoryAdapter1 extends RecyclerView.Adapter<CategoryAdapter1.MyViewHolder> {
    private List<OrderTitleData1> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TextViewTitle;

        public MyViewHolder(View v) {
            super(v);
            TextViewTitle = v.findViewById(R.id.TextView_content1);
        }
    }


    public CategoryAdapter1(List<OrderTitleData1> myDataset, Context context) {
        mDataset = myDataset;
        Fresco.initialize(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_layout1, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderTitleData1 news = mDataset.get(position);
        holder.TextViewTitle.setText(news.getCategory());
    }

    @Override
    public int getItemCount() {
        //삼항 연상자
        return mDataset == null ? 0 : mDataset.size();
    }

}
