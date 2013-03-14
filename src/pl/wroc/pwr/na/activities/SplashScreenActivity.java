package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {

	ImageView loading1;
	ImageView loading2;
	ImageView loading3;
	ImageView loading4;
	ImageView loading5;

	UseInternalStorage uis;
	EventController ep;
	NAPWrApplication app;

	int day;

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
		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_WEEK);
		loading1.setImageResource(R.drawable.loading_on);

		if (uis.readObject("dzisiaj") == null && !isNetworkAvailable()) {
			finish();
			Toast.makeText(getApplicationContext(),
					"Aplikacja wymaga aktywnego połaczenia z internetm", 5000)
					.show();
		} else if (uis.readObject("dzisiaj") != null && !isNetworkAvailable()) {
			loading();
		} else {
			downloadEvents();
		}

	}

	private void loading() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@SuppressWarnings("unchecked")
			public void run() {

				app.dzisiaj = (ArrayList<EventObject>) uis
						.readObject("dzisiaj");
				if (app.dzisiaj == null) {
					app.dzisiaj = new ArrayList<EventObject>();
				}
				loading2.setImageResource(R.drawable.loading_on);

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {

						app.top10 = (ArrayList<EventObject>) uis
								.readObject("top10");
						if (app.top10 == null) {
							app.top10 = new ArrayList<EventObject>();
						}
						loading3.setImageResource(R.drawable.loading_on);

						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {

								app.jutro = (ArrayList<EventObject>) uis
										.readObject("jutro");
								if (app.jutro == null) {
									app.jutro = new ArrayList<EventObject>();
								}
								loading4.setImageResource(R.drawable.loading_on);

								Handler handler = new Handler();
								handler.postDelayed(new Runnable() {

									@Override
									public void run() {

										app.kalendarz = (ArrayList<PlanObject>) uis
												.readObject("kalendarz");
										if (app.kalendarz == null) {
											app.kalendarz = new ArrayList<PlanObject>();
										}
										loading5.setImageResource(R.drawable.loading_on);

										Handler handler = new Handler();
										handler.postDelayed(new Runnable() {

											@Override
											public void run() {

												app.ulubione = (ArrayList<EventObject>) uis
														.readObject("ulubione");
												if (app.ulubione == null) {
													app.ulubione = new ArrayList<EventObject>();
												}

												finish();
												// start the home screen

												SplashScreenActivity.this
														.startActivity(new Intent(
																SplashScreenActivity.this,
																MenuActivity.class));

											}

										}, 400);
									}

								}, 400);
							}

						}, 400);
					}

				}, 400);
			}

		}, 400);
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
//					if (app.dzisiaj != null) {
//						app.dzisiaj.get(0).setImagePoster(
//								getApplicationContext());
//					}
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
//							if (app.top10 != null) {
//								app.top10.get(0).setImagePoster(
//										getApplicationContext());
//							}
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
//									if (app.jutro != null) {
//										app.jutro.get(0).setImagePoster(
//												getApplicationContext());
//									}
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

//													if (app.ulubione.size() > 0) {
//														app.ulubione
//																.get(0)
//																.setImagePoster(
//																		getApplicationContext());
//													}

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

												app.downloadDay = day;
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
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

}