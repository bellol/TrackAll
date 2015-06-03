package com.example.bellng.trackall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bellng.trackall.listitems.Package;


public class AddPackageActivity extends Activity {

    Spinner spinner;
    EditText titleInput,trackingInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);
        setTitle("Add Package");

        titleInput = (EditText) findViewById(R.id.titleInput);
        trackingInput = (EditText) findViewById(R.id.trackingInput);

        spinner = (Spinner) findViewById(R.id.courierSpinner);
        ArrayAdapter<PackageType> adapter = new ArrayAdapter<PackageType>(this,android.R.layout.simple_spinner_item,PackageType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_package, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_package) {
            //TODO: check if it is valid and if so, create the object and pass it back to main
            PackageType type = (PackageType) spinner.getSelectedItem();
            Intent i = new Intent();

            Package p = new Package(titleInput.getText().toString(), trackingInput.getText().toString(),type.getSlugName());
            i.putExtra("item",p);
            setResult(RESULT_OK, i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
