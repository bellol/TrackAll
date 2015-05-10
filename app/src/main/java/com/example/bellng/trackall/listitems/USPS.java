package com.example.bellng.trackall.listitems;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bellng.trackall.ListItem;

/**
 * Created by Bell on 11/05/15.
 */
public class USPS implements ListItem, Parcelable{
    private final String iconURL = "http://bloximages.chicago2.vip.townnews.com/independentri.com/content/tncms/assets/v3/editorial/b/d6/bd6f012f-c19c-5ad2-911f-d8dc1c820d3d/51e83b753c7cd.preview-300.jpg";

    private String title,trackingNumber;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public USPS createFromParcel(Parcel in){
            return new USPS(in);
        }
        public USPS[] newArray(int size){
            return new USPS[size];
        }
    };

    public USPS(Parcel in){
        this.title = in.readString();
        this.trackingNumber = in.readString();
    }

    public USPS(String title, String trackingNumber){
        this.title = title;
        this.trackingNumber = trackingNumber;
    }

    public void update(){
        //TODO: set up aftership
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return "test description";
    }

    public String getImageURL(){
        return iconURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(trackingNumber);
    }
}
