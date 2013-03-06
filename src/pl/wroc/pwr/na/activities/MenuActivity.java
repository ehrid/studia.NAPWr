package pl.wroc.pwr.na.activities;

import java.util.ArrayList;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.MenuCollectionPagerAdapter;
import pl.wroc.pwr.na.dialogs.CloseAppDialog;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.EventController;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends FragmentActivity {

	public ArrayList<EventObject> current = new ArrayList<EventObject>();
	public ArrayList<EventObject> top10;
	public ArrayList<EventObject> dzisiaj;
	public ArrayList<EventObject> jutro;
	public ArrayList<PlanObject> kalendarz;
	public ArrayList<EventObject> ulubione;

	MenuCollectionPagerAdapter mCollectionPagerAdapter;
	public ViewPager mViewPager;
	Button login;
	TextView loginText;

	public static MenuActivity activityMain;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		mCollectionPagerAdapter = new MenuCollectionPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager_menu);
		mViewPager.setAdapter(mCollectionPagerAdapter);

		activityMain = this;

		top10 = ((NAPWrApplication) getApplication()).top10;
		dzisiaj = ((NAPWrApplication) getApplication()).dzisiaj;
		jutro = ((NAPWrApplication) getApplication()).jutro;
		kalendarz = ((NAPWrApplication) getApplication()).kalendarz;
		ulubione = ((NAPWrApplication) getApplication()).ulubione;

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (((NAPWrApplication) getApplication()).top10.size() == 0) {
			finish();
			startActivity(new Intent(this, SplashScreenActivity.class));
		}
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (mViewPager.getCurrentItem() == 0) {
				// new MenuDialog(this).show();
			} else {
				mViewPager.setCurrentItem(0);
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mViewPager.getCurrentItem() == 0) {
				new CloseAppDialog(this).show();
			} else {
				mViewPager.setCurrentItem(0);
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	};

	public void closeApplication() {
		finish();
	}

	public void logoff() {
		((NAPWrApplication) getApplication()).forgetUser();
		mViewPager.refreshDrawableState();
	}

	public boolean ifLogedin() {
		return ((NAPWrApplication) getApplication()).logedin;
	}

	ProgressDialog progressDialog;

	public void eventDownload() {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		EventController ep = new EventController();

		NAPWrApplication app = (NAPWrApplication) getApplication();

		ep.addDzisiaj(app);
		ep.addJutro(app);
		ep.addTop(app);
		ep.addUlubione(app);
		ep.addKalendarz(app);

		dzisiaj = app.dzisiaj;
		jutro = app.jutro;
		top10 = app.top10;
		ulubione = app.ulubione;
		kalendarz = app.kalendarz;
		
		uis.writeObject(app.dzisiaj, "dzisiaj");
		uis.writeObject(app.jutro, "jutro");
		uis.writeObject(app.top10, "top10");
		uis.writeObject(app.ulubione, "ulubione");
		uis.writeObject(app.kalendarz, "kalendarz");

		mViewPager.refreshDrawableState();
	}

	public boolean isLoginAvailable() {
		boolean networkAvailable = isNetworkAvailable();
		if (!networkAvailable) {
			Toast.makeText(getApplicationContext(),
					"Brak aktywnego połączenia", 1000).show();
		}
		return networkAvailable;
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	// To use the AsyncTask, it must be subclassed
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		// Before running code in separate thread
		@Override
		protected void onPreExecute() {
			// Create a new progress dialog
			progressDialog = new ProgressDialog(MenuActivity.this);
			progressDialog
					.setMessage("Odświeżanie listy wydarzeń.\nAby anulować naciśnij wstecz.");
			progressDialog.setIndeterminate(false);
			progressDialog.show();
		}

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			eventDownload();
			return null;
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			progressDialog.dismiss();
		}
	}

}
