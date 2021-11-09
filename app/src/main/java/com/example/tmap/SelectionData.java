package com.example.tmap;

import java.io.Serializable;

public class SelectionData implements Serializable {
    private String stitle;
    private String sprice;

    public String getSTitle() {
        return stitle;
    }

    public void setSTitle(String stitle) {
        this.stitle = stitle;
    }

    public String getSPrice() {
        return sprice;
    }

    public void setSPrice(String sprice) {
        this.sprice = sprice;
    }
}
