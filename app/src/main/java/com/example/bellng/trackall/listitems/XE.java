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

    String title;

    int amount;
    String from,to,converted;

    boolean updating;

    public XE(int amount, String from, String to){
        this.amount = amount;
        this.from = from;
        this.to = to;
        title = "Currency Conversion";
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
            String url = "http://www.xe.com/currencyconverter/convert/?Amount=" + amount + "&From=" + from + "&To=" + to;
            updating = true;
            new RetrieveConversionTask().execute(url);
        }
    }

    @Override
    public void addToDatabase(DatabaseHelper dbHelper) {

    }

    @Override
    public void deleteFromDatabase(DatabaseHelper dbHelper) {

    }

    @Override
    public void editName(DatabaseHelper dbHelper, String name) {

    }

    public boolean isUpdating(){
        return updating;
    }
    class RetrieveConversionTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
                Document doc = Jsoup.connect(urls[0]).userAgent(ua).get();
                converted = doc.select("td[class=rightCol]").first().ownText();
                if(converted != null || !converted.equals("")) converted = converted.substring(0, converted.length() - 1);
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
