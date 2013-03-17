package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.EventController;
import pl.wroc.pwr.na.tools.RequestTaskString;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

	ImageView loading1;
	ImageView loading2;
	ImageView loading3;
	ImageView loading4;
	ImageView loading5;

	UseInternalStorage uis;
	EventController ep;
	NAPWrApplication app;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		loading1 = (ImageView) findViewById(R.id.splash_loading1);
		loading2 = (ImageView) findViewById(R.id.splash_loading2);
		loading3 = (ImageView) findViewById(R.id.splash_loading3);
		loading4 = (ImageView) findViewById(R.id.splash_loading4);
		loading5 = (ImageView) findViewById(R.id.splash_loading5);

		uis = new UseInternalStorage(getApplicationContext());

		ep = new EventController();
		app = (NAPWrApplication) getApplication();
		loading1.setImageResource(R.drawable.loading_on);

		if (!isNetworkAvailable()) {
			goRobot();
		} else {
			downloadEvents();
		}

	}

	private void goRobot() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			public void run() {
				finish();
				SplashScreenActivity.this.startActivity(new Intent(
						SplashScreenActivity.this,
						ConnectionErrorActivity.class));
			}

		}, 1000);
	}

	private void downloadEvents() {

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			public void run() {
				try {
					JSONArray completeJSONArr = new JSONArray(
							(String) new RequestTaskString()
									.execute(
											"http://www.napwr.pl/mobile/wydarzenia/dzis")
									.get());

					app.dzisiaj = ep.getEvents(completeJSONArr);
					// if (app.dzisiaj != null) {
					// app.dzisiaj.get(0).setImagePoster(
					// getApplicationContext());
					// }
					uis.writeObject(app.dzisiaj, "dzisiaj");
					loading2.setImageResource(R.drawable.loading_on);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						try {
							JSONArray completeJSONArr = new JSONArray(
									(String) new RequestTaskString().execute(
											"http://www.napwr.pl/json/topten")
											.get());

							app.top10 = ep.getEvents(completeJSONArr);
							// if (app.top10 != null) {
							// app.top10.get(0).setImagePoster(
							// getApplicationContext());
							// }
							uis.writeObject(app.top10, "top10");
							loading3.setImageResource(R.drawable.loading_on);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								try {
									JSONArray completeJSONArr = new JSONArray(
											(String) new RequestTaskString()
													.execute(
															"http://www.napwr.pl/mobile/wydarzenia/jutro")
													.get());

									app.jutro = ep.getEvents(completeJSONArr);
									// if (app.jutro != null) {
									// app.jutro.get(0).setImagePoster(
									// getApplicationContext());
									// }
									uis.writeObject(app.jutro, "jutro");
									loading4.setImageResource(R.drawable.loading_on);

								} catch (JSONException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (ExecutionException e) {
									e.printStackTrace();
								}
								Handler handler = new Handler();
								handler.postDelayed(new Runnable() {

									@Override
									public void run() {
										try {
											if (app.logedin) {
												JSONObject completeObject = new JSONObject(
														(String) new RequestTaskString()
																.execute(
																		"http://www.napwr.pl/mobile/plan/"
																				+ app.userId)
																.get());

												app.kalendarz = ep
														.getPlan(completeObject);
											} else {
												app.kalendarz = new ArrayList<PlanObject>();
											}
											uis.writeObject(app.kalendarz,
													"kalendarz");
											loading5.setImageResource(R.drawable.loading_on);
										} catch (JSONException e) {
											e.printStackTrace();
										} catch (InterruptedException e) {
											e.printStackTrace();
										} catch (ExecutionException e) {
											e.printStackTrace();
										}

										Handler handler = new Handler();
										handler.postDelayed(new Runnable() {

											@Override
											public void run() {
												try {
													if (app.logedin) {
														JSONArray completeJSONArr = new JSONArray(
																(String) new RequestTaskString()
																		.execute(
																				"http://www.napwr.pl/mobile/wydarzenia/ulubione/"
																						+ app.userId)
																		.get());

														app.ulubione = ep
																.getEventsUlubione(completeJSONArr);
													} else {
														app.ulubione = new ArrayList<EventObject>();
													}

													// if (app.ulubione.size() >
													// 0) {
													// app.ulubione
													// .get(0)
													// .setImagePoster(
													// getApplicationContext());
													// }

													uis.writeObject(
															app.ulubione,
															"ulubione");

												} catch (JSONException e) {
													e.printStackTrace();
												} catch (InterruptedException e) {
													e.printStackTrace();
												} catch (ExecutionException e) {
													e.printStackTrace();
												}

												finish();
												// start the home screen

												SplashScreenActivity.this
														.startActivity(new Intent(
																SplashScreenActivity.this,
																MenuActivity.class));

											}

										}, 200);
									}

								}, 200);
							}

						}, 200);
					}

				}, 200);
			}

		}, 200);

	}

	private boolean isNetworkAvailable() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) SplashScreenActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
				if (ni.isConnected()) {
					haveConnectedWifi = true;
					Log.d("Internt Connection", "WIFI CONNECTION AVAILABLE");
				} else {
					Log.d("Internt Connection", "WIFI CONNECTION NOT AVAILABLE");
				}
			}
			if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
				if (ni.isConnected()) {
					haveConnectedMobile = true;
					Log.d("Internt Connection",
							"MOBILE INTERNET CONNECTION AVAILABLE");
				} else {
					Log.d("Internt Connection",
							"MOBILE INTERNET CONNECTION NOT AVAILABLE");
				}
			}
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

}