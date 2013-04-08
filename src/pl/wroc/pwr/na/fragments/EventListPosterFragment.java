package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventListPosterFragment extends Fragment {
	public static final String LIST_TITLE = "listName";
	
	ImageView menu;
	TextView name;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.fragment_event_poster,
				container, false);

		menu = (ImageView) rootView.findViewById(R.id.fragment_event_poster_menu);
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).openMenu();
			}
		});
		
		name = (TextView) rootView.findViewById(R.id.fragment_event_poster_name);
		Bundle args = getArguments();
		name.setText(args.getString(LIST_TITLE));

		return rootView;
	}
}
