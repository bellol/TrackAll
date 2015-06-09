package com.example.bellng.trackall.enums;

/**
 * Created by bellng on 8/06/2015.
 */
public enum OtherItem {

    XE("XE Currency Conversion","file:///android_asset/xe.png"),
    Myki("Myki Account Balance","file:///android_asset/myki.jpeg"),
    ASX("ASX Top 50 Stock Price","file:///android_asset/asx.jpg");

    private String displayName,iconURL;

    private OtherItem(String displayName, String iconURL){
        this.displayName = displayName;
        this.iconURL = iconURL;
    }

    public String toString(){
        return displayName;
    }
    public String getIconURL() {return iconURL;}

}
