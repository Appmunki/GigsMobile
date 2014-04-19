package com.appmunki.gigs.dummy;

import android.content.Context;

import com.appmunki.gigs.resturants.ResturantModel;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContentModel {


	public static void createDummyContent(Context context) {
		// Add 3 sample items.
		if (ResturantModel.readResturants(context).isEmpty()) {

			new ResturantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);
			new ResturantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);
			new ResturantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);
			new ResturantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);

		}
	}

}
