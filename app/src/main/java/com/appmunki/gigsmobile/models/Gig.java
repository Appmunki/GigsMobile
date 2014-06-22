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
public class Gig implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String status;
    private float lat;
    private float longit;

    private int employer_id;
    private String updated_at;
    private User mWorker;

    public Gig(){
        //TODO: remove after testing
        Random random = new Random();
        id = random.nextInt(3);

        lat = random.nextFloat()*200;
        longit = random.nextFloat()*200;
    }
    public Gig(Parcel in){
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        status = in.readString();
        employer_id = Integer.parseInt(in.readString());
        updated_at = in.readString();
    }
    @Override
    public String toString() {
        return new StringBuffer("{title: ").append(title).append(", description: ").append(description).append(", status: ").append(status).append(", employer_id: ").append(employer_id).append("}").toString();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(description);
        out.writeString(status);
        out.writeString(String.valueOf(employer_id));
        out.writeString(updated_at);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Gig> CREATOR = new Parcelable.Creator<Gig>() {
        public Gig createFromParcel(Parcel in) {
            return new Gig(in);
        }

        public Gig[] newArray(int size) {
            return new Gig[size];
        }
    };


    public Bitmap getPicture(Context context){
        Random random = new Random();

        Bitmap bitmap=null;
        // load image
        try {
            // get input stream
            InputStream ims = context.getAssets().open("gig"+id+".jpg");
            bitmap= BitmapFactory.decodeStream(ims);

        }
        catch(Exception ex) {

        }
        return bitmap;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return 0;
    }

    public User getWorker() {
        return mWorker;
    }

    public void setLocation(float latitude, float longitude){
        this.lat = latitude;
        this.longit = longitude;
    }
    public float getLongitude(){return longit;}
    public float getLatitude(){return lat;}
    

    public int getId(){return id;}

}
