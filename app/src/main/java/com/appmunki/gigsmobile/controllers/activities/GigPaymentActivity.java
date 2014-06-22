package com.appmunki.gigsmobile.controllers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.helpers.Utils;
import com.appmunki.gigsmobile.models.Payment;
import com.appmunki.gigsmobile.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.appmunki.gigsmobile.helpers.Utils.IntoString;

public class GigPaymentActivity extends Activity {

    //defining views
    @InjectView(R.id.payment_gig_cardnumber)
    EditText gigCardNumberEditText;
    @InjectView(R.id.payment_gig_month_spinner)
    Spinner gigMonthSpinner;
    @InjectView(R.id.payment_gig_year_spinner)
    Spinner gigYearSpinner;
    @InjectView(R.id.payment_gig_cvc)
    EditText gigCVCEditText;
    @InjectView(R.id.payment_gig_amount)
    EditText gigAmountEditText;
    @InjectView(R.id.payment_gig_console)
    TextView gigConsoleEditText;

    private int gigId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gig_form);
        ButterKnife.inject(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gigId = extras.getInt("GIG_ID", -1);
            if (gigId >= 0) {
                Log.v("PAYMENT", "GIG TO PAY " + gigId);
            }

        }
        gigCardNumberEditText.setText("4242424242424242");


    }

    private void createCard() {


        String cardNumber = gigCardNumberEditText.getText().toString();
        int month = Integer.parseInt(gigMonthSpinner.getSelectedItem() + "");
        int year = Integer.parseInt(gigYearSpinner.getSelectedItem() + "");
        String cvc = gigCVCEditText.getText().toString();

        Card card = new Card(cardNumber, month, year, cvc);

        if (!card.validateCard()) {
            Log.v("STRIPE", "FAIL");
        }

        final String log = "";

        Stripe stripe;
        try {
            stripe = new Stripe("pk_test_GSnTcklRvQE1kIw8EJ5Kppe6");
            stripe.createToken(card, new TokenCallback() {
                public void onSuccess(Token token) {
                    Log.v("STRIPE", "Success " + token.getId());
                    gigConsoleEditText.setText("Success " + token.getId());
                    chargeCard(token.getId());
                }

                public void onError(Exception error) {
                    // Show localized error message
                    Log.v("STRIPE", "FAIL " + error.getLocalizedMessage());
                    gigConsoleEditText.setText("FAIL " + error.getLocalizedMessage());
                }
            });
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }


    }

    private void chargeCard(String tokenId) {

        Log.v("STRIPE", "TOKEN " + UtilTempo.currentUser.getAuthToken());

        String amount = gigAmountEditText.getText().toString();

        if(amount.length()>0) {
            Payment payment = new Payment(gigId, tokenId, Integer.parseInt(amount));
            GigsWebServiceManager.getService().charge(payment, UtilTempo.currentUser.getEmail(), UtilTempo.currentUser.getAuthToken(), ChargeCallBack);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_gig, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_create_create_gig:
                createCard();
//                saveGig();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGig() {
        finish();
    }


    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    private Callback<Response> ChargeCallBack = new Callback<Response>() {
        private String TAG = this.getClass().getSimpleName();

        /**
         * Successful HTTP response.
         *
         * @param response
         * @param response
         */
        @Override
        public void success(Response response, Response response2) {


            Gson gson = new Gson();


            String body = null;
            try {
                body = IntoString(response.getBody().in());
                Log.v("STRIPE", body);
            } catch (IOException e) {
                e.printStackTrace();
            }


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
                if(error.getLocalizedMessage()!=null) {
                    Log.v("STRIPE", error.getLocalizedMessage());
                }

            }


        }

    };
}
