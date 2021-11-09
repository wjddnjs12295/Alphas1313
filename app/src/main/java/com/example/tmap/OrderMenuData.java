package com.example.tmap;

import android.util.Log;

import java.io.Serializable;

public class OrderMenuData implements Serializable {
    private String name;
    private String urlToImage;
    private String price;
    private String menuid;
    private String option;
    private String categoryid;
    private boolean showtitle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getCategoryidid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean getshowTitle() {
        return showtitle;
    }

    public void setShowtitle(boolean showtitle) {
        this.showtitle = showtitle;
    }

}
