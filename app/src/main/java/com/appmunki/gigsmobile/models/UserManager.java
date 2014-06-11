package com.appmunki.gigsmobile.models;

import android.util.Log;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.otto.Produce;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.appmunki.gigsmobile.helpers.Utils.IntoString;

/**
 * Created by radzell on 5/29/14.
 */
public class UserManager extends BaseManager{
    private static UserManager mManager;
    private static User mCurrentUser;

    public static UserManager getInstance() {
        if (mManager == null) {
            mManager = new UserManager();
        }

        return mManager;
    }

    public static User getCurrentUser(){
        return mCurrentUser;
    }

    /**
     * Authenticate the user
     *
     * @param email
     *            Email for the account
     * @param password
     *            Password for the account
     */
    public void authenticate(String email, String password,String type) {
        postEvent(produceUserSignInStartEvent());
        GigsWebServiceManager.getInstance().login(new User(email, password, type), LoginCallback);
    }

    /**
     * Create new user. If the user email already exists an error will be
     * dispatched via the service bus
     *
     * @param email
     *            The user email for the new account
     * @param password
     *            Password for the new account
     */
    public void signUp( String email, String password,String passwordconfirm,String type) {
        postEvent(produceUserSignInStartEvent());
        GigsWebServiceManager.getInstance().register(new User(email, password, passwordconfirm,type), RegisterCallBack);
    }

    /**
     * Creates an event notifying that authentication has begun
     *
     * @return
     */
    @Produce
    public AuthenticateUserStartEvent produceUserSignInStartEvent() {
        return new AuthenticateUserStartEvent();
    }

    /**
     * Creates an event containing the signed in user
     *

     * @return
     */
    @Produce
    public AuthenticateUserSuccessEvent produceUserSignInSuccessEvent() {
        return new AuthenticateUserSuccessEvent();
    }

    /**
     * Creates an even for sign in errors
     *
     * @return
     */
    @Produce
    public AuthenticateUserErrorEvent produceUserSignInErrorEvent(
            RetrofitError exception) {
        return new AuthenticateUserErrorEvent(exception);
    }


    /**
     * Represents an asynchronous signup task used to register
     * the user.
     */
    @VisibleForTesting
    private Callback<Response> RegisterCallBack = new retrofit.Callback<Response>() {


        @Override
        public void success(Response response, Response response2) {
            Log.i("test","register sign in");

            Gson gson = new Gson();

            String body = null;
            try {
                body = IntoString(response.getBody().in());
            } catch (IOException e) {
                e.printStackTrace();
            }


            JsonObject resp = new JsonParser().parse(body).getAsJsonObject();

            //parse the information from the server.
            JsonObject data = resp.getAsJsonObject("data");

            //data from the http
            User user = gson.fromJson(data, User.class);
            mCurrentUser=user;
            postEvent(produceUserSignInSuccessEvent());

        }

        @Override
        public void failure(RetrofitError error) {
            postEvent(produceUserSignInErrorEvent(error));

        }
    };
    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    private Callback<Response> LoginCallback = new Callback<Response>(){

        /**
         * Successful HTTP response.
         *
         * @param response response to the server
         * @param response2 response from the server
         */
        @Override
        public void success(Response response, Response response2) {
            Log.i("test","success sign in");
            Gson gson = new Gson();


            String body = null;
            try {
                body = IntoString(response.getBody().in());
            } catch (IOException e) {
                e.printStackTrace();
            }


            JsonObject resp = new JsonParser().parse(body).getAsJsonObject();

            //parse the information from the server.
            JsonObject data = resp.getAsJsonObject("data");

            //data from the http

            mCurrentUser = gson.fromJson(data, User.class);
            postEvent(produceUserSignInSuccessEvent());


        }



        /**
         * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
         * exception.
         *
         * @param error error coming back from the server
         */
        @Override
        public void failure(RetrofitError error) {
            postEvent(produceUserSignInErrorEvent(error));

        }

    };
}
