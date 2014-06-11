package com.appmunki.gigsmobile;

import android.content.Context;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by radzell on 6/2/14.
 */
@RunWith(RobolectricTestRunner.class)
public class DatabaseTest {
    private Context context;

    public void initContext() {
        context = Robolectric.application.getApplicationContext();
        assertNotNull(context);
    }

    @BeforeClass
    public static void setup() {
        // To redirect Robolectric to stdout
        System.setProperty("robolectric.logging", "stdout");
    }

    @Test
    public void getDatabaseHelperTest() {
        initContext();

        //DatabaseHelperManager databaseHelperManager = new DatabaseHelperManager();
        //DatabaseHelper databaseHelper = databaseHelperManager.getHelper(context);
        //assertNotNull(databaseHelper);

        //Log.d(DatabaseHelperManagerTest.class.getName(), "Database Path:" + context.getDatabasePath(DatabaseHelper.DATABASE_NAME));
    }
}
