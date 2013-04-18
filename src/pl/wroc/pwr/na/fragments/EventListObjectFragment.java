package pl.wroc.pwr.na.fragments;

import java.util.ArrayList;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.EventActivity;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.adapters.EventListAdapter;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.tools.JSONParser;
import pl.wroc.pwr.na.tools.RSSParser;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EventListObjectFragment extends Fragment {

	public static final String LIST_TITLE = "list_title";
	public static final String LIST_MINIATURE = "list_miniature";
	public static final String LIST_TYPE = "list_type";
	public static final String LIST_URL = "list_url";

	private int type;
	private String url;

	private TextView title;
	private ImageView miniature;
	private ImageView menu;
	private ListView eventListView;
	private EventListAdapter adapter;
	private ProgressBar loading;
	private Context context;
	ArrayList<EventObject> eventList;
	Bundle args;

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.fragment_event_list,
				container, false);
		this.rootView = rootView;

		args = getArguments();
		context = rootView.getContext();

		title = (TextView) rootView.findViewById(R.id.eventlist_title);
		title.setText(args.getString(LIST_TITLE));

		type = args.getInt(LIST_TYPE);
		url = args.getString(LIST_URL);

		miniature = (ImageView) rootView.findViewById(R.id.eventlist_miniature);
		miniature.setImageResource(args.getInt(LIST_MINIATURE));

		loading = (ProgressBar) rootView.findViewById(R.id.event_list_loading);

		menu = (ImageView) rootView.findViewById(R.id.eventlist_menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).openMenu();
			}
		});

		eventListView = (ListView) rootView
				.findViewById(R.id.event_list_events);

		new LongOperation().execute("");

		return rootView;
	}

	private void addEvents() {
		eventList = ((MenuActivity) (MenuActivity.activityMain)).eventList
				.get(args.getString(LIST_TITLE));

		adapter = new EventListAdapter(context, R.layout.item_event_list_2,
				eventList,
				((MenuActivity) (MenuActivity.activityMain))
						.getApplicationContext());
		eventListView.setAdapter(adapter);
	}

	private void addListeners() {
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

		if (eventList.isEmpty()) {
			rootView.findViewById(R.id.no_events_popup).setVisibility(
					View.VISIBLE);
		}
	}

	private void showLoading() {
		loading.setVisibility(View.VISIBLE);
	}

	private void hideLoading() {
		loading.setVisibility(View.GONE);
	}

	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			if (((MenuActivity) (MenuActivity.activityMain))
					.haveToDownload(args.getString(LIST_TITLE))) {
				if (type == 1) {
					RSSParser rssParser = new RSSParser();
					((MenuActivity) (MenuActivity.activityMain)).addEventItem(
							args.getString(LIST_TITLE),
							rssParser.getEventsRSS(url));
				} else {
					JSONParser jsonParser = new JSONParser();
					((MenuActivity) (MenuActivity.activityMain)).addEventItem(
							args.getString(LIST_TITLE),
							jsonParser.getEventsJSON(url));
				}
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			hideLoading();
			addEvents();
			addListeners();
		}

		@Override
		protected void onPreExecute() {
			showLoading();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
