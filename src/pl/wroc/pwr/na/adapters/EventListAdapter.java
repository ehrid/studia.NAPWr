package pl.wroc.pwr.na.adapters;

import java.util.List;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.EventObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventListAdapter extends ArrayAdapter<EventObject> {

	int resource;
	String response;
	Context context;

	// Initialize adapter
	public EventListAdapter(Context context, int resource,
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