package com.appmunki.gigs.resturants;

import com.orm.androrm.Model;
import com.orm.androrm.field.BlobField;
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
		mPicture = new BlobField();
	}

	protected CharField mTitle;
	protected CharField mDescription;
	protected DoubleField mRatingS;
	protected DoubleField mRatingY;
	protected BlobField mPicture;

}
