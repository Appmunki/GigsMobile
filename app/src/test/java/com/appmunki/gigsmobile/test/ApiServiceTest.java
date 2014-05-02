package com.appmunki.gigsmobile.test;

import android.util.Log;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.appmunki.gigsmobile.models.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.Transcript;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by radzell on 4/29/14.
 */
// Test class for MyActivity
@RunWith(RobolectricTestRunner.class)
public class ApiServiceTest {
    private String TAG=this.getClass().getSimpleName();


    private Transcript transcript;


    @Before
    public void setup() {
        transcript = new Transcript();
        ShadowLog.stream = System.out;
    }

    @Test
    public void testShouldPass() {
        Assert.assertTrue(Boolean.TRUE);
    }

    @Test
    public void testSigninPass() {
        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();

        String password = Mockito.anyString();
        GigsWebServiceManager.getService().login(new User("user4@example.com", "Radzell", "Employer"), LoginCallBack);

        Robolectric.getBackgroundScheduler().runOneTask();
        transcript.assertEventsSoFar("200");


        Robolectric.getUiThreadScheduler().runOneTask();
        for(String event :transcript.getEvents()){
            Log.i("event",event);
        }
        Log.i(TAG,"count:"+transcript.getEvents().size());
    }
    @Test
    public void testSignup() {
        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();

        String password = Mockito.anyString();

        GigsWebServiceManager.getService().register(new User(Mockito.anyString()+"@example.com",password,password,"Employer"),RegisterCallBack);

        Robolectric.getBackgroundScheduler().runOneTask();

        transcript.assertEventsSoFar("200");

        Robolectric.getUiThreadScheduler().runOneTask();

        for(String event :transcript.getEvents()){
            Log.i("event",event);
        }
        Log.i(TAG,"count:"+transcript.getEvents().size());
    }
    private Callback<Response> RegisterCallBack = new Callback<Response>() {
        private String TAG=this.getClass().getSimpleName();


        @Override
        public void success(Response response, Response response2) {

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };
    private Callback<Response> LoginCallBack=new Callback<Response>() {
        private String TAG=this.getClass().getSimpleName();





        @Override
        public void success(Response user, Response response) {

        }

        @Override
        public void failure(RetrofitError error) {
            Log.i(TAG, error.toString());

        }
    };

}
