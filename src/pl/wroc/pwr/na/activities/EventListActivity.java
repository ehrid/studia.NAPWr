package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.EventListAdapter;
import pl.wroc.pwr.na.objects.EventObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventListActivity extends Activity implements OnClickListener {

	public static final String LIST_TITLE = "pl.wroc.pwr.na.list_title";

	private TextView title;
	private ListView eventListView;
	private EventListAdapter adapter;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		
		context = this.getApplicationContext();

		title = (TextView) findViewById(R.id.eventlist_title);
		title.setText(getIntent().getStringExtra(LIST_TITLE));
		eventListView = (ListView) findViewById(R.id.event_list_events);
		addEvents();

		eventListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				EventObject event = adapter.getEvent(position);

				Intent i = new Intent(context, LoginActivity.class);
				i.putExtra(EventActivity.CURRENT_EVENT_OBJECT, ""+event.id);
				
				startActivity(new Intent(context, EventActivity.class));
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public void addEvents() {
		ArrayList<EventObject> eventList = new ArrayList<EventObject>();

		eventList.add(new EventObject("Wydarznie1", 1));
		eventList.add(new EventObject("Wydarznie2", 2));
		eventList.add(new EventObject("Wydarznie3", 3));
		eventList.add(new EventObject("Wydarznie4", 4));
		eventList.add(new EventObject("Wydarznie5", 5));
		eventList.add(new EventObject("Wydarznie6", 6));
		eventList.add(new EventObject("Wydarznie7", 7));

		adapter = new EventListAdapter(this, R.layout.item_event_list, eventList);
		eventListView.setAdapter(adapter);
	}

}
