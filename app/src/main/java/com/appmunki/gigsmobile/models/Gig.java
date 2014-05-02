package com.appmunki.gigsmobile.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by radzell on 4/27/14.
 */
public class Gig implements Parcelable {
    private String title;
    private String description;
    private String status;
    private int employer_id;
    private String updated_at;

    public Gig(){

    }
    public Gig(Parcel in){
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
}
