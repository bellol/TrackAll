package com.example.bellng.trackall;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bellng.trackall.listitems.Package;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends Activity {

    public static final int ADD_ITEM_REQUEST = 0;

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

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItem result = (ListItem) itemListView.getAdapter().getItem(i);
                if(result instanceof Package){
                    Intent intent = new Intent(getApplicationContext(),ViewPackageActivity.class);
                    intent.putExtra("r", (Serializable) result);
                    startActivity(intent);
                }
                /*
                Intent intent = new Intent(getApplicationContext(), .class);
                intent.putExtra("r", result);
                intent.putExtra("index", i);
                startActivityForResult(intent,VIEW_PACKAGE_REQUEST);
                */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        itemAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_package) {
            Intent i = new Intent(this,AddPackageActivity.class);
            startActivityForResult(i, ADD_ITEM_REQUEST);
            return true;
        }
        if (id == R.id.action_add_account){
            Intent i = new Intent(this,AddAccountActivity.class);
            startActivityForResult(i, ADD_ITEM_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                //ListItem item = data.getParcelableExtra("item");
                ListItem item = (ListItem) data.getSerializableExtra("item");
                item.update();
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
