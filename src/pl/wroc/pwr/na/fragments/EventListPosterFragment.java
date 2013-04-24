package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class EventListPosterFragment extends Fragment {
	public static final String LIST_TITLE = "listName";
	public static final String LIST_URL = "url";

	Bitmap background;
	String url;

	ImageView menu;
	ImageView poster;
	TextView title;
	View rootView;

	Bundle args;
	LongOperation asyncTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.fragment_event_poster,
				container, false);

		this.rootView = rootView;
		menu = (ImageView) rootView
				.findViewById(R.id.fragment_event_poster_menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).openMenu();
			}
		});

		args = getArguments();
		url = args.getString(LIST_URL);

		title = (TextView) rootView
				.findViewById(R.id.fragment_event_poster_title);
		title.setText(args.getString(LIST_TITLE));

		poster = (ImageView) rootView
				.findViewById(R.id.fragment_event_poster_poster);

		Log.d("ORIENTATION",
				((MenuActivity) (MenuActivity.activityMain))
						.getScreenOrientation() + "");
		if (((MenuActivity) (MenuActivity.activityMain)).getScreenOrientation() == 1) {
			url += "_portrait";
		} else {
			url += "_landscape";
		}

		Log.d("POSTER", "STARTING ASYNC TASK");
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

	private void addPoster() {
		asyncTask.cancel(true);
		title.setVisibility(View.GONE);
		poster.setBackgroundDrawable(new BitmapDrawable(background));
		Animation myFadeInAnimation = AnimationUtils.loadAnimation(
				rootView.getContext(), R.anim.fadein);
		poster.startAnimation(myFadeInAnimation);
		poster.setVisibility(View.VISIBLE);
		((MenuActivity) (MenuActivity.activityMain)).mViewPager
				.refreshDrawableState();

		Log.d("POSTER", "ASYNC TASK STATUS - " + asyncTask.isCancelled());
	}

	private class LongOperation extends AsyncTask<String, Void, String> {
		boolean running = true;

		@Override
		protected String doInBackground(String... params) {
			while (running) {
				Log.d("POSTER", "ENTERED BACKGROUND - ASYNC TASK");
				background = ((MenuActivity) (MenuActivity.activityMain))
						.getBitmapFromUIS(url);

				Log.d("TLO", (background != null) + "/" + url);
				if (background == null
						&& ((MenuActivity) (MenuActivity.activityMain))
								.haveToDownloadBackground()) {
					background = ((MenuActivity) (MenuActivity.activityMain))
							.saveBitmapToUIS(url);
				}
				running = false;
				if (isCancelled())
					break;
			}

			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			if (background != null) {
				addPoster();
			}
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
