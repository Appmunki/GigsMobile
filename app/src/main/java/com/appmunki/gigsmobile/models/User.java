package com.appmunki.gigsmobile.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by radzell on 4/27/14.
 */
public class User implements Parcelable {
    private String email;
    private String password;
    private String password_confirmation;
    private String authentication_token;
    private String type;

    public User(Parcel in){
        email = in.readString();
        password = in.readString();
        password_confirmation = in.readString();
        authentication_token = in.readString();
        type = in.readString();
    }
    public User(){

    }
    public User(String pEmail, String pPassword, String pType) {
        this.email = pEmail;
        this.password = pPassword;
        this.type = pType;
    }
    public User(String pEmail, String pPassword,String pPassword_confirmation, String pType) {
        this.email = pEmail;
        this.password = pPassword;
        this.password_confirmation = pPassword_confirmation;
        this.type = pType;
    }

    public String getAuthToken() {
        return authentication_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(email);
        out.writeString(password);
        out.writeString(password_confirmation);
        out.writeString(authentication_token);
        out.writeString(type);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return new StringBuffer("{ email: ").append(email).append(", password: ").append(password).append(", password_confirmation: ").append(password_confirmation).append(", auth_token: ").append(authentication_token).append(", type: ").append(type).append("}").toString();
    }

    public String getEmail() {
        return email;
    }
    public Bitmap getPicture(Context context){
        Random random = new Random();

        Bitmap bitmap=null;
        // load image
        try {
            // get input stream
            InputStream ims = context.getAssets().open("user"+random.nextInt(5)+".jpg");
            bitmap=BitmapFactory.decodeStream(ims);

        }
        catch(Exception ex) {

        }
        return bitmap;
    }
}
