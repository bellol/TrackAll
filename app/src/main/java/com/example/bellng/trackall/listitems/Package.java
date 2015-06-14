package com.example.bellng.trackall.listitems;

import com.example.bellng.trackall.DatabaseHelper;

import java.io.Serializable;
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

    // Database constants
    public static final String TABLE_NAME = "packages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TRACKING_NUMBER = "tracking_number";
    public static final String COLUMN_SLUG_NAME = "slug_name";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_TRACKING_NUMBER + " TEXT NOT NULL, " +
                    COLUMN_SLUG_NAME + " TEXT NOT NULL" +
                    ")";

    private long id;
    public String title,trackingNumber,slugName,description;
    public Tracking tracking; // stores the information about the shipment
    public List<Checkpoint> checkpoints;
    private String API_KEY = "652c08bc-f1b1-45dd-99bf-0baa6d576f91";
    private boolean updating;

    // Default constructor with required params
    public Package(long id, String title, String trackingNumber, String slugName) {
        this.id = id;
        this.title = title;
        this.trackingNumber = trackingNumber;
        this.slugName = slugName;
        updating = false;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public Package(String title, String trackingNumber, String slugName) {
        this.title = title;
        this.trackingNumber = trackingNumber;
        this.slugName = slugName;
        updating = false;
    }

    /**
     * This method retrieves the "Tracking" object from the API
     *
     * The object is created and then passed into the API for it added
     * @return
     */
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

    public String getTrackingNumber(){
        return trackingNumber;
    }

    public String getSlugName(){
        return slugName;
    }

    @Override
    public String getImageURL() {
        switch(slugName) {
            case "australia-post":
                return "file:///android_asset/auspost.png";
            case "usps":
                return "file:///android_asset/usps.png";
            case "star-track":
                return "file:///android_asset/startrack.png";
            case "dhl":
                return "file:///android_asset/dhl.png";
            case "tnt-au":
                return "file:///android_asset/tnt.jpg";
            case "fedex":
                return "file:///android_asset/fedex.jpg";
            case "ups":
                return "file:///android_asset/ups.jpg";
            case "toll-ipec":
                return "file:///android_asset/tollipec.png";
            case "toll-priority":
                return "file:///android_asset/tollpriority.gif";
            default:
                return null;
        }
    }

    @Override
    public void update() {
        if(!updating) {
            updating = true;

            if (tracking == null) tracking = createTracking();

            // get the information for this "tracking" from the API
            new ConnectionAPI(API_KEY, ConnectionAPIMethods.getTrackingByNumber, this, tracking).execute();
        }
    }

    public void addToDatabase(DatabaseHelper dbHelper){
        dbHelper.addPackage(this);
    }

    public void deleteFromDatabase(DatabaseHelper dbHelper){
        dbHelper.removePackage(this);
    }

    public void editName(DatabaseHelper dbHelper, String name){
        this.title = name;
        dbHelper.editPackageName(this, name);
    }

    public boolean isUpdating(){
        return updating;
    }

    @Override
    public void onTaskComplete(ConnectionAPI result) {
        updating = false;

        if (result.getException()!=null) {
            System.out.println(result.getException().getMessage());
            return;
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
}
