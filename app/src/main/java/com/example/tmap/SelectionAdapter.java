package com.example.tmap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.MyViewHolder> {
    private List<SelectionData> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView STitle;
        public TextView SPrice;
        public ImageView check02;

        public MyViewHolder(View v){
            super(v);
            STitle = v.findViewById(R.id.stitle);
            SPrice = v.findViewById(R.id.sprice);
            check02 = v.findViewById(R.id.check02);
        }
    }


    public SelectionAdapter(List<SelectionData> myDataset, Context context) {
        mDataset = myDataset;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selection_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SelectionData Eoption = mDataset.get(position);

        String price;
        price = Eoption.getEPrice();
        price = price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        holder.STitle.setText(Eoption.getETitle());
        holder.SPrice.setText("+"+price+"원");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("선택클릭",Eoption.getETitle());
            }
        });
    }

    @Override
    public int getItemCount() {

        //삼항 연상자
        return mDataset == null ? 0 : mDataset.size();
    }
}
