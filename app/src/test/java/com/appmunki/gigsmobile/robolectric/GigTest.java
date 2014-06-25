package com.appmunki.gigsmobile.robolectric;

import android.util.Log;

import com.appmunki.gigsmobile.controllers.MainActivity;
import com.appmunki.gigsmobile.helpers.DatabaseHelper;
import com.appmunki.gigsmobile.models.Gig;
import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by radzell on 6/16/14.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GigTest{
    private final String TAG = this.getClass().getSimpleName();
    private MainActivity activity;
    private Dao<Gig, Integer> gigDao;
    private DatabaseHelper databaseHelper;

    @Before
    public void setup() throws SQLException{
        System.setProperty("robolectric.logging", "stdout");
        Log.i(TAG,"Setup");

        //do whatever is necessary before every test
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        databaseHelper = activity.getDatabaseHelper();
        gigDao = databaseHelper.getGigDataDao();
    }


    @After
    public void teardown(){
        Log.i(TAG,"tearDown");

    }

    @Test
    public void testDataHelper() throws SQLException{
        Log.i(TAG,"testDataHelper");
        assertNotNull(databaseHelper);
        assertTrue(databaseHelper.isOpen());
        assertTrue(gigDao.isTableExists());
    }

    @Test
    public void testCreateGig() throws SQLException{
        Log.i(TAG, "testCreateddGig");


        Date today = new Date();
        // get our dao
        Gig gig = new Gig();
        gig.setId(1);
        gig.setTitle("I am walking the dog");
        gig.setDescription("I need someone to talk the dog while I am at work. They should" +
                " be " +
                "pretty good with dogs. The keys will be left with a neighbor. You just need to " +
                "go get the keys take paulie for a walk. Als if you could fill up his water and " +
                "food bowl if they are low. The food is in the cabinent under the sink.");
        gig.setLocation(10.10010f, 10.10102f);
        gig.setStatus("Pending");
        gig.setEmployerID(10);
        gig.setWorkerID(9);
        gig.setUpdatedAt(today);
        Dao.CreateOrUpdateStatus saved = gigDao.createOrUpdate(gig);
        Log.i("TAG", "saved: " + saved.getNumLinesChanged());
        Log.i(TAG, "countOf:" + gigDao.countOf());
        assertEquals(saved, 1);


    }

    @Test
    public void testGetGig() throws SQLException{
        Log.i(TAG, "testGetGig");

        Date today = new Date();
        // get our dao
        Gig createdgig = new Gig();
        createdgig.setId(1);
        createdgig.setTitle("I am walking the dog");
        createdgig.setDescription("I need someone to talk the dog while I am at work. They should" +
                " be " +
                "pretty good with dogs. The keys will be left with a neighbor. You just need to " +
                "go get the keys take paulie for a walk. Als if you could fill up his water and " +
                "food bowl if they are low. The food is in the cabinent under the sink.");
        createdgig.setLocation(10.10010f, 10.10102f);
        createdgig.setStatus("Pending");
        createdgig.setEmployerID(10);
        createdgig.setWorkerID(9);
        createdgig.setUpdatedAt(today);
        gigDao.createOrUpdate(createdgig);

        Gig gig = gigDao.queryForId(1);
        assertTrue(gig != null);
        assertEquals(gig.getID(), 1);
        assertTrue(gig.getTitle().equals("I am walking the dog"));
        assertTrue(gig.getDescription().equals("I need someone to talk the dog while I am at work" +
                ". They should be " +
                "pretty good with dogs. The keys will be left with a neighbor. You just need to " +
                "go get the keys take paulie for a walk. Als if you could fill up his water and " +
                "food bowl if they are low. The food is in the cabinent under the sink."));
        assertEquals(gig.getLongitude(), 10.10102f);
        assertEquals(gig.getLatitude(), 10.10010f);
        assertEquals(gig.getEmployerID(), 10);
        assertEquals(gig.getWorkerID(), 9);

    }

    /*@Test
    public void testUpdateGig() throws SQLException{
        Log.i(TAG, "testUpdatedGig");

        Date today = new Date();

        // get our dao
        Gig createdgig = new Gig();
        createdgig.setId(1);
        createdgig.setTitle("I am walking the dog");
        createdgig.setDescription("I need someone to talk the dog while I am at work. They should" +
                " be " +
                "pretty good with dogs. The keys will be left with a neighbor. You just need to " +
                "go get the keys take paulie for a walk. Als if you could fill up his water and " +
                "food bowl if they are low. The food is in the cabinent under the sink.");
        createdgig.setLocation(10.10010f, 10.10102f);
        createdgig.setStatus("Pending");
        createdgig.setEmployerID(10);
        createdgig.setWorkerID(9);
        createdgig.setUpdatedAt(today);
        gigDao.createIfNotExists(createdgig);

        Date now = new Date();
        Gig gig = gigDao.queryForId(1);
        assertTrue(gig != null);
        gig.setLocation(11.445f, 11.2233f);
        gig.setWorkerID(20);
        gig.setUpdatedAt(now);
        gigDao.update(gig);

        Gig gigUpdated = gigDao.queryForId(1);
        assertTrue(gigUpdated != null);
        assertEquals(gigUpdated.getID(), 1);
        assertTrue(gigUpdated.getTitle().equals("I am walking the dog"));
        assertTrue(gig.getDescription().equals("I need someone to talk the dog while I am at work" +
                ". They should be " +
                "pretty good with dogs. The keys will be left with a neighbor. You just need to " +
                "go get the keys take paulie for a walk. Als if you could fill up his water and " +
                "food bowl if they are low. The food is in the cabinent under the sink."));
        assertEquals(gigUpdated.getLatitude(), 11.445f);
        assertEquals(gigUpdated.getLongitude(), 11.2233f);
        assertEquals(gigUpdated.getEmployerID(), 10);
        assertEquals(gigUpdated.getWorkerID(), 20);

        Log.i(TAG, "count" + gigDao.countOf());
    }

    @Test
    public void testDeleteGig() throws SQLException{
        Log.i(TAG,"testDeleteGig");
        Date today = new Date();

        // get our dao
        Gig createdgig = new Gig();
        createdgig.setId(1);
        createdgig.setTitle("I am walking the dog");
        createdgig.setDescription("I need someone to talk the dog while I am at work. They should" +
                " be " +
                "pretty good with dogs. The keys will be left with a neighbor. You just need to " +
                "go get the keys take paulie for a walk. Als if you could fill up his water and " +
                "food bowl if they are low. The food is in the cabinent under the sink.");
        createdgig.setLocation(10.10010f, 10.10102f);
        createdgig.setStatus("Pending");
        createdgig.setEmployerID(10);
        createdgig.setWorkerID(9);
        createdgig.setUpdatedAt(today);
        gigDao.createIfNotExists(createdgig);


        int saved = gigDao.deleteById(1);
        assertEquals(saved, 1);
        Gig gig = gigDao.queryForId(1);
        assertTrue(gig == null);
    }*/


}
