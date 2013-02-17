package pl.wroc.pwr.na.fragments;

import java.util.ArrayList;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.adapters.PlanAdapter;
import pl.wroc.pwr.na.objects.PlanObject;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class PlanObjectFragment extends Fragment {

	private TextView title;
	private ListView eventListView;
	private PlanAdapter adapter;
	private Context context;
	ArrayList<PlanObject> eventList;
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
		title.setText("Plan PWr");
		eventListView = (ListView) rootView
				.findViewById(R.id.event_list_events);
		addEvents();

		if (eventList.isEmpty()) {
			rootView.findViewById(R.id.no_plan_popup).setVisibility(
					View.VISIBLE);
			Linkify.addLinks(
					(TextView) rootView.findViewById(R.id.no_plan_popup_link),
					Linkify.ALL);
		}

		return rootView;
	}

	public void addEvents() {
		eventList = ((MenuActivity) (MenuActivity.activityMain)).kalendarz;
		
		adapter = new PlanAdapter(context, R.layout.item_plan,
				eventList);
		eventListView.setAdapter(adapter);
	}
}