package com.example.bellng.trackall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bellng.trackall.listitems.Package;

import Classes.Checkpoint;


public class ViewPackageActivity extends Activity {

    TextView checkpointLabel;
    Package p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package);

        Intent i = getIntent();
        p = (Package) i.getSerializableExtra("r");
        setTitle(p.getTitle());
        checkpointLabel = (TextView) findViewById(R.id.checkpoints);

        if(p.checkpoints != null) {
            String cplabel = "";
            for (Checkpoint c : p.checkpoints) {
                cplabel += (c + "\n");
            }
            checkpointLabel.setText(cplabel);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
