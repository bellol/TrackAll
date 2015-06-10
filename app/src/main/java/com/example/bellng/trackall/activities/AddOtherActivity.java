package com.example.bellng.trackall.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bellng.trackall.R;
import com.example.bellng.trackall.enums.OtherItem;
import com.example.bellng.trackall.listitems.ListItem;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class AddOtherActivity extends Activity {

    public static final int ADD_REQUEST = 0;
    ListView listView;
    ArrayList<OtherItem> items;
    OtherAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other);

        setTitle("Add Other Item");
        // grab the listview
        listView = (ListView) findViewById(R.id.listView);

        // populate the array with all possible "otheritem"s
        items = new ArrayList<OtherItem>(Arrays.asList(OtherItem.values()));

        adapter = new OtherAdapter(this,items);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OtherItem result = (OtherItem) listView.getAdapter().getItem(i);

                if (result == OtherItem.XE) {
                    Intent intent = new Intent(getApplicationContext(), AddXEActivity.class);
                    startActivityForResult(intent, ADD_REQUEST);
                }

                if (result == OtherItem.ASX){
                    Intent intent = new Intent(getApplicationContext(), AddASXActivity.class);
                    startActivityForResult(intent, ADD_REQUEST);
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_REQUEST){
            if(resultCode == RESULT_OK){
                ListItem item = (ListItem) data.getSerializableExtra("item");
                Intent i = new Intent();
                i.putExtra("item",(Serializable) item);
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adapter for this view
     */
    class OtherAdapter extends BaseAdapter {

        Context context;
        ArrayList<OtherItem> otherItems;

        public OtherAdapter(Context context, ArrayList<OtherItem> otherItems){
            this.context = context;
            this.otherItems = otherItems;
        }

        public int getCount(){
            return otherItems.size();
        }

        @Override
        public OtherItem getItem(int i) {
            return otherItems.get(i);
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
                view = inflater.inflate(R.layout.list_add_other, null);
            }

            TextView title = (TextView) view.findViewById(R.id.titleLabel);
            ImageView image = (ImageView) view.findViewById(R.id.imageView);

            OtherItem item = otherItems.get(i);

            title.setText(item.toString());
            Picasso.with(context).load(item.getIconURL()).into(image);

            return view;
        }

    }
}
