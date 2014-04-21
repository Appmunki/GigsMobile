package com.appmunki.gigs.restaurant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.orm.androrm.Model;
import com.orm.androrm.QuerySet;
import com.orm.androrm.field.CharField;
import com.orm.androrm.field.DoubleField;

import java.io.ByteArrayOutputStream;

public class RestaurantModel extends Model {
	// initializes the standard ID field
	// and sets it to autoincrement
	public RestaurantModel() {
		super(true);
		mTitle = new CharField();
		mDescription = new CharField();
		mPicture = new CharField();
		mRating = new DoubleField();

	}

	public RestaurantModel(String pTitle, String pDescription, double pRating) { 
		this();
		mTitle.set(pTitle);
		mDescription.set(pDescription);
		mRating.set(pRating);

	}

	protected CharField mTitle;
	protected CharField mDescription;
	protected DoubleField mRating;
	protected CharField mPicture;
	public String getTitle() {
		return mTitle.get();
	}
	public void setTitle(String pTitle) {
		this.mTitle.set(pTitle);
	}
	public String getDescription() {
		return mDescription.get();
	}
	public void setDescription(String pDescription) {
		this.mDescription.set(pDescription);
	}
	public Double getRating() {
		return mRating.get();
	}
	public void setRating(Double pRating) {
		this.mRating.set(pRating);
	}

	public Bitmap getPicture() {
		byte[] imgbytes = Base64.decode(mPicture.get(), Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(imgbytes, 0,
		imgbytes.length);
		return bitmap;
	}
	public void setPicture(Bitmap pPicture) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		pPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream .toByteArray();
		String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

		this.mPicture.set(encoded);
	}
	
	public static final QuerySet<RestaurantModel> readResturants(Context context) {
        return objects(context, RestaurantModel.class);
    }
	
	

}
