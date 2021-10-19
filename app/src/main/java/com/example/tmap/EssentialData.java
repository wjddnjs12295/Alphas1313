package com.example.tmap;

import java.io.Serializable;

public class EssentialData implements Serializable {
    private String etitle;
    private String eprice;

    public String getETitle() {
        return etitle;
    }

    public void setETitle(String etitle) {
        this.etitle = etitle;
    }

    public String getEPrice() {
        return eprice;
    }

    public void setEPrice(String eprice) {
        this.eprice = eprice;
    }
}
