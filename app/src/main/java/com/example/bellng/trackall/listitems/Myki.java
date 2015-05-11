package com.example.bellng.trackall.listitems;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bellng.trackall.ListItem;

/**
 * Created by bellng on 11/05/2015.
 */
public class Myki implements ListItem, Parcelable {

    private final String iconURL = "http://pbs.twimg.com/profile_images/471546658426089472/ZtAyQAmk.jpeg";
    private String title;
    private String username,password;
    private String balance;

    public Myki(String title,String username, String password){
        this.title = title;
        this.username = username;
        this.password = password;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Myki createFromParcel(Parcel in){
            return new Myki(in);
        }
        public Myki[] newArray(int size){
            return new Myki[size];
        }
    };

    public Myki(Parcel in){
        this.title = in.readString();
        this.username = in.readString();
        this.password = in.readString();
    }

    public void update(){
        //TODO: set up selenium/selendroid
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return "Balance: $100\nEstimated Top-Up in 4 days";
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
        parcel.writeString(username);
        parcel.writeString(password);
    }
}

