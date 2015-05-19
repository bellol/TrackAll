package com.example.bellng.trackall.listitems;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bellng.trackall.ListItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Classes.AsyncTaskCompleteListener;
import Classes.Checkpoint;
import Classes.ConnectionAPI;
import Classes.Tracking;
import Enums.ConnectionAPIMethods;

/**
 * Created by bellng on 19/05/2015.
 */
public class Package implements ListItem, Serializable, AsyncTaskCompleteListener<ConnectionAPI> {

    public String title,trackingNumber,slugName,description;
    public Tracking tracking;
    public List<Checkpoint> checkpoints; // change back to private
    private String API_KEY = "652c08bc-f1b1-45dd-99bf-0baa6d576f91";

    public Package(String title, String trackingNumber, String slugName) {
        this.title = title;
        this.trackingNumber = trackingNumber;
        this.slugName = slugName;
    }

    private Tracking createTracking(){
        Tracking t = new Tracking(trackingNumber);
        t.setSlug(slugName);
        new ConnectionAPI(API_KEY, ConnectionAPIMethods.postTracking, this, t).execute();
        return t;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return checkpoints != null ? "Status: " + checkpoints.get(checkpoints.size() - 1).getTag() + "\n" + checkpoints.get(checkpoints.size() - 1).getMessage() : "Awaiting Refresh";
    }

    @Override
    public String getImageURL() {
        switch(slugName) {
            case "australia-post":
                return "https://pbs.twimg.com/profile_images/378800000631491947/913fde7b3e31a32153098569a34f635d.png";
            case "usps":
                return "http://bloximages.chicago2.vip.townnews.com/independentri.com/content/tncms/assets/v3/editorial/b/d6/bd6f012f-c19c-5ad2-911f-d8dc1c820d3d/51e83b753c7cd.preview-300.jpg";
            case "star-track":
                return "http://firstchoicecouriers.com.au/wp-content/uploads/2014/07/Startrack-Logo.jpg";
            default:
                return null;
        }
    }

    @Override
    public void update() {
        Tracking t = tracking;

        if(t == null){
          t = createTracking();
        }

        new ConnectionAPI(API_KEY, ConnectionAPIMethods.getTrackingByNumber, this,t).execute();
    }

    @Override
    public void onTaskComplete(ConnectionAPI result) {
        if (result.getException()!=null) {
            System.out.println(result.getException().getMessage());//do something with the exception
            return;
            //TODO: HANDLE EXCEPTION PROPERLY
        }

        switch (result.getMethod().getNumberMethod()) {
            case 2://getTrackingByNumber
                tracking = (Tracking) result.getReturn();
                checkpoints = tracking.getCheckpoints();
                break;
            case 5://postTracking
                //In the response we will have exactly the information of the server
                tracking = (Tracking) result.getReturn(); //The posted tracking
                break;
        }
    }


    protected Package(Parcel in) {
        title = in.readString();
        trackingNumber = in.readString();
        slugName = in.readString();
        description = in.readString();
        tracking = (Tracking) in.readValue(Tracking.class.getClassLoader());
        if (in.readByte() == 0x01) {
            checkpoints = new ArrayList<Checkpoint>();
            in.readList(checkpoints, Checkpoint.class.getClassLoader());
        } else {
            checkpoints = null;
        }
        API_KEY = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(trackingNumber);
        dest.writeString(slugName);
        dest.writeString(description);
        dest.writeValue(tracking);
        if (checkpoints == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(checkpoints);
        }
        dest.writeString(API_KEY);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Package> CREATOR = new Parcelable.Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel in) {
            return new Package(in);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };


}
