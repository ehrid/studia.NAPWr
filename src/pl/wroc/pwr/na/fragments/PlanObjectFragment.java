package pl.wroc.pwr.na.fragments;

import java.util.ArrayList;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.adapters.PlanAdapter;
import pl.wroc.pwr.na.objects.PlanObject;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
	private ImageView menu;
	ArrayList<PlanObject> eventList;
	Bundle args;

	View rootView;
	String url;
	LongOperation asyncTask;

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

		Typeface fontType = ((MenuActivity) (MenuActivity.activityMain))
				.getTypeFace();
		title.setTypeface(fontType);

		miniature = (ImageView) rootView.findViewById(R.id.eventlist_miniature);
		miniature.setImageResource(R.drawable.miniature_calendar);

		menu = (ImageView) rootView.findViewById(R.id.btn_menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).clfs.click();
			}
		});

		loading = (ProgressBar) rootView.findViewById(R.id.event_list_loading);

		eventListView = (ListView) rootView
				.findViewById(R.id.event_list_events);

		asyncTask = new LongOperation();
		startMyTask();

		return rootView;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// API 11
	void startMyTask() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			asyncTask.execute();
	}

	public void addEvents() {
		eventList = ((MenuActivity) (MenuActivity.activityMain)).getKalendarz();

		if (eventList != null) {
			adapter = new PlanAdapter(context, R.layout.item_plan, eventList);
			eventListView.setAdapter(adapter);
		}
	}

	private void addListeners() {
		if (eventList != null) {
			if (eventList.isEmpty()) {
				rootView.findViewById(R.id.no_plan_popup).setVisibility(
						View.VISIBLE);
				Linkify.addLinks((TextView) rootView
						.findViewById(R.id.no_plan_popup_link), Linkify.ALL);
				Linkify.addLinks((TextView) rootView
						.findViewById(R.id.no_plan_popup_link2), Linkify.ALL);
			}
		} else {
			rootView.findViewById(R.id.no_plan_popup).setVisibility(
					View.VISIBLE);
			Linkify.addLinks((TextView) rootView
					.findViewById(R.id.no_plan_popup_link), Linkify.ALL);
			Linkify.addLinks((TextView) rootView
					.findViewById(R.id.no_plan_popup_link2), Linkify.ALL);
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
			if (((MenuActivity) (MenuActivity.activityMain)).getKalendarz() == null) {
				if(!((MenuActivity) (MenuActivity.activityMain))
							.isOffline()) {
					((MenuActivity) (MenuActivity.activityMain)).getPlan();
				} else {
					((MenuActivity) (MenuActivity.activityMain)).preparePlan();
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
