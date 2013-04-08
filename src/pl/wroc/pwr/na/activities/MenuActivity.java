package pl.wroc.pwr.na.activities;

import java.util.ArrayList;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.MenuCollectionPagerAdapter;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.ListItemObject;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.EventController;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends FragmentActivity implements OnClickListener {

	public ArrayList<EventObject> current = new ArrayList<EventObject>();
	public ArrayList<EventObject> top10;
	public ArrayList<EventObject> dzisiaj;
	public ArrayList<EventObject> jutro;
	public ArrayList<PlanObject> kalendarz;
	public ArrayList<EventObject> ulubione;
	public ArrayList<ListItemObject> listItems;

	private boolean openMenu = false;

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
		makeItemList();
		mCollectionPagerAdapter = new MenuCollectionPagerAdapter(
				getSupportFragmentManager(), listItems);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager_menu);
		mViewPager.setAdapter(mCollectionPagerAdapter);

		activityMain = this;

		top10 = ((NAPWrApplication) getApplication()).top10;
		dzisiaj = ((NAPWrApplication) getApplication()).dzisiaj;
		jutro = ((NAPWrApplication) getApplication()).jutro;
		kalendarz = ((NAPWrApplication) getApplication()).kalendarz;
		ulubione = ((NAPWrApplication) getApplication()).ulubione;

		findViewById(R.id.left_menu).bringToFront();

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
		if (((NAPWrApplication) getApplication()).top10.size() == 0) {
			finish();
			startActivity(new Intent(this, SplashScreenActivity.class));
		}
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
	 * LOGIKA DZIAŁANIA MENU
	 */

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

	private void makeItemList() {
		/*
		 * TYPE: 
		 * 0 - LISTA WYDARZEŃ JSON 
		 * 1 - LISTA WYDARZEŃ RSS 
		 * 2 - POSTER 
		 * 3 - PLAN ZAJĘĆ
		 * 
		 * new ListItemObject(type, title, miniature)
		 */
		listItems = new ArrayList<ListItemObject>();
		listItems.add(new ListItemObject(2, "JUWENALIA", 0));
		if (ifLogedin()) {
			listItems.add(new ListItemObject(2, "user_name", 0));
			listItems.add(new ListItemObject(3, "Plan zajęć",
					R.drawable.miniature_calendar));
			listItems.add(new ListItemObject(0, "Ulubione",
					R.drawable.miniature_favourites));
		}
		listItems.add(new ListItemObject(2, "DZISIAJ", 0));
		listItems.add(new ListItemObject(0, "Dzisiaj",
				R.drawable.miniature_today));
		listItems.add(new ListItemObject(2, "JUTRO", 0));
		listItems.add(new ListItemObject(0, "Jutro",
				R.drawable.miniature_tommorow));
		listItems.add(new ListItemObject(2, "KATEGORIE", 0));
		listItems.add(new ListItemObject(2, "TOP 10", 0));
		listItems.add(new ListItemObject(0, "Top 10",
				R.drawable.miniature_top10));
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

		if (ifLogedin()) {
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

		if (ifLogedin()) {
			btn_user.setVisibility(View.GONE);
			findViewById(R.id.stroke_user).setVisibility(View.GONE);
		}

		btn_refesh.setVisibility(View.GONE);
		findViewById(R.id.stroke_refresh).setVisibility(View.GONE);

		btn_about.setVisibility(View.GONE);
		findViewById(R.id.stroke_about).setVisibility(View.GONE);

		btn_sponsor.setVisibility(View.GONE);
		findViewById(R.id.stroke_sponsor).setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_juwenalia:
			closeMenu();
			setItem("JUWENALIA");
			break;
		case R.id.menu_user:
			closeMenu();
			setItem("user_name");
			break;
		case R.id.menu_today:
			closeMenu();
			setItem("DZISIAJ");
			break;
		case R.id.menu_tomorrow:
			closeMenu();
			setItem("JUTRO");
			break;
		case R.id.menu_category:
			closeMenu();
			setItem("KATEGORIE");
			break;
		case R.id.menu_top10:
			closeMenu();
			setItem("TOP 10");
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
