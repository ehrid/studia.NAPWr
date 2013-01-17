package pl.wroc.pwr.na.adapters;

import java.util.List;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.EventObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
		
		//the same for number of likes:
		// Get the text boxes from the listitem.xml file
		TextView likeSum = (TextView) alertView
				.findViewById(R.id.event_list_item_like_sum);

		// Assign the appropriate data from our alert object above
		likeSum.setText(Integer.toString(event.likeSum));
		
		//the same for content:
		// Get the text boxes from the listitem.xml file
		TextView content = (TextView) alertView
				.findViewById(R.id.event_list_item_content);

		// Assign the appropriate data from our alert object above
		content.setText(event.content);
		
		//the same for start date:
		// Get the text boxes from the listitem.xml file
		TextView startDate = (TextView) alertView
				.findViewById(R.id.event_list_item_start_date);

		// Assign the appropriate data from our alert object above
		startDate.setText(event.startDate.toString());
		
//		//the same for end date:
//		// Get the text boxes from the listitem.xml file
//		TextView endDate = (TextView) alertView
//				.findViewById(R.id.event_toDate);
//
//		// Assign the appropriate data from our alert object above
//		endDate.setText(event.endDate.toString());
		
		
		
		//the same for organization:
		// Get the text boxes from the listitem.xml file
		TextView organization = (TextView) alertView
				.findViewById(R.id.event_list_item_organization_name);

		// Assign the appropriate data from our alert object above
		organization.setText(event.organization.organizationName);
		
		
		//the same for small poster url:
		// Get the text boxes from the listitem.xml file
		WebView myWebView = (WebView) alertView.findViewById(R.id.webview);
		myWebView.loadUrl(event.poster.toString());
		

		return alertView;
	}
}