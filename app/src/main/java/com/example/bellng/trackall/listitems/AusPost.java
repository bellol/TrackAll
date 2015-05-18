package com.example.bellng.trackall.listitems;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.bellng.trackall.ListItem;

import Classes.AsyncTaskCompleteListener;
import Classes.ConnectionAPI;
import Classes.Tracking;
import Enums.ConnectionAPIMethods;


/**
 * Created by bellng on 10/05/2015.
 */
public class AusPost implements ListItem, Parcelable,AsyncTaskCompleteListener<ConnectionAPI> {
    private final String iconURL = "https://pbs.twimg.com/profile_images/378800000631491947/913fde7b3e31a32153098569a34f635d.png";
    private final String slug = "australia-post";

    private String title, trackingNumber;

    private String status = "before update";

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AusPost createFromParcel(Parcel in) {
            return new AusPost(in);
        }

        public AusPost[] newArray(int size) {
            return new AusPost[size];
        }
    };

    public AusPost(Parcel in) {
        this.title = in.readString();
        this.trackingNumber = in.readString();
    }

    public AusPost(String title, String trackingNumber) {
        this.title = title;
        this.trackingNumber = trackingNumber;
    }

    public void update() {
        //TODO: set up aftership
        //TODO: check if trackinge xists in accountbefore adding
       Tracking tracking = new Tracking(trackingNumber);
        tracking.setSlug(slug);

       //Tracking tracking = new Tracking("6WR0001905");

        //Tracking tracking = new Tracking("00247280");
        //tracking.setSlug("star-track");
        new ConnectionAPI("652c08bc-f1b1-45dd-99bf-0baa6d576f91", ConnectionAPIMethods.getTrackingByNumber, this,tracking).execute();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return status;
    }

    public String getImageURL() {
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

    @Override
    public void onTaskComplete(ConnectionAPI result) {
        //Remember to control a possible Exception
        if (result.getException()!=null) {
            System.out.println(result.getException().getMessage());//do something with the exception
            return;
            //TODO: handle the exception
        }

        switch (result.getMethod().getNumberMethod()) {
            case 2://getTrackingByNumber
                Tracking tracking = (Tracking) result.getReturn();
                System.out.println(tracking.getTag());
                status = tracking.getTag().toString();
                break;
        }
    }
}
