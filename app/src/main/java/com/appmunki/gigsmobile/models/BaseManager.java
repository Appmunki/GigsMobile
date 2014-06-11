package com.appmunki.gigsmobile.models;

import com.appmunki.gigsmobile.helpers.BusProvider;

/**
 * Created by radzell on 5/29/14.
 */
public class BaseManager {
    protected void postEvent(Object event){
        BusProvider.getInstance().post(event);
    }
}
