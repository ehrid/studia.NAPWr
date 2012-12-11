package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.EventObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends Activity implements OnClickListener {

	// Header
	Button back;
	Button share;

	// Event
	Button likeit;
	TextView title;
	TextView fromDate;
	TextView toDate;
	TextView address;
	TextView content;
	ImageView poster;

	EventObject event;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		back = (Button) findViewById(R.id.back);
		share = (Button) findViewById(R.id.share);

		title = (TextView) findViewById(R.id.event_title);
		likeit = (Button) findViewById(R.id.event_likeit);
		fromDate = (TextView) findViewById(R.id.event_fromDate);
		toDate = (TextView) findViewById(R.id.event_toDate);
		address = (TextView) findViewById(R.id.event_address);
		content = (TextView) findViewById(R.id.event_content);
		poster = (ImageView) findViewById(R.id.event_poster);

		event = ((EventListActivity)((EventListActivity.getInstance()))).event;

		content.setText(event.name);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
