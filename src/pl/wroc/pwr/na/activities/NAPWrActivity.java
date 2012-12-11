package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class NAPWrActivity extends Activity implements OnClickListener {

	private int ACTIVE_OPTION;
	private boolean LOOGED_IN;

	Button top10;
	Button today;
	Button tommorow;
	Button callendar;
	Button favourites;
	Button login;
	TextView loginText;

	private static NAPWrActivity singleInstance = null;

	public static NAPWrActivity getInstance() {
		return singleInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ACTIVE_OPTION = 2;
		setLOOGED_IN(false);

		if (PreferencesActivity.getFulscreen(this)) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		setContentView(R.layout.activity_main);

		// odszukujemy przyciski i podpinamy pod nie listenery
		top10 = (Button) findViewById(R.id.main_button_top10);
		top10.setOnClickListener(this);
		today = (Button) findViewById(R.id.main_button_today);
		today.setOnClickListener(this);
		tommorow = (Button) findViewById(R.id.main_button_tommorow);
		tommorow.setOnClickListener(this);
		callendar = (Button) findViewById(R.id.main_button_callendar);
		callendar.setOnClickListener(this);
		favourites = (Button) findViewById(R.id.main_button_favourites);
		favourites.setOnClickListener(this);
		login = (Button) findViewById(R.id.main_button_login);
		login.setOnClickListener(this);
		loginText = (TextView) findViewById(R.id.main_textView_login);

		singleInstance = this;

		makeAtive(ACTIVE_OPTION);
	}

	@Override
	protected void onResume() {
		super.onResume();
		makeAtive(ACTIVE_OPTION);
	}

	public void makeAtive(int i) {
		makeBasicBackground();
		Button b = today;
		switch (i) {
		case 1:
			b = top10;
			break;
		case 2:
			b = today;
			break;
		case 3:
			b = tommorow;
			break;
		case 4:
			b = callendar;
			break;
		case 5:
			b = favourites;
			break;
		case 6:
			b = login;
			break;
		}
		b.setBackgroundResource(R.drawable.custom_button_selected);

		if (isLOOGED_IN()) {
			login.setBackgroundResource(R.drawable.custom_button_selected);
		}
	}

	public void makeBasicBackground() {
		top10.setBackgroundResource(R.drawable.custom_button);
		today.setBackgroundResource(R.drawable.custom_button);
		tommorow.setBackgroundResource(R.drawable.custom_button);
		callendar.setBackgroundResource(R.drawable.custom_button);
		favourites.setBackgroundResource(R.drawable.custom_button);
		login.setBackgroundResource(R.drawable.custom_button);
	}

	@Override
	public void onClick(View v) {

		// Wykonywanie akcji w przypadku nacisniecia ktoregos z przyciskow
		switch (v.getId()) {
		case R.id.main_button_top10:
			startList("Top 10");
			break;

		case R.id.main_button_today:
			startList("Dzisiaj");
			break;

		case R.id.main_button_tommorow:
			startList("Jutro");
			break;

		case R.id.main_button_callendar:
			startList("Kalendarz");
			break;

		case R.id.main_button_favourites:
			if (LOOGED_IN) {
				startList("Ulubione");
			} else {
			startActivity(new Intent(this, LoginActivity.class));
			}
			break;

		case R.id.main_button_login:
			if (LOOGED_IN) {
				setLOOGED_IN(false);
				setLogedInLabel(false);
				makeAtive(ACTIVE_OPTION);
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		}
	}

	public void startList(String name) {
		Intent i = new Intent(this, EventListActivity.class);
		i.putExtra(EventListActivity.LIST_TITLE, name);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Wykonywanie akcji w przypadku naci�ni�cia kt�rego� z przycisk�w menu
		// systemowego
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, PreferencesActivity.class));
			return true;
		case R.id.menu_exit:
			finish();
			return true;
		}
		return false;
	}

	public boolean isLOOGED_IN() {
		return LOOGED_IN;
	}

	public void setLOOGED_IN(boolean LOOGED_IN) {
		this.LOOGED_IN = LOOGED_IN;
	}

	public void setLogedInLabel(boolean logged) {
		if (logged) {
			loginText.setText(R.string.label_logout);
		} else {
			loginText.setText(R.string.label_login2);
		}
	}

}
