package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.tools.EventController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
		EventController ep = new EventController();
		NAPWrApplication app = (NAPWrApplication) getApplication();
		
		app.restoreUser();

		// DZISIAJ
		ep.addDzisiaj(app);
		loading1.setImageResource(R.drawable.loading_on);

		// TOP10
		ep.addTop(app);
		loading2.setImageResource(R.drawable.loading_on);

		// JUTRO
		ep.addJutro(app);
		loading3.setImageResource(R.drawable.loading_on);

		// KALENDARZ
		ep.addKalendarz(app);
		loading4.setImageResource(R.drawable.loading_on);

		// ULUBIONE
		ep.addUlubione(app);
		loading5.setImageResource(R.drawable.loading_on);

	}
}