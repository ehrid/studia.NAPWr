package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.Date;
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

	public EventObject event;

	private static EventListActivity singleInstance = null;

	public static EventListActivity getInstance() {
		return singleInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		context = this.getApplicationContext();

		title = (TextView) findViewById(R.id.eventlist_title);
		title.setText(getIntent().getStringExtra(LIST_TITLE));
		eventListView = (ListView) findViewById(R.id.event_list_events);
		addEvents();

		singleInstance = this;

		eventListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				event = adapter.getEvent(position);

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

		for(int i=1; i < 10; i++)
		{
			eventList.add(new EventObject("Wydarznie"+i, i, "Lorem ipsum dolor sit amet","http://www.obrazek.pl",7*i, new Date()));
		}
		

		adapter = new EventListAdapter(this, R.layout.item_event_list,
				eventList);
		eventListView.setAdapter(adapter);
	}

}
