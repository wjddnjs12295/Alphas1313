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

import java.util.List;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.MyViewHolder> {
    private List<SelectionData> mDataset;

    public interface OnItemClickEventListener {
        void onItemClick(View a_view, int a_position);
    }

    private SelectionAdapter.OnItemClickEventListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView STitle;
        public TextView SPrice;
        public ImageView check02;
        private boolean Click;

        public MyViewHolder(View v, SelectionAdapter.OnItemClickEventListener onItemClickEventListener) {
            super(v);
            STitle = v.findViewById(R.id.stitle);
            SPrice = v.findViewById(R.id.sprice);
            check02 = v.findViewById(R.id.check02);

//            v.setOnClickListener(view -> {
//                final int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    onItemClickEventListener.onItemClick(view, position);
//                    Click = !Click;
//                    if (Click) {
//                        check02.setImageResource(R.mipmap.check);
//                    } else {
//                        check02.setImageResource(R.mipmap.nonecheck);
//                    }
//                }
//            });
        }
    }


    public SelectionAdapter(List<SelectionData> myDataset, Context context) {
        mDataset = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selection_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v, mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SelectionData Eoption = mDataset.get(position);

        final boolean[] Click = {false};

        String price;
        price = Eoption.getSPrice();
        price = price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        holder.STitle.setText(Eoption.getSTitle());
        holder.SPrice.setText("+" + price + "원");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click[0] = !Click[0];
                if (Click[0]) {
                    holder.check02.setImageResource(R.mipmap.check);
                } else {
                    holder.check02.setImageResource(R.mipmap.nonecheck);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //삼항 연상자
        return mDataset == null ? 0 : mDataset.size();
    }

    public void setOnItemClickListener(SelectionAdapter.OnItemClickEventListener a_listener) {
        mItemClickListener = a_listener;
    }
}
