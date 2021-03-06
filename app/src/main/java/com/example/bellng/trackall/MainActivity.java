package com.example.bellng.trackall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.bellng.trackall.activities.AddOtherActivity;
import com.example.bellng.trackall.activities.AddPackageActivity;
import com.example.bellng.trackall.activities.ViewPackageActivity;
import com.example.bellng.trackall.listitems.ListItem;
import com.example.bellng.trackall.listitems.Package;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends Activity {

    public static final int ADD_ITEM_REQUEST = 0;
    public static final int VIEW_PACKAGE_REQUEST = 1;

    private ListView itemListView;
    private ItemAdapter itemAdapter;
    private ArrayList<ListItem> itemList;
    private PullRefreshLayout pullRefresh;
    private DatabaseHelper dbHelper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        dbHelper = new DatabaseHelper(getApplicationContext());
        pullRefresh = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        itemListView = (ListView) findViewById(R.id.itemListView);
        registerForContextMenu(itemListView);

        // Get all the items from the database and update the status
        itemList = dbHelper.getAllItems();
        for(ListItem li : itemList){
            li.update();
        }

        itemAdapter = new ItemAdapter(this,itemList);
        itemListView.setAdapter(itemAdapter);

        // Set the list to be clickable
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItem result = (ListItem) itemListView.getAdapter().getItem(i);

                // start the view package activity if the tapped item is a package
                if (result instanceof Package) {
                    Intent intent = new Intent(getApplicationContext(), ViewPackageActivity.class);
                    intent.putExtra("r", (Serializable) result);
                    intent.putExtra("index", i);
                    startActivityForResult(intent, VIEW_PACKAGE_REQUEST);
                }

            }
        });

        // This is called when the list is pulled down (to refresh)
        pullRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for(ListItem i : itemList){
                    i.update();
                }
                handler.postDelayed(updateListView, 1000);
            }
        });

        handler.postDelayed(updateListView, 2000);
    }

    /**
     * This task polls the item list and will stop the refreshing animation when it has completed
     *
     */
    private Runnable updateListView = new Runnable() {
        public void run() {
            if(stillRefreshing()){
                handler.postDelayed(updateListView,500);
            }else {
                itemAdapter.notifyDataSetChanged();
                pullRefresh.setRefreshing(false);
            }
        }
    };

    /**
     * This sees if any of the items in itemList are in the process of updating
     * @return
     */
    public boolean stillRefreshing(){
        for(ListItem i : itemList){
            if(i.isUpdating()) return true;
        }
        return false;
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

        if (id == R.id.action_add_package) {
            Intent i = new Intent(this,AddPackageActivity.class);
            startActivityForResult(i, ADD_ITEM_REQUEST);
            return true;
        }
        if (id == R.id.action_add_other){
            Intent i = new Intent(this,AddOtherActivity.class);
            startActivityForResult(i, ADD_ITEM_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                ListItem item = (ListItem) data.getSerializableExtra("item");
                item.addToDatabase(dbHelper);
                item.update();

                //add the item to the list that we display
                itemList.add(item);
                itemAdapter.notifyDataSetChanged();

                // check if the item has updated after 2000ms and then refresh the list
                handler.postDelayed(updateListView, 2000);
            }
        }

        if (requestCode == VIEW_PACKAGE_REQUEST) {
            if(resultCode == RESULT_OK){
                String action = data.getStringExtra("action");
                if(action.equals("delete")) {
                    int index = data.getIntExtra("index",0);
                    itemList.get(index).deleteFromDatabase(dbHelper);
                    itemList.remove(index);
                    itemAdapter.notifyDataSetChanged();
                }
                if(action.equals("edit")){
                    ListItem item = (ListItem) data.getSerializableExtra("item");
                    int index = data.getIntExtra("index",0);
                    itemList.get(index).editName(dbHelper,item.getTitle());
                    itemAdapter.notifyDataSetChanged();
                }
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
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.action_edit: // Create a popup input dialog to edit name
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(promptView);

                final EditText editText = (EditText) promptView.findViewById(R.id.editText);
                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ListItem li = itemList.get(info.position);
                                li.editName(dbHelper,editText.getText().toString());

                                // Remove the old item and replace it with the one with the edited name
                                itemList.remove(info.position);
                                itemList.add(li);

                                itemAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                return true;
            case R.id.action_delete:
                itemList.get(info.position).deleteFromDatabase(dbHelper);
                itemList.remove(info.position);
                itemAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    /**
     * The adapter for the listview in this activity
     */
    class ItemAdapter extends BaseAdapter {
        Context context;
        ArrayList<ListItem> items;

        public ItemAdapter(Context context, ArrayList<ListItem> items){
            this.context = context;
            this.items = items;
        }

        public int getCount(){
            return items.size();
        }

        public ListItem getItem(int i){
            return items.get(i);
        }

        public long getItemId(int i){
            return i;
        }

        public View getView(int i, View view, ViewGroup viewGroup){
            if(view == null){
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, null);
            }

            TextView title = (TextView) view.findViewById(R.id.titleLabel);
            TextView description = (TextView) view.findViewById(R.id.descLabel);
            ImageView image = (ImageView) view.findViewById(R.id.imageView);

            ListItem item = items.get(i);

            title.setText(item.getTitle());
            description.setText(item.getDescription());
            Picasso.with(context).load(item.getImageURL()).into(image);

            return view;
        }
    }
}
