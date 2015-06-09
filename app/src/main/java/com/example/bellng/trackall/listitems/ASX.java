package com.example.bellng.trackall.listitems;

import android.os.AsyncTask;

import com.example.bellng.trackall.DatabaseHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.Serializable;

/**
 * Created by Bell on 10/06/15.
 */
public class ASX implements ListItem, Serializable {

    // Database constants
    public static final String TABLE_NAME = "asx";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TICKER = "ticker";
    public static final String COLUMN_COMPANY = "company";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_TICKER + " TEXT NOT NULL, " +
                    COLUMN_COMPANY + " TEXT NOT NULL" +
                    ")";

    private long id;

    String title;
    String ticker,companyName;
    String price;
    boolean updating;

    public ASX(String title, String ticker, String companyName){
        this.title = title;
        this.ticker = ticker;
        this.companyName = companyName;
        updating = false;
    }

    public ASX(long id, String title, String ticker, String companyName){
        this.id = id;
        this.title = title;
        this.ticker = ticker;
        this.companyName = companyName;
        updating = false;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getTicker(){
        return ticker;
    }

    public String getCompanyName(){
        return companyName;
    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return price != null ? "Current price: $" + price : "Awaiting Refresh";
    }

    @Override
    public String getImageURL() {
        return "file:///android_asset/asx.jpg";
    }

    @Override
    public void update() {
        if(!updating) {
            updating = true;
            new RetrievePriceTask().execute("http://www.google.com/finance?q=ASX:" + ticker);
        }
    }

    @Override
    public void addToDatabase(DatabaseHelper dbHelper) {
        dbHelper.addASX(this);
    }

    @Override
    public void deleteFromDatabase(DatabaseHelper dbHelper) {
        dbHelper.removeASX(this);
    }

    @Override
    public void editName(DatabaseHelper dbHelper, String name) {
        this.title = name;
        dbHelper.editASXName(this,name);
    }

    @Override
    public boolean isUpdating() {
        return updating;
    }

    class RetrievePriceTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
                Document doc = Jsoup.connect(urls[0]).userAgent(ua).get();
                price = doc.select("span[class=pr]").first().children().first().ownText();
                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            updating = false;
        }
    }
}
