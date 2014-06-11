package com.appmunki.gigsmobile.controllers;

import android.accounts.AccountAuthenticatorActivity;

import com.appmunki.gigsmobile.helpers.BusProvider;

/**
 * Created by radzell on 5/31/14.
 */
public class BaseAuthenticatorActivity extends AccountAuthenticatorActivity {
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

    /**
     * Post the event to the service bus
     * @param event
     * 		The event to dispatch on the service bus
     */
    protected void postEvent(Object event) {
        BusProvider.getInstance().post(event);
    }
}
