package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.tools.JSONParser;
import pl.wroc.pwr.na.tools.PlanParser;
import pl.wroc.pwr.na.tools.PosterController;
import pl.wroc.pwr.na.tools.RSSParser;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

	ImageView loading1;
	ImageView loading2;
	ImageView loading3;
	ImageView loading4;
	ImageView loading5;

	UseInternalStorage uis;
	NAPWrApplication app;

	JSONParser jsonParser;
	RSSParser rssParser;
	PlanParser planParser;
	boolean shouldStartAleready = false;

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

		jsonParser = new JSONParser();
		rssParser = new RSSParser();
		planParser = new PlanParser();

		app = (NAPWrApplication) getApplication();
		loading1.setImageResource(R.drawable.loading_on);

		if (!isNetworkAvailable()) {
			shouldStartAleready = true;
			goRobot();
		} else {
			downloadEvents();
		}
		saveJuwenalia();
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

		}, 1500);
	}
	
	private void saveJuwenalia() {

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			public void run() {
				PosterController pc = new PosterController();
				pc.writePoster(BitmapFactory.decodeResource(getResources(),
						R.drawable.juwenalia_portiat),
						getResources().getString(R.string.menu_juwenalia) + "_portiat",
						getApplicationContext(), getWidthOfScreen(), 1);
				
				pc.writePoster(BitmapFactory.decodeResource(getResources(),
						R.drawable.juwenalia_landscape),
						getResources().getString(R.string.menu_juwenalia) + "_landscape",
						getApplicationContext(), getWidthOfScreen(), 2);
			}

		}, 100);
	}

	private void downloadEvents() {
		if (!shouldStartAleready) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					loading2.setImageResource(R.drawable.loading_on);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							loading3.setImageResource(R.drawable.loading_on);
							Handler handler = new Handler();
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									loading4.setImageResource(R.drawable.loading_on);
									Handler handler = new Handler();
									handler.postDelayed(new Runnable() {

										@Override
										public void run() {
											loading5.setImageResource(R.drawable.loading_on);
											Handler handler = new Handler();
											handler.postDelayed(new Runnable() {

												@Override
												public void run() {
													finish();

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
	
	public int getWidthOfScreen(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		return metrics.widthPixels;
	}
	
	public int getHeightOfScreen(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		return metrics.heightPixels;
	}

}