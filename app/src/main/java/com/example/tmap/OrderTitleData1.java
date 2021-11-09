package com.example.tmap;

import android.util.Log;

import java.io.Serializable;

public class OrderTitleData1 implements Serializable {
    private String category;

    public String getCategory() {
        Log.d("가늬","가가가가");
        return category;
    }

    public void setCategory(String category) {
        Log.d("받늬","가가가가");
        this.category = category;
    }
}