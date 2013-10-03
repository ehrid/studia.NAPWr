package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.objects.EventObject;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventObjectFragment extends Fragment {
	public static final String ARG_OBJECT = "object";

	// Header
	// Button back;
	// Button share;

	// Event
	//ImageView likeit;
	//TextView likeitSum;
	TextView title;
	TextView fromDate;
	TextView toDate;
	TextView address;
	TextView organizaer;
	TextView content;
	ImageView poster;

	EventObject event;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.fragment_event,
				container, false);

		// back = (Button) rootView.findViewById(R.id.back);
		// share = (Button) rootView.findViewById(R.id.share);

		title = (TextView) rootView.findViewById(R.id.event_title);
		//likeit = (ImageView) rootView.findViewById(R.id.event_likeit);
		fromDate = (TextView) rootView.findViewById(R.id.event_fromDate);
		toDate = (TextView) rootView.findViewById(R.id.event_toDate);
		address = (TextView) rootView.findViewById(R.id.event_address);
		organizaer = (TextView) rootView.findViewById(R.id.event_organizer);
		content = (TextView) rootView.findViewById(R.id.event_content);
		//likeitSum = (TextView) rootView.findViewById(R.id.event_likeit_sum);
		
		Typeface fontType = ((MenuActivity) (MenuActivity.activityMain)).getTypeFace();
		title.setTypeface(fontType);

		Bundle args = getArguments();
		event = ((MenuActivity) (MenuActivity.activityMain)).current.get(args
				.getInt(ARG_OBJECT));
		
		//likeitSum.setText(event.likeSum + "");
		if (event._name != null)
			title.setText(event._name.toString());
		if (event._startDate != null) {
			fromDate.setText("Od: " + event._startDate.toString());
		} else {
			fromDate.setVisibility(View.GONE);
		}
		if (event._endDate != null) {
			toDate.setText("Do: " + event._endDate.toString());
		} else {
			toDate.setVisibility(View.GONE);
		}
		if (event._address != null) {
			address.setText("Adres: " + event._address._address);
		} else {
			address.setVisibility(View.GONE);
		}
		if (event._organization != null) {
			organizaer.setText("Organizator: " + event._organization._name);
		} else {
			organizaer.setVisibility(View.GONE);
		}
		if (event._content != null)
			content.setText(event._content.toString());

//		poster.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Bundle args = getArguments();
//
//				new ShowPosterDialog(
//						((EventActivity) (EventActivity.activityMain)), args
//								.getInt(ARG_OBJECT), ((MenuActivity) (MenuActivity.activityMain)).getApplicationContext()).show();
//			}
//		});
//		
//		event.setImagePoster(((MenuActivity) (MenuActivity.activityMain)).getApplicationContext());

		return rootView;
	}
}
