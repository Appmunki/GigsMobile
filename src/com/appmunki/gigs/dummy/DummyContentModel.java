package com.appmunki.gigs.dummy;

import android.content.Context;

import com.appmunki.gigs.restaurant.RestaurantModel;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContentModel {


	public static void createDummyContent(Context context) {
		// Add 3 sample items.
		if (RestaurantModel.readResturants(context).isEmpty()) {

			new RestaurantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);
			new RestaurantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);
			new RestaurantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);
			new RestaurantModel("First Resturant", "Test Description", 4.0, 0.0)
					.save(context, 0);

		}
	}

}
