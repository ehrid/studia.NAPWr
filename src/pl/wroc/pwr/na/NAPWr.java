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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(Preferences.getFulscreen(this)){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		setContentView(R.layout.main);

		// odszukujemy przyciski i podpinamy pod nie listenery
		Button top10 = (Button) findViewById(R.id.button_top10);
		top10.setOnClickListener(this);
		Button today = (Button) findViewById(R.id.button_today);
		today.setOnClickListener(this);
		Button tommorow = (Button) findViewById(R.id.button_tommorow);
		tommorow.setOnClickListener(this);
		Button callendar = (Button) findViewById(R.id.button_callendar);
		callendar.setOnClickListener(this);
		Button favourites = (Button) findViewById(R.id.button_favourites);
		favourites.setOnClickListener(this);
		Button login = (Button) findViewById(R.id.button_login);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		// Wykonywanie akcji w przypadku naci�ni�cia kt�rego� z przycisk�w
		switch (v.getId()) {
		case R.id.button_top10:
			// tre��
			break;

		case R.id.button_today:
			// tre��
			break;

		case R.id.button_tommorow:
			// tre��
			break;

		case R.id.button_callendar:
			// tre��
			break;

		case R.id.button_favourites:
			// tre��
			break;

		case R.id.button_login:
			Intent i = new Intent(this, Login.class);
			startActivity(i);
			break;
		}
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
