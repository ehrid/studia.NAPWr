package pl.wroc.pwr.na.fragments;

import java.util.ArrayList;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.EventActivity;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.adapters.EventListAdapter;
import pl.wroc.pwr.na.objects.EventObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class EventListObjectFragment extends Fragment {

	public static final String LIST_TITLE = "list_title";

	private TextView title;
	private ListView eventListView;
	private EventListAdapter adapter;
	private Context context;
	ArrayList<EventObject> eventList;
	Bundle args;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.fragment_event_list,
				container, false);

		args = getArguments();
		context = rootView.getContext();

		title = (TextView) rootView.findViewById(R.id.eventlist_title);
		title.setText(args.getString(LIST_TITLE));
		eventListView = (ListView) rootView.findViewById(R.id.event_list_events);
		addEvents();

		eventListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(context, EventActivity.class);
				i.putExtra("START_ITEM", position);
				startActivity(i);
				((MenuActivity) (MenuActivity.activityMain)).current = eventList;
			}
		});

		return rootView;
	}

	public void addEvents() {
		if (args.getString(LIST_TITLE).equals("Top 10")) {
			eventList = ((MenuActivity) (MenuActivity.activityMain)).top10;
		} else if (args.getString(LIST_TITLE).equals("Dzisiaj")) {
			eventList = ((MenuActivity) (MenuActivity.activityMain)).dzisiaj;
		} else if (args.getString(LIST_TITLE).equals("Jutro")) {
			eventList = ((MenuActivity) (MenuActivity.activityMain)).jutro;
		} else if (args.getString(LIST_TITLE).equals("Kalendarz")) {
			eventList = ((MenuActivity) (MenuActivity.activityMain)).kalendarz;
		}

		adapter = new EventListAdapter(context, R.layout.item_event_list,
				eventList);
		eventListView.setAdapter(adapter);
	}
}
