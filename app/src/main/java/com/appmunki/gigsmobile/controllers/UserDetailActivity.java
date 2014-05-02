package com.appmunki.gigsmobile.controllers;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.models.Gig;
import com.appmunki.gigsmobile.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

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

public class UserDetailActivity extends Activity {
    private String TAG = this.getClass().getSimpleName();
    /**
     * UI references.
     */
    @InjectView(R.id.gigslistView)
    ListView mGigsListView;
    @InjectView(R.id.useremailtextView)
    TextView mUserEmailTextView;

    /**
     * Gigs adapter
     */
    GigAdapter mGigAdapter;

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

            mGigAdapter.addAll(gigsList);
            mGigAdapter.notifyDataSetChanged();

            Log.i(TAG,gigsList.get(0).toString());
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getBaseContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.activity_user_detail_header)
                .contentLayout(R.layout.activity_user_detail);
        setContentView(helper.createView(this));
        helper.initActionBar(this);
        ButterKnife.inject(this);

        User user = getIntent().getParcelableExtra("user");
        Log.i(TAG,user.toString());

        mUserEmailTextView.setText(user.getEmail());

        mGigAdapter = new GigAdapter(this,0,new ArrayList<Gig>());
        //Load the initial gigs
        GigsWebServiceManager.getService().getUserGigs(user.getEmail(),user.getAuthToken(), ListGigsCallback);


    }
    @OnItemClick(R.id.gigslistView)
    void onGigItemSelected(int position) {
        Intent intent = new Intent(this,GigDetailActivity.class);
        intent.putExtra("gig",mGigAdapter.getItem(position));

        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_detail, menu);
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

}
