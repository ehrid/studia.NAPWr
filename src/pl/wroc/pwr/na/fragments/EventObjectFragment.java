package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.EventActivity;
import pl.wroc.pwr.na.activities.EventListActivity;
import pl.wroc.pwr.na.objects.EventObject;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventObjectFragment extends Fragment {
	public static final String ARG_OBJECT = "object";

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
	Button poster;

	EventObject event;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.item_event, container,
				false);

		back = (Button) rootView.findViewById(R.id.back);
		share = (Button) rootView.findViewById(R.id.share);

		title = (TextView) rootView.findViewById(R.id.event_title);
		likeit = (Button) rootView.findViewById(R.id.event_likeit);
		fromDate = (TextView) rootView.findViewById(R.id.event_fromDate);
		toDate = (TextView) rootView.findViewById(R.id.event_toDate);
		address = (TextView) rootView.findViewById(R.id.event_address);
		content = (TextView) rootView.findViewById(R.id.event_content);
		poster = (Button) rootView.findViewById(R.id.event_poster);
		
		Bundle args = getArguments();
		event = ((EventListActivity) ((EventListActivity.getInstance()))).eventList.get(args.getInt(ARG_OBJECT));

		if (event.name != null)
			title.setText(event.name.toString());
		if (event.startDate != null)
			fromDate.setText(event.startDate.toString());
		if (event.endDate != null)
			toDate.setText(event.endDate.toString());
		if (event.address != null)
			address.setText(event.address.toString());
		if (event.content != null)
			content.setText(event.content.toString());

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EventActivity.activityMain.finish();
			}
		});
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		return rootView;
	}
}
