package com.example.bellng.trackall.listitems;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bellng.trackall.DatabaseHelper;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by bellng on 11/05/2015.
 */
public class Myki implements ListItem, Serializable {

    private final String iconURL = "http://pbs.twimg.com/profile_images/471546658426089472/ZtAyQAmk.jpeg";
    private String title;
    private String username,password;
    private HashMap<String,String> cards;
    private String defaultCard;
    boolean updating;

    public Myki(String title,String username, String password){
        this.title = title;
        this.username = username;
        this.password = password;
        this.cards = new HashMap<String,String>();
        updating = false;
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

    public boolean isUpdating(){
        return updating;
    }

    public void update(){
        //TODO: set up selenium/selendroid
    }

    public void addToDatabase(DatabaseHelper dbHelper){
        //TODO:
    }

    public void deleteFromDatabase(DatabaseHelper dbHelper){
        //TODO:
    }

    public void editName(DatabaseHelper dbHelper, String name){
        //TODO:
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return (defaultCard == null || defaultCard.equals("")) ? "Tap here to setup your default card" : "Balance: "+ cards.get(defaultCard) +"\nEstimated Top-Up in x Days";
    }

    public String getImageURL(){
        return iconURL;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(username);
        parcel.writeString(password);
    }
}

