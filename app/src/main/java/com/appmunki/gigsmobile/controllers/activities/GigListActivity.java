package com.appmunki.gigsmobile.controllers.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.adapters.GigAdapter;
import com.appmunki.gigsmobile.helpers.Utils;
import com.appmunki.gigsmobile.models.Gig;
import com.appmunki.gigsmobile.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.appmunki.gigsmobile.helpers.Utils.IntoString;

public class GigListActivity extends Activity {
    /**
     * UI references.
     */
    @InjectView(R.id.gigslistView)
    ListView mGigsListView;
    /**
     * Gigs adapter
     */
    GigAdapter mGigAdapter;
    private ImageView mGigHeaderMapImageView;
    private TextView mGigHeaderTextView;
    private String TAG = this.getClass().getSimpleName();
    /**
     *
     */
    private Callback<Response> ListGigsCallback = new Callback<Response>() {
        private String TAG = this.getClass().getSimpleName();

        @Override
        public void success(Response gigs, Response response) {
            Log.i(TAG, Arrays.toString(response.getHeaders().toArray()));
            Log.i(TAG, String.valueOf(response.getStatus()));

            String body = null;
            try {
                body = IntoString(response.getBody().in());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, body);


            JsonObject resp = new JsonParser().parse(body).getAsJsonObject();
            Toast.makeText(getApplicationContext(), resp.getAsJsonPrimitive("info").getAsString(), Toast.LENGTH_SHORT).show();


            //data from the http
            Type listType = new TypeToken<ArrayList<Gig>>() {
            }.getType();
            List<Gig> gigsList = new Gson().fromJson(resp.getAsJsonArray("data"), listType);
            boolean success = resp.getAsJsonPrimitive("success").getAsBoolean();
            String info = resp.getAsJsonPrimitive("info").getAsString();


            updatedHeader();

            mGigAdapter.addAll(gigsList);
            mGigAdapter.notifyDataSetChanged();

        }

        @Override
        public void failure(RetrofitError error) {

            Toast.makeText(getBaseContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private User mUser;

    private void updatedHeader() {
        Picasso.with(this).load("http://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=900x400&maptype=roadmap%20&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318%20&markers=color:red%7Clabel:C%7C40.718217,-73.998284&sensor=false").error(R.drawable.gig_placeholder).placeholder(R.drawable.gig_placeholder).fit().centerCrop().into(mGigHeaderMapImageView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_gig_list);
        ButterKnife.inject(this);
        addHeader();

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        if (savedInstanceState != null) {
            mUser = savedInstanceState.getParcelable(Utils.ARG_USER);
        } else {
            mUser = getIntent().getParcelableExtra(Utils.ARG_USER);
        }
        Log.i(TAG, "" + savedInstanceState);
        Log.i(TAG, mUser.toString());


        ArrayList<Gig> gigsList = new ArrayList<Gig>();
        mGigAdapter = new GigAdapter(this, R.layout.gig_list_item, gigsList);
        mGigsListView.setAdapter(mGigAdapter);
        //Load the initial gigs
        GigsWebServiceManager.getService().getUserGigs(mUser.getEmail(), mUser.getAuthToken(), ListGigsCallback);
    }


    /**
     * Add the total information from the gig list information
     */
    private void addHeader() {
        View header = getLayoutInflater().inflate(R.layout.activity_gig_list_header, null);
        mGigHeaderMapImageView = (ImageView) header.findViewById(R.id.gigheaderimageView);
        mGigHeaderTextView = (TextView) header.findViewById(R.id.gigheadercounttextView);
        mGigsListView.addHeaderView(header);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mUser = savedInstanceState.getParcelable(Utils.ARG_USER);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelable(Utils.ARG_USER, mUser);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gig_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_create_gig) {
            Intent intent = new Intent(this, GigCreateActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.gigslistView)
    void onGigItemSelected(int position) {
        //Intent intent = new Intent(this, GigDetailActivity.class);
        //intent.putExtra("gig", mGigAdapter.getItem(position));

        //startActivity(intent);
    }
}
