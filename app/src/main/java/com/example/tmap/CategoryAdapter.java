package com.example.tmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<OrderTitleData> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Button TextViewTitle;

        public MyViewHolder(View v) {
            super(v);
            TextViewTitle = v.findViewById(R.id.TextView_content);
        }
    }


    public CategoryAdapter(List<OrderTitleData> myDataset, Context context) {
        mDataset = myDataset;
        Fresco.initialize(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderTitleData news = mDataset.get(position);

        holder.TextViewTitle.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {

        //삼항 연상자
        return mDataset == null ? 0 : mDataset.size();
    }
}
