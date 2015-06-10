package com.example.bellng.trackall.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bellng.trackall.R;
import com.example.bellng.trackall.enums.CurrencyCode;
import com.example.bellng.trackall.listitems.XE;


public class AddXEActivity extends Activity {

    EditText amountInput;
    Spinner fromSpinner,toSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_xe);

        setTitle("Add Conversion");

        amountInput = (EditText) findViewById(R.id.amountInput);
        fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) findViewById(R.id.toSpinner);

        ArrayAdapter<CurrencyCode> adapter = new ArrayAdapter<CurrencyCode>(this,android.R.layout.simple_spinner_item,CurrencyCode.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_xe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            //TODO: validate inputs first
            try {
                String amountString = amountInput.getText().toString();
                XE xe = new XE("Currency Conversion", Integer.parseInt(amountString), fromSpinner.getSelectedItem().toString(), toSpinner.getSelectedItem().toString());
                Intent intent = new Intent();
                intent.putExtra("item", xe);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }catch(Exception e){
                new AlertDialog.Builder(this)
                        .setTitle("INVALID INPUT")
                        .setMessage("Please enter an integer")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
