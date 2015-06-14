package com.example.bellng.trackall.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bellng.trackall.R;
import com.example.bellng.trackall.listitems.ASX;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class AddASXActivity extends Activity {

    ListView listOfCompanies;
    ArrayList<String> companyList;
    ASXAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asx);

        // The array
        companyList = new ArrayList<String>();

        // The ListView
        listOfCompanies = (ListView) findViewById(R.id.listOfCompanies);

        // Async task to retrieve the ASX 50 and set the listView
        new RetrieveTopFiftyTask().execute("http://www.asx.com.au/asx/widget/topCompanies.do");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_add_asx, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    class RetrieveTopFiftyTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String code,company;

                // Set the user agent to avoid being detected as a bot
                String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";

                // Get the document from the passed URL
                Document doc = Jsoup.connect(urls[0]).userAgent(ua).get();

                // For every table row (<tr>) in <tbody>
                for(Element e : doc.select("tbody").select("tr")){
                    code = e.select("td[class=code]").first().ownText();
                    company = e.select("a[title^=Company information for]").first().ownText();
                    companyList.add(code + " - " + company);
                }

                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            adapter = new ASXAdapter(getApplicationContext(),companyList);
            listOfCompanies.setAdapter(adapter);

            listOfCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String result = (String) listOfCompanies.getAdapter().getItem(i);

                    // Grab the ticker code from the title (first three characters)
                    String ticker = result.substring(0,3);

                    // Grab the company name from the title
                    String company = result.substring(6);

                    // Creates the object and sends it back via an intent
                    ASX asx = new ASX(ticker + " Stock Price",ticker,company);
                    Intent intent = new Intent();
                    intent.putExtra("item",asx);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    class ASXAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> companies;

        public ASXAdapter(Context context, ArrayList<String> companies){
            this.context = context;
            this.companies = companies;
        }

        public int getCount(){
            return companies.size();
        }

        @Override
        public String getItem(int i) {
            return companies.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_add_asx, null);
            }

            TextView title = (TextView) view.findViewById(R.id.titleLabel);

            String item = companies.get(i);

            title.setText(item);

            return view;
        }

    }
}
