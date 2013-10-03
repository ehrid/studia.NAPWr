package pl.wroc.pwr.na.adapters;

import java.util.List;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.PlanObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlanAdapter extends ArrayAdapter<PlanObject> {

	int resource;
	String response;
	Context context;

	// Initialize adapter
	public PlanAdapter(Context context, int resource, List<PlanObject> items) {
		super(context, resource, items);
		this.resource = resource;

	}

	public PlanObject getEvent(int position) {
		return getItem(position);
	}

	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout alertView;
		// Get the current alert object
		PlanObject event = getItem(position);

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

		TextView time = (TextView) alertView.findViewById(R.id.plan_time);
		TextView title = (TextView) alertView.findViewById(R.id.plan_title);
		TextView place = (TextView) alertView.findViewById(R.id.plan_place);
		TextView teacher = (TextView) alertView.findViewById(R.id.plan_teacher);
		TextView type = (TextView) alertView.findViewById(R.id.plan_type);
		TextView date = (TextView) alertView.findViewById(R.id.plan_date);
		LinearLayout item = (LinearLayout) alertView
				.findViewById(R.id.plan_item);
		LinearLayout separator = (LinearLayout) alertView
				.findViewById(R.id.plan_separator);

		if (event._type.equals("separator")) {
			separator.setVisibility(View.VISIBLE);
			item.setVisibility(View.GONE);

			date.setText(event._date);
		} else {
			separator.setVisibility(View.GONE);
			item.setVisibility(View.VISIBLE);
			time.setText(event._time);
			title.setText(event._title);
			place.setText(event._place);
			teacher.setText(event._lecturer);

			String sType = event._type;
			type.setText(sType);

			if (sType.equals("W")) {
				item.setBackgroundDrawable(alertView.getResources()
						.getDrawable(R.drawable.plan_wyklad));
			} else if (sType.equals("C")) {
				item.setBackgroundDrawable(alertView.getResources()
						.getDrawable(R.drawable.plan_cwiczenia));
			} else if (sType.equals("P")) {
				item.setBackgroundDrawable(alertView.getResources()
						.getDrawable(R.drawable.plan_projekt));
			} else if (sType.equals("S")) {
				item.setBackgroundDrawable(alertView.getResources()
						.getDrawable(R.drawable.plan_seminarium));
			} else if (sType.equals("L")) {
				item.setBackgroundDrawable(alertView.getResources()
						.getDrawable(R.drawable.plan_laborki));
			} else if (sType.equals("X")) {
				item.setBackgroundDrawable(alertView.getResources()
						.getDrawable(R.drawable.plan_inne));
			}
		}

		return alertView;
	}
}