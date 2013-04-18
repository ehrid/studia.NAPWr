package pl.wroc.pwr.na.activities;

import java.util.ArrayList;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConnectionErrorActivity extends Activity {

	Button reconnect;
	Button offline;

	NAPWrApplication app;
	UseInternalStorage uis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection_error);

		reconnect = (Button) findViewById(R.id.connectionError_reconnect);
		offline = (Button) findViewById(R.id.connectionError_offline);

		uis = new UseInternalStorage(getApplicationContext());
		app = (NAPWrApplication) getApplication();

		reconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				ConnectionErrorActivity.this.startActivity(new Intent(
						ConnectionErrorActivity.this,
						SplashScreenActivity.class));
			}
		});

		if (uis.readObject("dzisiaj") == null) {
			offline.setVisibility(View.GONE);
		} else {
			offline.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					app.eventList.put(
							getResources().getString(R.string.menu_top10_sub),
							(ArrayList<EventObject>) uis
									.readObject(getResources().getString(
											R.string.menu_today_sub)));
					app.eventList.put(
							getResources().getString(R.string.menu_today_sub),
							(ArrayList<EventObject>) uis
									.readObject(getResources().getString(
											R.string.menu_today_sub)));
					app.eventList.put(
							getResources()
									.getString(R.string.menu_tomorrow_sub),
							(ArrayList<EventObject>) uis
									.readObject(getResources().getString(
											R.string.menu_tomorrow_sub)));

					if (app.logedin) {
						app.kalendarz = (ArrayList<PlanObject>) uis
								.readObject(getResources().getString(
										R.string.menu_user_plan));
						app.eventList
								.put(getResources().getString(
										R.string.menu_user_favourities),
										(ArrayList<EventObject>) uis
												.readObject(getResources()
														.getString(
																R.string.menu_user_favourities)));
					}

					finish();
					ConnectionErrorActivity.this.startActivity(new Intent(
							ConnectionErrorActivity.this, MenuActivity.class));
				}
			});
		}
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	};
}
