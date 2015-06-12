package com.example.bellng.trackall.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bellng.trackall.R;
import com.example.bellng.trackall.listitems.Package;

import java.util.List;

import Classes.AsyncTaskCompleteListener;
import Classes.ConnectionAPI;
import Classes.Courier;
import Enums.ConnectionAPIMethods;


public class AddPackageActivity extends Activity implements AsyncTaskCompleteListener<ConnectionAPI>  {

    Spinner spinner;
    EditText titleInput,trackingInput;
    List<Courier> couriers;

    private String API_KEY = "652c08bc-f1b1-45dd-99bf-0baa6d576f91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);
        setTitle("Add Package");

        titleInput = (EditText) findViewById(R.id.titleInput);
        trackingInput = (EditText) findViewById(R.id.trackingInput);


        spinner = (Spinner) findViewById(R.id.courierSpinner);

        // get the courier list and fill out the spinner with returned couriers
        new ConnectionAPI(API_KEY, ConnectionAPIMethods.getCouriers, this).execute();
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
            String title = titleInput.getText().toString();
            String tracking = trackingInput.getText().toString();

            //check if any of the fields are empty. if so, display an error dialog
            if(title == null || title.equals("") || tracking == null || tracking.equals("") || spinner.getSelectedItem() == null){
                new AlertDialog.Builder(this)
                        .setTitle("INVALID INPUT")
                        .setMessage("Please fill out all fields")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else {
                Courier type = (Courier) spinner.getSelectedItem();
                Intent i = new Intent();

                Package p = new Package(titleInput.getText().toString(), trackingInput.getText().toString(), type.getSlug());
                i.putExtra("item", p);
                setResult(RESULT_OK, i);
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method attempts to detect which courier the tracking number belongs to.
     * Sets the spinner to detected courier(s) if successful
     * @param v
     */
    public void detect(View v){
        String tracking = trackingInput.getText().toString();
        if(!(tracking == null || tracking.equals(""))) {
            new ConnectionAPI(API_KEY, ConnectionAPIMethods.detectCouriers, this, trackingInput.getText().toString()).execute();
        }
    }

    /**
     * Fill the spinner with items from "list"
     * @param list
     */
    public void setSpinnerItems(List<Courier> list){
        ArrayAdapter<Courier> adapter = new ArrayAdapter<Courier>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onTaskComplete(ConnectionAPI result) {

        if (result.getException()!=null) {
            System.out.println(result.getException().getMessage());//do something with the exception
            return;
            //TODO: HANDLE EXCEPTION PROPERLY
        }

        switch (result.getMethod().getNumberMethod()) {
            case 7://getCouriers(7)
                couriers = (List<Courier>) result.getReturn();
                setSpinnerItems(couriers);
                break;
            case 8://detectCouriers(8)
                couriers = (List<Courier>) result.getReturn(); //The detected Couriers
                if(couriers.size() > 0){
                    setSpinnerItems(couriers);
                }
                break;
        }
    }
}
