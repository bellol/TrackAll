package com.example.bellng.trackall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bellng.trackall.listitems.Myki;


public class AddAccountActivity extends Activity {

    Spinner spinner;
    EditText title,username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        setTitle("Add Account");

        title = (EditText) findViewById(R.id.titleInput);
        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);

        spinner = (Spinner) findViewById(R.id.accountSpinner);
        ArrayAdapter<AccountType> adapter = new ArrayAdapter<AccountType>(this,android.R.layout.simple_spinner_item,AccountType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_account) {
            AccountType type = (AccountType) spinner.getSelectedItem();
            Intent i = new Intent();
            switch(type){
                case Myki:
                    Myki myki = new Myki(title.getText().toString(),username.getText().toString(),password.getText().toString());
                    i.putExtra("item", myki);
                    break;
            }
            setResult(RESULT_OK,i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
