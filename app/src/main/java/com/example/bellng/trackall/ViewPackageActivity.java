package com.example.bellng.trackall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.bellng.trackall.listitems.Package;

import java.util.ArrayList;
import java.util.Collections;

import Classes.Checkpoint;


public class ViewPackageActivity extends Activity {

    Package p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package);

        Intent i = getIntent();
        p = (Package) i.getSerializableExtra("r");
        setTitle(p.getTitle());

        ListView checkpointList = (ListView) findViewById(R.id.checkpointList);

        if(p.checkpoints != null) {
            ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>(p.checkpoints);

            Collections.reverse(checkpoints);

            CheckpointAdapter adapter = new CheckpointAdapter(this, checkpoints);

            checkpointList.setAdapter(adapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_package, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            return true;
        }
        if (id == R.id.action_delete){
            Intent intent = new Intent();
            intent.putExtra("action","delete");
            intent.putExtra("item",p);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        if (id == R.id.action_refresh){
            p.update();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
