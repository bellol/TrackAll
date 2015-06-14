package com.example.bellng.trackall.listitems;

import android.os.AsyncTask;

import com.example.bellng.trackall.DatabaseHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.Serializable;

/**
 * Created by bellng on 8/06/2015.
 */
public class XE implements ListItem,Serializable {

    // Database constants
    public static final String TABLE_NAME = "xe";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_FROM = "from_currency";
    public static final String COLUMN_TO = "to_currency";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                    COLUMN_FROM + " TEXT NOT NULL, " +
                    COLUMN_TO + " TEXT NOT NULL" +
                    ")";

    private long id;
    String title;
    int amount;
    String from,to,converted;

    boolean updating;

    public XE(long id, String title, int amount, String from, String to){
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.from = from;
        this.to = to;
        updating = false;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }
    public XE(String title, int amount, String from, String to){
        this.title = title;
        this.amount = amount;
        this.from = from;
        this.to = to;
        updating = false;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return converted != null ? amount + " " + from + " = " + converted + " " + to : "Awaiting Refresh";
    }

    @Override
    public String getImageURL() {
        return "file:///android_asset/xe.png";
    }

    @Override
    public void update() {
        if(!updating) {
            // the URL to be scraped
            String url = "http://www.xe.com/currencyconverter/convert/?Amount=" + amount + "&From=" + from + "&To=" + to;
            updating = true;

            // async task to scrape passed URL
            new RetrieveConversionTask().execute(url);
        }
    }

    @Override
    public void addToDatabase(DatabaseHelper dbHelper) {
        dbHelper.addXE(this);
    }

    @Override
    public void deleteFromDatabase(DatabaseHelper dbHelper) {
        dbHelper.removeXE(this);
    }

    @Override
    public void editName(DatabaseHelper dbHelper, String name) {
        this.title = name;
        dbHelper.editXEName(this,name);
    }

    public int getAmount(){
        return amount;
    }

    public String getFrom(){
        return from;
    }

    public String getTo(){
        return to;
    }
    public boolean isUpdating(){
        return updating;
    }

    class RetrieveConversionTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                // Set user agent to avoid being detected as a bot
                String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";

                // Get the document from the url
                Document doc = Jsoup.connect(urls[0]).userAgent(ua).timeout(5000).get();

                // Extract the required value from the page
                converted = doc.select("td[class=rightCol]").first().ownText();

                // Since there is an extra "space" character at the end, remove it
                if(converted != null || !converted.equals("")) converted = converted.substring(0, converted.length() - 1);

                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String feed) {
            updating = false;
        }
    }
}
