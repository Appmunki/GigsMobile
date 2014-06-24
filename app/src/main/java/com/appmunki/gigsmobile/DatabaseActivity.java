package com.appmunki.gigsmobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appmunki.gigsmobile.controllers.BaseActivity;
import com.appmunki.gigsmobile.models.Gig;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;


public class DatabaseActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSampleDatabaseStuff("",null);
        setContentView(R.layout.activity_database);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Do our sample database stuff as an example.
     */
    private void doSampleDatabaseStuff(String action, TextView tv) {
        try {
            // get our dao
            Dao<Gig, Integer> gigDao = getDatabaseHelper().getGigDataDao();
            // query for all of the data objects in the database
            List<Gig> list = gigDao.queryForAll();

        } catch (SQLException e) {
            Log.e("", "Database exception", e);
            tv.setText("Database exeption: " + e);
            return;
        }
    }
}
