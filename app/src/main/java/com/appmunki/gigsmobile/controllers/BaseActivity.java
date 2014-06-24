package com.appmunki.gigsmobile.controllers;

import android.app.Activity;

import com.appmunki.gigsmobile.helpers.BusProvider;
import com.appmunki.gigsmobile.helpers.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

/**
 * Created by radzell on 5/29/14.
 */
public class BaseActivity extends Activity{

    /**
     * You'll need this in your class to cache the helper in the class.
     */
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onResume() {
        super.onResume();
        //register the activity to the service bus
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        /**activity must be removed from the service bus in onPause or an error will occur
         when the bus attempts to dispatch an event to the paused activity. **/
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        /*
		 * You'll need this in your class to release the helper when done.
		 */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
        super.onDestroy();
    }

    /**
     * Post the event to the service bus
     * @param event
     * 		The event to dispatch on the service bus
     */
    protected void postEvent(Object event) {
        BusProvider.getInstance().post(event);
    }

    /**
     * You'll need this in your class to get the helper from the manager once per class.
     */
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
