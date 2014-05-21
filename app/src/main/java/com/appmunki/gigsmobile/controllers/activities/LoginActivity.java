package com.appmunki.gigsmobile.controllers.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.helpers.BusProvider;
import com.appmunki.gigsmobile.helpers.Utils;
import com.appmunki.gigsmobile.models.User;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.appmunki.gigsmobile.helpers.Utils.IntoString;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Represents an asynchronous signup task used to register
     * the user.
     */
    @VisibleForTesting
    private Callback<Response> RegisterCallBack = new Callback<Response>() {
        private String TAG = this.getClass().getSimpleName();


        @Override
        public void success(Response response, Response response2) {


            showProgress(false, "");

            Gson gson = new Gson();

            String body = null;
            try {
                body = IntoString(response.getBody().in());
            } catch (IOException e) {
                e.printStackTrace();
            }


            JsonObject resp = new JsonParser().parse(body).getAsJsonObject();
            Toast.makeText(getApplicationContext(), resp.getAsJsonPrimitive("info").getAsString(), Toast.LENGTH_SHORT).show();

            //parse the information from the server.
            JsonObject data = resp.getAsJsonObject("data");

            //data from the http
            User user = gson.fromJson(data, User.class);
            boolean success = resp.getAsJsonPrimitive("success").getAsBoolean();
            String info = resp.getAsJsonPrimitive("info").getAsString();

            //sending the user and starting the userdetailactivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(Utils.ARG_USER, user);
            startActivity(intent);


        }

        @Override
        public void failure(RetrofitError error) {
            if (error.isNetworkError()) {
                Toast.makeText(getBaseContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            } else {

                mEmailView.setError(error.getLocalizedMessage());
                mPasswordView.requestFocus();
            }
            showProgress(false, "");
        }
    };
    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    private Callback<Response> LoginCallBack = new Callback<Response>() {
        private String TAG = this.getClass().getSimpleName();

        /**
         * Successful HTTP response.
         *
         * @param response
         * @param response
         */
        @Override
        public void success(Response response, Response response2) {
            showProgress(false, "");

            Gson gson = new Gson();


            String body = null;
            try {
                body = IntoString(response.getBody().in());
            } catch (IOException e) {
                e.printStackTrace();
            }


            JsonObject resp = new JsonParser().parse(body).getAsJsonObject();
            Toast.makeText(getApplicationContext(), resp.getAsJsonPrimitive("info").getAsString(), Toast.LENGTH_SHORT).show();

            //parse the information from the server.
            JsonObject data = resp.getAsJsonObject("data");

            //data from the http
            User user = gson.fromJson(data, User.class);
            boolean success = resp.getAsJsonPrimitive("success").getAsBoolean();
            String info = resp.getAsJsonPrimitive("info").getAsString();

            //starting user detail activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(Utils.ARG_USER, user);
            startActivity(intent);

        }

        /**
         * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
         * exception.
         *
         * @param error
         */
        @Override
        public void failure(RetrofitError error) {
            if (error.isNetworkError()) {
                Toast.makeText(getBaseContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            } else {

                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            showProgress(false, "");

        }

    };
    /**
     * UI references.
     */
    @InjectView(R.id.login_activity_email)
    AutoCompleteTextView mEmailView;
    @InjectView(R.id.login_activity_password)
    EditText mPasswordView;
    @InjectView(R.id.login_activity_passwordconfirm)
    EditText mConfirmPasswordView;
    @InjectView(R.id.type_spinner)
    Spinner mUserTypeView;
    @InjectView(R.id.login_status)
    View mProgressView;
    @InjectView(R.id.login_form)
    View mLoginFormView;
    @InjectView(R.id.login_status_message)
    TextView mLoginStatus;
    @InjectView(R.id.login_activity_email_sign_in_button)
    Button mEmailSignInButton;
    @InjectView(R.id.login_activity_email_sign_up_button)
    Button mEmailSignUpButton;

    /**
     * class tag
     */
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.inject(this);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setDisplayShowTitleEnabled(false);


        populateAutoComplete();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 10 || id == EditorInfo.IME_NULL) {
                    attemptSignin();
                    return true;
                }
                return false;
            }
        });
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 11 || id == EditorInfo.IME_NULL) {
                    attemptSignup();
                    return true;
                }
                return false;
            }
        });


        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignin();
            }
        });

        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserTypeView.setAdapter(adapter);

        // Set up the login form.
        //mEmailView.setText("radzell@appmunki.com");
        //mPasswordView.setText("Radzell1");
        //mConfirmPasswordView.setText("Radzell1");
        //mEmailSignInButton.performClick();
    }

    @Override
    protected void onResume() {
        BusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @VisibleForTesting
    public void attemptSignin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        int typeposition = mUserTypeView.getSelectedItemPosition();
        String type = typeposition == 1 ? "Worker" : "Employer";
        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            String title = getResources().getString(R.string.logging_in);

            showProgress(true, title);
            GigsWebServiceManager.getService().login(new User(email, password, type), LoginCallBack);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptSignup() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordconfirmation = mConfirmPasswordView.getText().toString();

        int typeposition = mUserTypeView.getSelectedItemPosition();

        String type = typeposition == 1 ? "Worker" : "Employer";

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordconfirmation)) {
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = mConfirmPasswordView;
            cancel = true;
        } else if (!isPasswordValid(passwordconfirmation)) {
            mConfirmPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        } else if (!isPasswordConfirmed(password, passwordconfirmation)) {
            mConfirmPasswordView.setError(getString(R.string.error_password_doesnt_match));
            focusView = mConfirmPasswordView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            String title = getResources().getString(R.string.signing_up);

            showProgress(true, title);
            GigsWebServiceManager.getService().register(new User(email, password, passwordconfirmation, type), RegisterCallBack);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show, String title) {
        mLoginStatus.setText(title);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isPasswordConfirmed(String password, String passwordconfirmation) {
        return password.equals(passwordconfirmation);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Adds to email to the emailEdittext
     *
     * @param emailAddressCollection a collection of typically used email address
     */
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * A profile for a email addres
     */
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }


}



