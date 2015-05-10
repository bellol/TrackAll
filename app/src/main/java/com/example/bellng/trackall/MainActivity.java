package com.example.bellng.trackall;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    public static final int ADD_PACKAGE_REQUEST = 0;

    private SwipeRefreshLayout swipeLayout;
    private ListView itemListView;
    private ItemAdapter itemAdapter;
    private ArrayList<ListItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemListView = (ListView) findViewById(R.id.itemListView);
        registerForContextMenu(itemListView);

        itemList = new ArrayList<ListItem>(); //TODO: this should be finding the list from the database instead of initializing a new list every time

        itemAdapter = new ItemAdapter(this,itemList);
        itemListView.setAdapter(itemAdapter);
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
            startActivityForResult(i, ADD_PACKAGE_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PACKAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                ListItem item = data.getParcelableExtra("item");
                itemList.add(item);
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.itemListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_onhold_item, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.action_edit:
                //TODO: allow for input to change title of item (itemList.get(info.position))
                return true;
            case R.id.action_delete:
                itemList.remove(info.position);
                itemAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private class UpdateItemsTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){
            return null;
        }
    }


}
