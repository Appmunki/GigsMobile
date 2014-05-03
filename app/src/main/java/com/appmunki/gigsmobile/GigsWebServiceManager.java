package com.appmunki.gigsmobile;

import android.util.Log;

import com.appmunki.gigsmobile.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

import static retrofit.RestAdapter.LogLevel.FULL;



/**
 * Created by radzell on 4/27/14.
 */
public class GigsWebServiceManager {
    private static String TAG = "GigsWebServiceManager";

    /**
     * Used to add default header to a project
     */
    private static RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type","application/json");
        }
    };

    /**
     *
     */
    private static class UserSerializer implements JsonSerializer<User> {
        public JsonElement serialize(final User person, final Type type, final JsonSerializationContext context) {

            JsonObject result = new JsonObject();
            Gson gson = new Gson();
            result.add(User.class.getSimpleName().toLowerCase(), gson.toJsonTree(person).getAsJsonObject());
            return result;
        }
    }
    /**
     * Location of the webserver
     */
    private static String API_URL = "http://gigsbackend.herokuapp.com/api/v1";

    /**
     * Logger for the api
     */
    private static RestAdapter.Log log = new RestAdapter.Log() {
        public void log(String message) {
            Log.i(TAG,message);
        }
    };

    /**
     * Adapter use for using a rest api
     */
    private static RestAdapter restAdapter = new RestAdapter.Builder()

            .setEndpoint(API_URL)
            .setLog(log)
            .setLogLevel(FULL)
            .setConverter(new GsonConverter(new GsonBuilder().setPrettyPrinting().registerTypeAdapter(User.class, new UserSerializer()).create()))
            .setRequestInterceptor(requestInterceptor)
            .build();
    private static GigsWebService gigsWebService = null;

    public static GigsWebService getService(){
        if(gigsWebService==null)
            gigsWebService = restAdapter.create(GigsWebService.class);
        return gigsWebService;
    }

    public interface GigsWebService {
        /**
         * User rest calls
         */
        @POST("/sessions")
        void login(
                @Body User user,
                Callback<Response> cb
        );
        @POST("/registrations")
        void register(
                @Body User user,
                Callback<Response> cb
        );

        /**
         * Get request to retrieve gigs from an user
         * @param email email of the gig owner
         * @param authorization auth_token of the gig owner
         * @param cb callback of the webservice request
         */
        @GET("/gigs")
        void getUserGigs(@Header("X-User-Email") String email,@Header("X-User-Token") String authorization, Callback<Response> cb);

        /**
         * Workers rest calls
         */


        /**
         * Employers rest calls
         */

        /**
         * Gigs rest calls
         */
    }



}

