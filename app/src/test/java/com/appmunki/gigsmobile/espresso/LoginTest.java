package com.appmunki.gigsmobile.espresso;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.controllers.LoginActivity;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

@LargeTest
public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity mActivity;


    @SuppressWarnings("deprecation")
    public LoginTest() {
        // This constructor was deprecated - but we want to support lower API levels.
        super("com.appmunki.gigsmobile.controllers.activities", LoginActivity.class);
    }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();

    }
    @SuppressWarnings("unchecked")
    public void testGoodSignin() throws Throwable {
        onView(ViewMatchers.withId(R.id.login_activity_email)).perform(typeText("test@example.com"),closeSoftKeyboard());
        onView(withId(R.id.login_activity_password)).perform(typeText("Have a cup of Espresso."), closeSoftKeyboard());
        onView(withId(R.id.login_activity_email_sign_in_button)).perform(click());

    }


    @Override
    protected void tearDown() throws Exception {

    }
}