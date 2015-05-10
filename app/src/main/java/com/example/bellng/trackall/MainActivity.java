package com.example.bellng.trackall;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    public static final int ADD_ITEM_REQUEST = 0;

    private ListView itemListView;
    private ArrayList<ListItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemListView = (ListView) findViewById(R.id.itemListView);

        //TODO: this should be finding the list from the database instead of initializing a new list every time
        itemList = new ArrayList<ListItem>();

        itemListView.setAdapter(new ItemAdapter(this,itemList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_package) {
            Intent i = new Intent(this,AddPackageActivity.class);
            startActivityForResult(i,ADD_ITEM_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class UpdateItemsTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){
            return null;
        }
    }
}
