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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlanObjectFragment extends Fragment {
	/***/
	public static final String LIST_URL = "list_url";

	private MenuActivity menuActivity = (MenuActivity) MenuActivity.activityMain;
	private ProgressBar _progresBar;
	private ListView _eventListView;
	private PlanAdapter adapter;
	private Context _context;
	private ArrayList<PlanObject> _eventList;
	private View _rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getRootView(inflater, container);
		getContext();
		TextView headerTitle = prepareHeaderTitle();
		setHeaderTitleFont(headerTitle);
		prepareHeaderMiniature();
		prepareMenuButton();
		loadProgresBar();
		loadEventListView();
		startAsyncTask();
		return _rootView;
	}

	private void getRootView(LayoutInflater inflater, ViewGroup container) {
		_rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
	}

	private void getContext() {
		_context = _rootView.getContext();
	}

	private TextView prepareHeaderTitle() {
		TextView headerTitle = (TextView) _rootView.findViewById(R.id.eventlist_title);
		headerTitle.setText("Plan zajęć");
		return headerTitle;
	}

	private void setHeaderTitleFont(TextView headerTitle) {
		Typeface fontType = menuActivity.getTypeFace();
		headerTitle.setTypeface(fontType);
	}

	private void prepareHeaderMiniature() {
		ImageView headerMiniature = (ImageView) _rootView.findViewById(R.id.eventlist_miniature);
		headerMiniature.setImageResource(R.drawable.miniature_calendar);
	}

	private void prepareMenuButton() {
		ImageView menuButton = (ImageView) _rootView.findViewById(R.id.btn_menu);

		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuActivity.clfs.click();
			}
		});
	}

	private void loadProgresBar() {
		_progresBar = (ProgressBar) _rootView.findViewById(R.id.event_list_loading);
	}

	private void loadEventListView() {
		_eventListView = (ListView) _rootView.findViewById(R.id.event_list_events);
	}

	private void startAsyncTask() {
		LongOperation asyncTask = new LongOperation();
		startMyTask(asyncTask);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// API 11
	private void startMyTask(LongOperation asyncTask) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			asyncTask.execute();
	}

	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			MenuActivity menuActivity = (MenuActivity) MenuActivity.activityMain;
			if (menuActivity.getPlanEvents() == null) {
				downloadPlan(menuActivity);
			}
			return "Executed";
		}

		private void downloadPlan(MenuActivity menuActivity) {
			if (!menuActivity.isOffline())
				menuActivity.getPlan();
			else
				menuActivity.preparePlan();
		}

		@Override
		protected void onPostExecute(String result) {
			hideLoadingView();
			addPlanEvents();
			showAlertIfNoPlanPresent();
		}

		@Override
		protected void onPreExecute() {
			showLoadingView();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private void addPlanEvents() {
		_eventList = menuActivity.getPlanEvents();

		if (_eventList != null) {
			adapter = new PlanAdapter(_context, R.layout.item_plan, _eventList);
			_eventListView.setAdapter(adapter);
		}
	}

	private void showAlertIfNoPlanPresent() {
		if (_eventList == null || _eventList.isEmpty())
			showNoPlanAlert();
	}

	private void showNoPlanAlert() {
		TextView link = (TextView) _rootView.findViewById(R.id.no_plan_popup_link);
		TextView link2 = (TextView) _rootView.findViewById(R.id.no_plan_popup_link2);
		View alert = _rootView.findViewById(R.id.no_plan_popup);

		alert.setVisibility(View.VISIBLE);
		Linkify.addLinks(link, Linkify.ALL);
		Linkify.addLinks(link2, Linkify.ALL);
	}

	private void showLoadingView() {
		_progresBar.setVisibility(View.VISIBLE);
	}

	private void hideLoadingView() {
		_progresBar.setVisibility(View.GONE);
	}
}
