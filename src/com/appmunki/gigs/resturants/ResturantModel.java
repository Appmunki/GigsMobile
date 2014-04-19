package com.appmunki.gigs.resturants;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.orm.androrm.Model;
import com.orm.androrm.QuerySet;
import com.orm.androrm.field.CharField;
import com.orm.androrm.field.DoubleField;

public class ResturantModel extends Model {
	// initializes the standard ID field
	// and sets it to autoincrement
	public ResturantModel() {
		super(true);
		mTitle = new CharField();
		mDescription = new CharField();
		mRatingS = new DoubleField();
		mRatingY = new DoubleField();
		mPicture = new CharField();
	}

	public ResturantModel(String pTitle, String pDescription, double pRatingS,
			double pRatingY) { 
		this();
		mTitle.set(pTitle);
		mDescription.set(pDescription);
		mRatingS.set(pRatingS);
		mRatingY.set(pRatingY);

	}

	protected CharField mTitle;
	protected CharField mDescription;
	protected DoubleField mRatingS;
	protected DoubleField mRatingY;
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
	public Double getRatingS() {
		return mRatingS.get();
	}
	public void setRatingS(Double pRatingS) {
		this.mRatingS.set(pRatingS);
	}
	public Double getRatingY() {
		return mRatingY.get();
	}
	public void setRatingY(Double pRatingY) {
		this.mRatingY.set(pRatingY);
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
	
	public static final QuerySet<ResturantModel> readResturants(Context context) {
        return objects(context, ResturantModel.class);
    }
	
	

}
