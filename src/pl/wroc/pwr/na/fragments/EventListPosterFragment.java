package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.activities.MenuActivity.ClickListenerForScrolling;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class manages creating view for event list poster
 * 
 * @author horodysk
 */
public class EventListPosterFragment extends Fragment {
    /***/
    public static final String LIST_TITLE = "listName";

    /***/
    public static final String LIST_URL = "url";

    MenuActivity menuActivity = MenuActivity.activityMain;

    Bitmap background;

    String url;

    private ImageView poster;

    private TextView posterTitle;

    private View _rootView;

    private ImageView menuButton;

    private LongOperation _asyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @SuppressWarnings("unused") Bundle savedInstanceState) {
        _rootView = getRootView(inflater, container);
        setUrl();
        setPosterTitle();
        setPosterTitelFont();
        setPoster();
        setMenuButton();
        setUrlSufix();
        startMyTask();

        return _rootView;
    }

    private View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_event_poster, container, false);
    }

    private void setUrl() {
        Bundle args = getArguments();
        url = args.getString(LIST_URL);
    }

    private void setPosterTitle() {
        Bundle args = getArguments();
        posterTitle = (TextView) _rootView.findViewById(R.id.fragment_event_poster_title);
        posterTitle.setText(args.getString(LIST_TITLE));
    }

    private void setPosterTitelFont() {
        Typeface fontType = menuActivity.getTypeFace();
        posterTitle.setTypeface(fontType);
    }

    private void setPoster() {
        poster = (ImageView) _rootView.findViewById(R.id.fragment_event_poster_poster);
    }

    private void setMenuButton() {
        menuButton = (ImageView) _rootView.findViewById(R.id.btn_menu);
        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuActivity.menuSlider.click();
            }
        });
    }

    private void setUrlSufix() {
        if (menuActivity.getScreenOrientation() == 1)
            url += "_portrait";
        else
            url += "_landscape";
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    // API 11
    void startMyTask() {
        _asyncTask = new LongOperation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            _asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            _asyncTask.execute();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        boolean running = true;

        public LongOperation() {
        }

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                background = menuActivity.getBitmapFromUIS(url);

                if (background == null && menuActivity.haveToDownloadBackground())
                    background = menuActivity.saveBitmapToUIS(url);

                running = false;
                if (isCancelled())
                    break;
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(@SuppressWarnings("unused") String result) {
            if (background != null)
                addPoster();
        }
    }

    void addPoster() {
        cancelAsynchPosterDownlaodTask();
        resetPoster();
        animatePosterIn();
        refreshStateOFView();
        bringMenuButtonToFront();
        closeSlider();
    }

    private void cancelAsynchPosterDownlaodTask() {
        _asyncTask.cancel(true);
    }

    private void resetPoster() {
        posterTitle.setVisibility(View.GONE);
        poster.setBackgroundDrawable(new BitmapDrawable(background));
    }

    private void animatePosterIn() {
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(_rootView.getContext(), R.anim.fadein);
        poster.startAnimation(myFadeInAnimation);
        poster.setVisibility(View.VISIBLE);
    }

    private void refreshStateOFView() {
        menuActivity.mViewPager.refreshDrawableState();
    }

    private void bringMenuButtonToFront() {
        menuButton.bringToFront();
    }

    private void closeSlider() {
        ClickListenerForScrolling menuSlider = menuActivity.menuSlider;
        if (menuSlider.isOpened()) {
            menuSlider.close();
            menuSlider.click();
        }
    }
}
