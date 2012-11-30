package pl.wroc.pwr.na;

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

public class NAPWr extends Activity implements OnClickListener {

	private int ACTIVE_OPTION;

	Button top10;
	Button today;
	Button tommorow;
	Button callendar;
	Button favourites;
	Button login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ACTIVE_OPTION = 2;

		if (Preferences.getFulscreen(this)) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		setContentView(R.layout.main);

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
		
		makeAtive(ACTIVE_OPTION);
	}

	public void makeAtive(int i) {
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
	}
	
	public void makeBasicBackground(){
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
			startActivity(new Intent(this, Login.class));
			break;

		case R.id.main_button_login:
			startActivity(new Intent(this, Login.class));
			break;
		}
	}

	public void startList(String name) {
		Intent i = new Intent(this, EventList.class);
		i.putExtra(EventList.LIST_TITLE, name);
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
			startActivity(new Intent(this, Preferences.class));
			return true;
		case R.id.menu_exit:
			finish();
			return true;
		}
		return false;
	}
}
