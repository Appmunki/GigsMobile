package com.appmunki.gigs.review;

import com.orm.androrm.Model;
import com.orm.androrm.field.CharField;
import com.orm.androrm.field.DoubleField;

public class ReviewModel extends Model {
	// initializes the standard ID field
	// and sets it to autoincrement
	public ReviewModel() {
		super(true);
		mTitle = new CharField();
		mComment = new CharField();
		mRating = new DoubleField();

	}

	public ReviewModel(String pTitle, String pComment, double pRating) { 
		this();
		mTitle.set(pTitle);
		mComment.set(pComment);
		mRating.set(pRating);

	}
	protected CharField mTitle;
	protected CharField mComment;
	protected DoubleField mRating;
	
	public String getTitle() {
		return mTitle.get();
	}
	public void setTitle(String pTitle) {
		this.mTitle.set(pTitle);
	}
	public String getComment() {
		return mComment.get();
	}
	public void setComment(String pComment) {
		this.mComment.set(pComment);
	}
	public Double getRating() {
		return mRating.get();
	}
	public void setRating(Double pRatingS) {
		this.mRating.set(pRatingS);
	}
}
