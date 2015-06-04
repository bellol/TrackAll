package com.example.bellng.trackall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.bellng.trackall.listitems.Package;

import java.util.ArrayList;
import java.util.Collections;

import Classes.Checkpoint;


public class ViewPackageActivity extends Activity {

    Package p;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package);

        Intent i = getIntent();
        p = (Package) i.getSerializableExtra("r");
        setTitle(p.getTitle());
        index = i.getIntExtra("index",0);

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
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.editText);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            p.title = editText.getText().toString();
                            setTitle(editText.getText().toString());
                            Intent intent = new Intent();
                            intent.putExtra("action","edit");
                            intent.putExtra("item",p);
                            intent.putExtra("index",index);
                            setResult(RESULT_OK, intent);
                            finish();
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
        }
        if (id == R.id.action_delete){
            Intent intent = new Intent();
            intent.putExtra("action","delete");
            intent.putExtra("index",index);
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
