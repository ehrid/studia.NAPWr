package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {

	private TextView title;
	private ImageView miniature;
	NAPWrApplication app;

	private TextView plan;
	private ImageView menu;
	private Spinner facultity;
	private RadioGroup exit;
	private RadioGroup refresh;
	private CheckBox notifications;
	private Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		app = (NAPWrApplication) getApplication();

		title = (TextView) findViewById(R.id.eventlist_title);
		title.setText("Ustawienia");
		
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf"); 
		title.setTypeface(type);

		miniature = (ImageView) findViewById(R.id.eventlist_miniature);
		miniature.setImageResource(R.drawable.miniature_settings);
		
		menu = (ImageView) findViewById(R.id.btn_menu);
		menu.setVisibility(View.GONE);

		plan = (TextView) findViewById(R.id.settings_plan);
		plan.setText(app.getSettingsPlan());

		facultity = (Spinner) findViewById(R.id.settings_facultity);

		List<String> list = new ArrayList<String>();
		list.add("W-1 Architektura");
		list.add("W-2 Budiwnictwao");
		list.add("W-3 Chemia");
		list.add("W-4 Elektronika");
		list.add("W-5 Elektryczny");
		list.add("W-6 WGGG");
		list.add("W-7 IŚ");
		list.add("W-8 WIZ");
		list.add("W-9 WME");
		list.add("W-10 Mechaniczny");
		list.add("W-11 WPPT");
		list.add("W-12 WEMiF");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, list);

		facultity.setAdapter(adapter);
		facultity.setSelection(app.getSettingsWydzial());

		exit = (RadioGroup) findViewById(R.id.settings_exit);
		switch (app.getSettingsZamknij()) {
		case 0:
			exit.check(R.id.radio0);
			break;

		case 1:
			exit.check(R.id.radio1);
			break;
		}

		notifications = (CheckBox) findViewById(R.id.settings_notifications);
		notifications.setSelected(app.getSettingsPowiadomienia());

		refresh = (RadioGroup) findViewById(R.id.settings_refresh);
		switch (app.getSettingsOdswierzanie()) {
		case 0:
			refresh.check(R.id.radio2);
			break;
		case 1:
			refresh.check(R.id.radio3);
			break;
		case 2:
			refresh.check(R.id.radio4);
			break;
		}

		save = (Button) findViewById(R.id.settings_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				app.saveSettings(plan.getText().toString(), facultity
						.getSelectedItemPosition(), exit
						.indexOfChild(findViewById(exit
								.getCheckedRadioButtonId())), notifications
						.isChecked(), refresh.indexOfChild(findViewById(refresh
						.getCheckedRadioButtonId())));
				finish();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
