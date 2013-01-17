package pl.wroc.pwr.na.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.OrganizationObject;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {
	
	ImageView loading1;
	ImageView loading2;
	ImageView loading3;
	ImageView loading4;
	ImageView loading5;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		loading1 = (ImageView) findViewById(R.id.splash_loading1);
		loading2 = (ImageView) findViewById(R.id.splash_loading2);
		loading3 = (ImageView) findViewById(R.id.splash_loading3);
		loading4 = (ImageView) findViewById(R.id.splash_loading4);
		loading5 = (ImageView) findViewById(R.id.splash_loading5);
		
		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				// Collect data
				addEvents();

			}

		}, 400);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				// Collect data
				addEvents();

				// make sure we close the splash screen so the user won't come
				// back when it presses back key

				finish();
				// start the home screen

				Intent intent = new Intent(SplashScreenActivity.this,
						MenuActivity.class);
				SplashScreenActivity.this.startActivity(intent);

			}

		}, 400); // time in milliseconds (1 second = 1000 milliseconds) until
					// the run() method will be called

	}

	public void addEvents() {
		JSONArray completeJSONArr = null;
		try {
			// DZISIAJ
			completeJSONArr = new JSONArray((String) new RequestTask().execute(
					"http://na.pwr.wroc.pl/mobile/wydarzenia/dzis").get());
			((NAPWrApplication) getApplication()).dzisiaj = getEvents(completeJSONArr);
			
			loading1.setImageResource(R.drawable.loading_on);

			// TOP10
			completeJSONArr = new JSONArray((String) new RequestTask().execute(
					"http://na.pwr.wroc.pl/json/topten").get());
			((NAPWrApplication) getApplication()).top10 = getEvents(completeJSONArr);
			
			loading2.setImageResource(R.drawable.loading_on);

			// JUTRO
			completeJSONArr = new JSONArray((String) new RequestTask().execute(
					"http://na.pwr.wroc.pl/mobile/wydarzenia/jutro").get());
			((NAPWrApplication) getApplication()).jutro = getEvents(completeJSONArr);
			
			loading3.setImageResource(R.drawable.loading_on);

			// KALENDARZ
			((NAPWrApplication) getApplication()).kalendarz = new ArrayList<EventObject>();
			
			loading4.setImageResource(R.drawable.loading_on);

			// ULUBIONE
			((NAPWrApplication) getApplication()).ulubione = new ArrayList<EventObject>();
			
			loading5.setImageResource(R.drawable.loading_on);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<EventObject> getEvents(JSONArray completeJSONArr) {
		String wydarzenieTytul = "";
		String wydarzenieTresc = "";
		int wydarzenieSumaLajkow = 0;
		int wydarzenieWartoscPriorytet = 0;
		int wydarzeniePrzeczytalo = 0;
		String wydarzenieDataPoczatek = "";
		String wydarzenieDataKoniec = "";
		String linkToSmallPoster = "";
		String nazwaOrganizacji = "";
		OrganizationObject organizacja = new OrganizationObject("");

		JSONObject event;

		ArrayList<EventObject> eventList = new ArrayList<EventObject>();

		for (int i = 0; i < completeJSONArr.length(); i++) {

			try {
				event = completeJSONArr.getJSONObject(i);
				wydarzenieTytul = event.getString("wydarzenieTytul");
				wydarzenieTresc = event.getString("wydarzenieTresc");
				linkToSmallPoster = event.getJSONObject("plakat").getString(
						"plakatMiniatura");

				wydarzenieSumaLajkow = event.getInt("wydarzenieSumaLajkow");
				wydarzenieWartoscPriorytet = event
						.getInt("wydarzenieWartoscPriorytet");
				wydarzeniePrzeczytalo = event.getInt("wydarzeniePrzeczytalo");

				wydarzenieDataPoczatek = (String) event.getJSONObject(
						"wydarzenieDataPoczatek").get("date");
				
				wydarzenieDataKoniec = (String) event.getJSONObject(
						"wydarzenieDataKoniec").get("date");
				
				nazwaOrganizacji = (String) event.getJSONObject(
						"organizacja").getJSONObject("uzytkownik").get("uzytkownikNazwaWyswietlana");
				
				organizacja = new OrganizationObject(nazwaOrganizacji);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			eventList
					.add(new EventObject(
							wydarzenieTytul,
							i,
							wydarzenieTresc, // wydarzenieTresc.substring(0,
												// 200) + "...", //obcięcie
												// treści wydarzenia do 200
												// znaków
							"http://www.na.pwr.wroc.pl/" + linkToSmallPoster,
							wydarzenieSumaLajkow + wydarzenieWartoscPriorytet
									+ wydarzeniePrzeczytalo / 4,
							wydarzenieDataPoczatek, wydarzenieDataKoniec, organizacja));
		}

		return eventList;
	}

	class RequestTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);

					responseString = out.toString();
					out.close();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase() + uri[0]);
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {

			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..
		}
	}
}