package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.na.R;
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
	private CommentsAdapter adapter;
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

		adapter = new CommentsAdapter(this, R.layout.item_event_list, eventList);
		eventListView.setAdapter(adapter);
	}

	private class CommentsAdapter extends ArrayAdapter<EventObject> {

		int resource;
		String response;
		Context context;

		// Initialize adapter
		public CommentsAdapter(Context context, int resource,
				List<EventObject> items) {
			super(context, resource, items);
			this.resource = resource;

		}

		public EventObject getEvent(int position) {
			return getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout alertView;
			// Get the current alert object
			EventObject event = getItem(position);

			// Inflate the view
			if (convertView == null) {
				alertView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi;
				vi = (LayoutInflater) getContext().getSystemService(inflater);
				vi.inflate(resource, alertView, true);
			} else {
				alertView = (LinearLayout) convertView;
			}
			// Get the text boxes from the listitem.xml file
			TextView name = (TextView) alertView
					.findViewById(R.id.event_list_item_name);

			// Assign the appropriate data from our alert object above
			name.setText(event.name);

			return alertView;
		}
	}
}
