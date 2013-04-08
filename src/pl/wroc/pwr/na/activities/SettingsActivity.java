package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.na.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {
	
	private TextView title;
	private ImageView miniature;
	private ImageView menu;
	
	private Spinner facultity;
	private RadioGroup exit;
	private RadioGroup refresh;
	private CheckBox notifications;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		title = (TextView) findViewById(R.id.eventlist_title);
		title.setText("Ustawienia");
		
		miniature = (ImageView) findViewById(R.id.eventlist_miniature);
		miniature.setImageResource(R.drawable.miniature_settings);
		
		menu = (ImageView) findViewById(R.id.eventlist_menu);
		menu.setVisibility(View.GONE);
		
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
		
		exit = (RadioGroup) findViewById(R.id.settings_exit);
		refresh = (RadioGroup) findViewById(R.id.settings_refresh);

		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
