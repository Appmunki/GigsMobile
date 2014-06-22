package com.appmunki.gigsmobile.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.appmunki.gigsmobile.models.Gig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

import static com.appmunki.gigsmobile.helpers.Utils.IntoString;

/**
 * Created by radzell on 5/18/14.
 */
public class GigSyncAdapter extends AbstractThreadedSyncAdapter {
    private String TAG = this.getClass().getSimpleName();

    public GigSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public GigSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        //connect to server
        Response response = GigsWebServiceManager.getService().getUserGigs("","");
        String body = null;
        try {
            body = IntoString(response.getBody().in());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, body);


        JsonObject resp = new JsonParser().parse(body).getAsJsonObject();

        //data from the http
        Type listType = new TypeToken<ArrayList<Gig>>() {
        }.getType();


        List<Gig> gigsList = new Gson().fromJson(resp.getAsJsonArray("data"), listType);
        //download and upload data
        //handle data conflicts
        //clean up
    }
}
