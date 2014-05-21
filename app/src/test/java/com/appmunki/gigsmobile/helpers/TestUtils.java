package com.appmunki.gigsmobile.helpers;

import android.util.Log;

import com.appmunki.gigsmobile.models.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit.client.Response;
import retrofit.mime.TypedString;

/**
 * Created by radzell on 5/15/14.
 */
public class TestUtils {
    private String TAG = this.getClass().getSimpleName();

    public Response createOKUserSigninResponse(User user){
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true);
            jsonObject.put("info", "you successfully logged in");
            jsonObject.put("data", gson.toJson(user));
            Response response = new Response("", 200, "Ok", null, new TypedString(jsonObject.toString()));

            return response;
        }catch (Exception e){
            Log.i(TAG,"Error parsing json");
            Response response = new Response("", 500, "Error", null, new TypedString("Error parsing json"));
            return response;
        }
    }

    public Response createBadSigninResponse(User user){
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", false);
            jsonObject.put("info", "you didn't login a user");
            Response response = new Response("", 401, "Not Ok", null, new TypedString(""));

            return response;
        }catch (Exception e){
            Log.i(TAG,"Error parsing json");
            Response response = new Response("", 500, "Error", null, new TypedString("Error parsing json"));
            return response;
        }
    }
    public Response createOKUserSignupResponse(User user){
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true);
            jsonObject.put("info", "you created a user");
            jsonObject.put("data", gson.toJson(user));
            Response response = new Response("", 200, "Ok", null, new TypedString(jsonObject.toString()));

            return response;
        }catch (Exception e){
            Log.i(TAG,"Error parsing json");
            Response response = new Response("", 500, "Error", null, new TypedString("Error parsing json"));
            return response;
        }
    }

    public Response createBadSignupResponse(User user){
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", false);
            jsonObject.put("info", "you didn't create a user");
            Response response = new Response("", 401, "Not Ok", null, new TypedString(""));

            return response;
        }catch (Exception e){
            Log.i(TAG,"Error parsing json");
            Response response = new Response("", 500, "Error", null, new TypedString("Error parsing json"));
            return response;
        }
    }
}
