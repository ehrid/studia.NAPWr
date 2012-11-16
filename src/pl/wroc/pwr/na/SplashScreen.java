package pl.wroc.pwr.na;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(Preferences.getFulscreen(this)){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		setContentView(R.layout.splash);

		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {
			public void run() {
				//Pobieranie danych z bazy danch 1
			}
		}, 400);
		handler.postDelayed(new Runnable() {
			public void run() {
				//Pobieranie danych z bazy danch 2
			}
		}, 400);
		handler.postDelayed(new Runnable() {
			public void run() {
				//Pobieranie danych z bazy danch 3
			}
		}, 400);
		// run a thread after 2 seconds to start the home screen
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				// make sure we close the splash screen so the user won't come
				// back when it presses back key

				finish();
				// start the home screen

				Intent intent = new Intent(SplashScreen.this, NAPWr.class);
				SplashScreen.this.startActivity(intent);

			}

		}, 400); // time in milliseconds (1 second = 1000 milliseconds) until
					// the run() method will be called

	}
}