package com.appmunki.gigs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.savagelook.android.UrlJsonAsyncTask;

/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Activity that allow
 * 
 * @Override protected void onPostExecute(JSONObject json) {s the user to select
 *           the account they want to use to sign in. The class also implements
 *           integration with Google Play Services and Google Accounts.
 */
public class SignInActivity extends Activity {

	private static final String TASKS_URL = "http://192.168.1.10:3000/api/v1/resturants.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sign_in);

		loadTasksFromAPI(TASKS_URL);
		// startActivity(new Intent(this, ResturantListActivity.class));

	}

	private void loadTasksFromAPI(String url) {
		Log.i("Test", "loadResturant");

		// JSONTask js = new JSONTask();
		// js.execute();
		new HttpAsyncTask().execute(TASKS_URL);

	}

	private class GetTasksTask extends UrlJsonAsyncTask {
		public GetTasksTask(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				Log.i("Test", "onPost");

				Toast.makeText(context, json.toString(), Toast.LENGTH_LONG)
						.show();
			} catch (Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
						.show();
			} finally {
				super.onPostExecute(json);
			}
		}
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		public String GET(String url) {
			Log.i("http", url);
			InputStream inputStream = null;
			String result = "";
			try {

				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make GET request to the given URL
				HttpResponse httpResponse = httpclient
						.execute(new HttpGet(url));

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// convert inputstream to string
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";
			Log.i("http", result);

			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}

			return result;
		}

		private String convertInputStreamToString(InputStream inputStream)
				throws IOException {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			String result = "";
			while ((line = bufferedReader.readLine()) != null)
				result += line;

			inputStream.close();
			return result;

		}

		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG)
					.show();
			// etResponse.setText(result);
		}
	}

}