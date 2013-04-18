package pl.wroc.pwr.na.fragments;

import java.util.ArrayList;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.adapters.PlanAdapter;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.PlanParser;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlanObjectFragment extends Fragment {
	
	public static final String LIST_URL = "list_url";

	private TextView title;
	private ImageView miniature;
	private ProgressBar loading;
	private ListView eventListView;
	private PlanAdapter adapter;
	private Context context;
	ArrayList<PlanObject> eventList;
	ImageView menu;
	Bundle args;
	
	View rootView;
	String url;

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
		title.setText("Plan zajęć");
		
		miniature = (ImageView) rootView.findViewById(R.id.eventlist_miniature);
		miniature.setImageResource(R.drawable.miniature_calendar);
		
		loading = (ProgressBar) rootView.findViewById(R.id.event_list_loading);
		
		eventListView = (ListView) rootView
				.findViewById(R.id.event_list_events);
		
		new LongOperation().execute("");
		
		return rootView;
	}

	public void addEvents() {
		eventList = ((MenuActivity) (MenuActivity.activityMain)).kalendarz;
		
		adapter = new PlanAdapter(context, R.layout.item_plan,
				eventList);
		eventListView.setAdapter(adapter);
	}
	
	private void addListeners(){

		if (eventList.isEmpty()) {
			rootView.findViewById(R.id.no_plan_popup).setVisibility(
					View.VISIBLE);
			Linkify.addLinks(
					(TextView) rootView.findViewById(R.id.no_plan_popup_link),
					Linkify.ALL);
			Linkify.addLinks(
					(TextView) rootView.findViewById(R.id.no_plan_popup_link2),
					Linkify.ALL);
		}
		
		menu = (ImageView) rootView.findViewById(R.id.eventlist_menu);
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).openMenu();
			}
		});
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
			if(((MenuActivity) (MenuActivity.activityMain)).kalendarz == null){
				((MenuActivity) (MenuActivity.activityMain)).getPlan();
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
