package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.HashMap;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.MenuCollectionPagerAdapter;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.ListItemObject;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.PlanParser;
import pl.wroc.pwr.na.tools.PosterController;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends FragmentActivity implements OnClickListener {

	public ArrayList<EventObject> current = new ArrayList<EventObject>();

	public ArrayList<PlanObject> kalendarz;

	public HashMap<String, ArrayList<EventObject>> eventList = new HashMap<String, ArrayList<EventObject>>();
	public ArrayList<ListItemObject> listItems;

	private boolean openMenu = true;

	MenuCollectionPagerAdapter mCollectionPagerAdapter;
	public ViewPager mViewPager;
	Button login;
	TextView loginText;

	public NAPWrApplication app;

	public static MenuActivity activityMain;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		app = (NAPWrApplication) getApplication();
		activityMain = this;

		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		makeItemList();
		mCollectionPagerAdapter = new MenuCollectionPagerAdapter(
				getSupportFragmentManager(), listItems);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager_menu);
		mViewPager.setAdapter(mCollectionPagerAdapter);

		eventList = app.eventList;
		kalendarz = app.kalendarz;

		findViewById(R.id.left_menu).bringToFront();
		findViewById(R.id.left_menu).setVisibility(View.VISIBLE);
		setMenu();
	}

	public void openMenu() {
		findViewById(R.id.left_menu).bringToFront();
		if (openMenu) {
			findViewById(R.id.left_menu).setVisibility(View.GONE);
		} else {
			findViewById(R.id.left_menu).setVisibility(View.VISIBLE);
		}
		openMenu = !openMenu;
	}

	public void closeMenu() {
		findViewById(R.id.left_menu).setVisibility(View.GONE);
		openMenu = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			openMenu();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (openMenu) {
				closeMenu();
			} else {
				if (mViewPager.getCurrentItem() == 0) {
					moveTaskToBack(true);
				} else {
					mViewPager.setCurrentItem(0);
				}
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
		removePlanObject();
		mViewPager.refreshDrawableState();
	}

	public boolean ifLogedin() {
		return ((NAPWrApplication) getApplication()).logedin;
	}

	@SuppressLint("ShowToast")
	public boolean isLoginAvailable() {
		boolean networkAvailable = isNetworkAvailable();
		if (!networkAvailable) {
			Toast.makeText(getApplicationContext(),
					"Brak aktywnego połączenia", 2000).show();
		}
		return networkAvailable;
	}

	private boolean isNetworkAvailable() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) MenuActivity.this
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

	/*
	 * ELEMENTY DO POBIERANIA DYNAMICZNEGO
	 */

	public void addEventItem(String key, ArrayList<EventObject> value) {
		app.eventList.put(key, value);
		eventList.put(key, value);
		mViewPager.refreshDrawableState();
	}

	public void addPlanItem(ArrayList<PlanObject> value) {
		app.kalendarz = value;
		kalendarz = value;
		mViewPager.refreshDrawableState();
	}

	public void getPlan() {
		PlanParser planParser = new PlanParser();
		addPlanItem(planParser.getPlan(app));
	}

	public boolean haveToDownload(String key) {
		return !app.eventList.containsKey(key);
	}

	public Bitmap getBitmapFromUIS(String key) {
		PosterController pc = new PosterController();
		return pc.readPoster(key, getApplicationContext());
	}

	public Bitmap saveBitmapToUIS(String key) {
		PosterController pc = new PosterController();
		return pc.writePoster(key, getApplicationContext());
	}

	public int getScreenOrientation() {
		Display getOrient = getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if (getOrient.getWidth() == getOrient.getHeight()) {
			orientation = Configuration.ORIENTATION_SQUARE;
		} else {
			if (getOrient.getWidth() < getOrient.getHeight()) {
				orientation = Configuration.ORIENTATION_PORTRAIT;
			} else {
				orientation = Configuration.ORIENTATION_LANDSCAPE;
			}
		}
		return orientation;
	}
	
	public int getWidthOfScreen(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	
	public void savePlanObject(ArrayList<PlanObject> plan){
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		uis.writeObject(plan, "PlanZajecNAPWr");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<PlanObject> getPlanObject(){
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		return (ArrayList<PlanObject>) uis.readObject("PlanZajecNAPWr");
	}
	
	public void removePlanObject(){
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		uis.writeObject(null, "PlanZajecNAPWr");
	}

	/*
	 * LOGIKA DZIAŁANIA MENU
	 */

	// MAIN
	TextView btn_juwenalia;
	TextView btn_user;
	TextView btn_today;
	TextView btn_tomorrow;
	TextView btn_category;
	TextView btn_top10;
	TextView btn_refesh;
	TextView btn_settings;
	TextView btn_login;
	TextView btn_about;
	TextView btn_sponsor;
	TextView btn_exit;

	// ADDITIONAL
	TextView btn_user_plan;
	TextView btn_user_favourities;
	TextView btn_user_facultity;
	boolean userOptionsOpen = false;

	private void makeItemList() {
		/*
		 * TYPE: 0 - LISTA WYDARZEŃ JSON 1 - LISTA WYDARZEŃ RSS 2 - POSTER 3 -
		 * PLAN ZAJĘĆ
		 * 
		 * new ListItemObject(type, title, miniature)
		 */
		listItems = new ArrayList<ListItemObject>();
		listItems.add(new ListItemObject(2, getResources().getString(
				R.string.menu_juwenalia), 0, ""));
		listItems.add(new ListItemObject(1, getResources().getString(
				R.string.menu_juwenalia_sub), 0,
				"http://www.napwr.pl/rss/20/kategorie/juwenalia/"));
		if (ifLogedin()) {
			listItems.add(new ListItemObject(2, getResources().getString(
					R.string.menu_user), 0, ""));
			listItems
					.add(new ListItemObject(3, getResources().getString(
							R.string.menu_user_plan),
							R.drawable.miniature_calendar, ""));
			// listItems.add(new ListItemObject(0,
			// getResources().getString(R.string.menu_user_favourities),R.drawable.miniature_favourites,
			// ""));
		}
		listItems.add(new ListItemObject(2, getResources().getString(
				R.string.menu_today), 0, ""));
		listItems.add(new ListItemObject(0, getResources().getString(
				R.string.menu_today_sub), R.drawable.miniature_today,
				"http://www.napwr.pl/mobile/wydarzenia/dzis"));
		listItems.add(new ListItemObject(2, getResources().getString(
				R.string.menu_tomorrow), 0, ""));
		listItems.add(new ListItemObject(0, getResources().getString(
				R.string.menu_tomorrow_sub), R.drawable.miniature_tommorow,
				"http://www.napwr.pl/mobile/wydarzenia/jutro"));
		listItems.add(new ListItemObject(2, getResources().getString(
				R.string.menu_category), 0, ""));
		listItems.add(new ListItemObject(2, getResources().getString(
				R.string.menu_top10), 0, ""));
		listItems.add(new ListItemObject(0, getResources().getString(
				R.string.menu_top10_sub), R.drawable.miniature_top10,
				"http://www.napwr.pl/json/topten"));
	}

	void addItemsOnLogIn() {
		finish();
		startActivity(new Intent(this, MenuActivity.class));
	}

	private void setMenu() {
		btn_juwenalia = (TextView) findViewById(R.id.menu_juwenalia);
		btn_user = (TextView) findViewById(R.id.menu_user);
		btn_today = (TextView) findViewById(R.id.menu_today);
		btn_tomorrow = (TextView) findViewById(R.id.menu_tomorrow);
		btn_category = (TextView) findViewById(R.id.menu_category);
		btn_top10 = (TextView) findViewById(R.id.menu_top10);
		btn_refesh = (TextView) findViewById(R.id.menu_refresh);
		btn_settings = (TextView) findViewById(R.id.menu_settings);
		btn_login = (TextView) findViewById(R.id.menu_login);
		btn_about = (TextView) findViewById(R.id.menu_about);
		btn_sponsor = (TextView) findViewById(R.id.menu_sponsor);
		btn_exit = (TextView) findViewById(R.id.menu_exit);

		btn_user_plan = (TextView) findViewById(R.id.menu_user_plan);
		btn_user_favourities = (TextView) findViewById(R.id.menu_user_favourities);
		btn_user_facultity = (TextView) findViewById(R.id.menu_user_facultity);

		if (ifLogedin()) {
			btn_user.setText(app.userName);
			btn_login.setText(getResources().getString(R.string.menu_logout));
		}

		btn_juwenalia.setOnClickListener(this);
		btn_user.setOnClickListener(this);
		btn_today.setOnClickListener(this);
		btn_tomorrow.setOnClickListener(this);
		btn_category.setOnClickListener(this);
		btn_top10.setOnClickListener(this);
		btn_refesh.setOnClickListener(this);
		btn_settings.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_about.setOnClickListener(this);
		btn_sponsor.setOnClickListener(this);
		btn_exit.setOnClickListener(this);

		btn_user_plan.setOnClickListener(this);
		btn_user_favourities.setOnClickListener(this);
		btn_user_facultity.setOnClickListener(this);

		if (!ifLogedin()) {
			btn_user.setVisibility(View.GONE);
			findViewById(R.id.stroke_user).setVisibility(View.GONE);
		}
		btn_user_plan.setVisibility(View.GONE);
		btn_user_favourities.setVisibility(View.GONE);
		btn_user_facultity.setVisibility(View.GONE);

		btn_refesh.setVisibility(View.GONE);
		findViewById(R.id.stroke_refresh).setVisibility(View.GONE);

		btn_about.setVisibility(View.GONE);
		findViewById(R.id.stroke_about).setVisibility(View.GONE);

		btn_sponsor.setVisibility(View.GONE);
		findViewById(R.id.stroke_sponsor).setVisibility(View.GONE);
	}

	public void showUserOptions() {
		if (userOptionsOpen) {
			btn_user_plan.setVisibility(View.GONE);
			btn_user_favourities.setVisibility(View.GONE);
			btn_user_facultity.setVisibility(View.GONE);
		} else {
			btn_user_plan.setVisibility(View.VISIBLE);
			btn_user_favourities.setVisibility(View.VISIBLE);
			btn_user_facultity.setVisibility(View.GONE);
		}
		userOptionsOpen = !userOptionsOpen;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_juwenalia:
			closeMenu();
			setItem(getResources().getString(R.string.menu_juwenalia));
			break;
		case R.id.menu_user:
			showUserOptions();
			setItem(app.userName);
			break;
		case R.id.menu_user_plan:
			closeMenu();
			setItem(getResources().getString(R.string.menu_user_plan));
			break;
		case R.id.menu_user_favourities:
			closeMenu();
			setItem(getResources().getString(R.string.menu_user_favourities));
			break;
		case R.id.menu_user_facultity:
			closeMenu();
			setItem(getResources().getString(R.string.menu_user_facultity));
			break;
		case R.id.menu_today:
			closeMenu();
			setItem(getResources().getString(R.string.menu_today));
			break;
		case R.id.menu_tomorrow:
			closeMenu();
			setItem(getResources().getString(R.string.menu_tomorrow));
			break;
		case R.id.menu_category:
			closeMenu();
			setItem(getResources().getString(R.string.menu_category));
			break;
		case R.id.menu_top10:
			closeMenu();
			setItem(getResources().getString(R.string.menu_top10));
			break;
		case R.id.menu_refresh:
			closeMenu();
			break;
		case R.id.menu_settings:
			closeMenu();
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.menu_login:
			closeMenu();
			if (ifLogedin()) {
				logoff();
				btn_login
						.setText(getResources().getString(R.string.menu_login));
				addItemsOnLogIn();
			} else {
				if (isLoginAvailable()) {
					startActivity(new Intent(this, LoginActivity.class));
				}
			}
			break;
		case R.id.menu_about:
			closeMenu();
			break;
		case R.id.menu_sponsor:
			closeMenu();
			break;
		case R.id.menu_exit:
			closeMenu();
			finish();
			break;
		}
	}

	private void setItem(String name) {
		for (int i = 0; i < listItems.size(); i++) {
			if (listItems.get(i).title.equals(name)) {
				mViewPager.setCurrentItem(i);
			}
		}
	}

}
