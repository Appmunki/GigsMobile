package com.appmunki.gigsmobile.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.appmunki.gigsmobile.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GigCreateActivity extends Activity {

    //defining views
    @InjectView(R.id.create_gignameeditText)
    EditText gignameEditText;
    @InjectView(R.id.create_gigdescreditText)
    EditText gigdescriptionEditText;
    @InjectView(R.id.create_giglocationCompleteTextView)
    AutoCompleteTextView giglocationCompleteTextView;
    @InjectView(R.id.create_gigbudgeteditText)
    EditText gigBudgetEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig_form);
        ButterKnife.inject(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_gig, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_create_create_gig:
                saveGig();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGig() {
        finish();
    }
}
