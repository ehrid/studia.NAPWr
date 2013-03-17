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
					app.dzisiaj = (ArrayList<EventObject>) uis
							.readObject("dzisiaj");
					app.top10 = (ArrayList<EventObject>) uis
							.readObject("top10");
					app.jutro = (ArrayList<EventObject>) uis
							.readObject("jutro");
					if (app.logedin) {
						app.kalendarz = (ArrayList<PlanObject>) uis
								.readObject("kalendarz");
						app.ulubione = (ArrayList<EventObject>) uis
								.readObject("ulubione");
					}

					finish();
					ConnectionErrorActivity.this.startActivity(new Intent(
							ConnectionErrorActivity.this, MenuActivity.class));
				}
			});
		}
	}

}
