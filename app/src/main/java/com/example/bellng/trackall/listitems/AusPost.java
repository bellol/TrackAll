package com.example.bellng.trackall.listitems;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.bellng.trackall.ListItem;


/**
 * Created by bellng on 10/05/2015.
 */
public class AusPost implements ListItem, Parcelable {
    private final String iconURL = "https://pbs.twimg.com/profile_images/378800000631491947/913fde7b3e31a32153098569a34f635d.png";

    private String title,trackingNumber;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public AusPost createFromParcel(Parcel in){
            return new AusPost(in);
        }
        public AusPost[] newArray(int size){
            return new AusPost[size];
        }
    };

    public AusPost(Parcel in){
        this.title = in.readString();
        this.trackingNumber = in.readString();
    }

    public AusPost(String title, String trackingNumber){
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
