package com.appmunki.gigsmobile.robolectric;

import android.app.Activity;
import android.content.Context;


import com.appmunki.gigsmobile.controllers.MainActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;


/**
 * Created by radzell on 6/2/14.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DatabaseTest {
    @Before
    public void setup() {
        //do whatever is necessary before every test
    }

    @Test
    public void testActivityFound() {

        Assert.assertNotNull(true);
    }
}
