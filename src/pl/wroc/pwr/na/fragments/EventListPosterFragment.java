package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class EventListPosterFragment extends Fragment {
	public static final String LIST_TITLE = "listName";

	Bitmap background;
	String poster_name;

	ImageView menu;
	ImageView poster;

	Bundle args;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.fragment_event_poster,
				container, false);

		menu = (ImageView) rootView
				.findViewById(R.id.fragment_event_poster_menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).openMenu();
			}
		});

		args = getArguments();
		poster_name = args.getString(LIST_TITLE);

		poster = (ImageView) rootView
				.findViewById(R.id.fragment_event_poster_poster);

		Log.d("ORIENTATION", ((MenuActivity) (MenuActivity.activityMain)).getScreenOrientation() +"");
		if (((MenuActivity) (MenuActivity.activityMain)).getScreenOrientation() == 1) {
			poster_name += "_portiat";
		} else {
			poster_name += "_landscape";
		}

		new LongOperation().execute("");

		return rootView;
	}

	private void addPoster() {
		poster.setImageDrawable(new BitmapDrawable(background));
		poster.setVisibility(View.VISIBLE);
		((MenuActivity) (MenuActivity.activityMain)).mViewPager
				.refreshDrawableState();
	}

	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			background = ((MenuActivity) (MenuActivity.activityMain))
					.getBitmapFromUIS(poster_name);

			Log.d("TLO", (background != null) + "/" +poster_name);
			if (background == null) {
				background = ((MenuActivity) (MenuActivity.activityMain))
						.saveBitmapToUIS(poster_name);
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
