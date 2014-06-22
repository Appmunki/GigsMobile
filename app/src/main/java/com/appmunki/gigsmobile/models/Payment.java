package com.appmunki.gigsmobile.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by diegoamezquita on 6/17/14.
 */
public class Payment implements Parcelable {

    private int gigId;
    private String token;
    private int amount;

    public Payment(int gigId,String token,int amount){
        this.gigId = gigId;
        this.token = token;
        this.amount = amount;
    }


    @Override
    public String toString() {
        return new StringBuffer("{gigId: ").append(gigId).append(", token: ").append(token).append(", amount: ").append(amount).append("}").toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(gigId);
        out.writeString(token);
        out.writeString(String.valueOf(amount));
    }

}
