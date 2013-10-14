package pl.wroc.pwr.na.activities;

import java.util.ArrayList;
import java.util.HashMap;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.MenuCollectionPagerAdapter;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.ListItemObject;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.MyHorizontalScrollView;
import pl.wroc.pwr.na.tools.PlanParser;
import pl.wroc.pwr.na.tools.PosterController;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends FragmentActivity implements OnClickListener {
    public ArrayList<EventObject> current = new ArrayList<EventObject>();

    public ArrayList<ListItemObject> listItems;

    MenuCollectionPagerAdapter mCollectionPagerAdapter;

    public ViewPager mViewPager;

    Button login;

    TextView loginText;

    public NAPWrApplication app;

    MyHorizontalScrollView scrollView;

    public ClickListenerForScrolling menuSlider;

    View menu;

    View application;

    public static MenuActivity activityMain;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        LayoutInflater inflater = LayoutInflater.from(this);
        scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
        setContentView(scrollView);

        menu = inflater.inflate(R.layout.menu, null);
        application = inflater.inflate(R.layout.activity_main, null);

        menuSlider = new ClickListenerForScrolling(scrollView, menu);

        final View[] children = new View[] { menu, application };

        // Scroll to app (view[1]) when layout finished.
        int scrollToViewIdx = 1;
        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(menu, getWidthOfScreen()));

        app = (NAPWrApplication) getApplication();
        activityMain = this;

        // Create an adapter that when requested, will return a fragment
        // representing an object in
        // the collection.
        //
        // ViewPager and its adapters use support library fragments, so we must
        // use
        // getSupportFragmentManager.
        makeItemList();
        mCollectionPagerAdapter = new MenuCollectionPagerAdapter(getSupportFragmentManager(), listItems);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) application.findViewById(R.id.pager_menu);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        setMenu();
        showUserOptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            menuSlider.click();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!menuSlider.close()) {
                if (mViewPager.getCurrentItem() == 0) {
                    closeApplication();
                }
                else {
                    mViewPager.setCurrentItem(0);
                }
            }
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    };

    public void closeApplication() {
        if (app.getSettingsZamknij() == 0) {
            finish();
        }
        else {
            moveTaskToBack(true);
        }
    }

    public void logoff() {
        ((NAPWrApplication) getApplication()).forgetUser();
        removePlanObject();
        mViewPager.refreshDrawableState();
    }

    public boolean ifLogedin() {
        return ((NAPWrApplication) getApplication()).logedin;
    }

    @SuppressLint("ShowToast")
    public boolean isLoginAvailable() {
        boolean networkAvailable = isNetworkAvailable();
        if (!networkAvailable) {
            Toast.makeText(getApplicationContext(), "Brak aktywnego poÅ‚Ä…czenia", 2000).show();
        }
        return networkAvailable;
    }

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) MenuActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    Log.d("Internt Connection", "WIFI CONNECTION AVAILABLE");
                }
                else {
                    Log.d("Internt Connection", "WIFI CONNECTION NOT AVAILABLE");
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    Log.d("Internt Connection", "MOBILE INTERNET CONNECTION AVAILABLE");
                }
                else {
                    Log.d("Internt Connection", "MOBILE INTERNET CONNECTION NOT AVAILABLE");
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private boolean isWiFiAvailable() {
        boolean haveConnectedWifi = false;

        ConnectivityManager cm = (ConnectivityManager) MenuActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    Log.d("Internt Connection", "WIFI CONNECTION AVAILABLE");
                }
                else {
                    Log.d("Internt Connection", "WIFI CONNECTION NOT AVAILABLE");
                }
            }
        }
        return haveConnectedWifi;
    }

    /*
     * ELEMENTY DO POBIERANIA DYNAMICZNEGO
     */
    public HashMap<String, ArrayList<EventObject>> getEventList() {
        return app.eventList;
    }

    public void addEventItem(String key, ArrayList<EventObject> value) {
        if (app.eventList != null) {
            app.eventList.put(key, value);
            app.eventListUpdateTime.put(key, System.currentTimeMillis());
            app.saveEventList();
            mViewPager.refreshDrawableState();
        }
    }

    public boolean isOffline() {
        return !isNetworkAvailable();
    }

    public ArrayList<PlanObject> getPlanEvents() {
        return app.kalendarz;
    }

    public void getPlan() {
        PlanParser planParser = new PlanParser();
        savePlanObject(planParser.getPlan(app));
        preparePlan();
    }

    public void preparePlan() {
        PlanParser planParser = new PlanParser();
        app.kalendarz = planParser.preparePlan(getPlanObject());
        mViewPager.refreshDrawableState();
    }

    public boolean haveToDownload(String key) {
        if (isOffline()) {
            return false;
        }
        else {
            if (app.eventList != null) {
                if (app.eventList.containsKey(key)) {
                    int refreshType = app.getSettingsOdswierzanie();
                    int difference = 3600000;
                    if (refreshType != 0) {

                        if (refreshType == 2) {
                            difference *= 12;
                        }

                        Log.d("TIME", (System.currentTimeMillis() - app.eventListUpdateTime.get(key)) + "");
                        if ((System.currentTimeMillis() - app.eventListUpdateTime.get(key)) < difference) {
                            return false;
                        }
                        else {
                            Log.d("EVENTS", "TIME TO UPDATE - " + (System.currentTimeMillis() - app.eventListUpdateTime.get(key)));
                        }
                    }
                    else {
                        Log.d("EVENTS", "REFRESH == 0");
                        return false;
                    }
                }
                else {
                    Log.d("EVENTS", "DOES NOT COINTAIN KEY");
                }
            }
            else {
                Log.d("EVENTS", "EMPTY EVENT LIST");
            }
        }
        return true;
    }

    public Bitmap getBitmapFromUIS(String key) {
        PosterController pc = new PosterController();
        return pc.readPoster(key, getApplicationContext());
    }

    public Bitmap saveBitmapToUIS(String key) {
        PosterController pc = new PosterController();
        return pc.writePoster(key, getApplicationContext());
    }

    public boolean haveToDownloadBackground() {
        return isWiFiAvailable();
    }

    public int getScreenOrientation() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = Configuration.ORIENTATION_SQUARE;
        }
        else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }
            else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    public int getWidthOfScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public void savePlanObject(ArrayList<PlanObject> plan) {
        app.savePlanObject(plan);
    }

    public ArrayList<PlanObject> getPlanObject() {
        return app.getPlanObject();
    }

    public void removePlanObject() {
        app.removePlanObject();
    }

    /*
     * LOGIKA DZIAÅ�ANIA MENU
     */

    // MAIN
    TextView btn_juwenalia;

    TextView btn_user;

    TextView btn_today;

    TextView btn_tomorrow;

    TextView btn_it;

    TextView btn_kultura;

    TextView btn_sport;

    TextView btn_top10;

    TextView btn_refesh;

    TextView btn_settings;

    TextView btn_login;

    TextView btn_about;

    TextView btn_sponsor;

    TextView btn_exit;

    // ADDITIONAL
    TextView btn_user_plan;

    TextView btn_user_favourities;

    TextView btn_user_facultity;

    boolean userOptionsOpen = false;

    private void makeItemList() {
        /*
         * TYPE: 0 - LISTA WYDARZEÅƒ JSON 1 - LISTA WYDARZEÅƒ RSS 2 - POSTER 3 -
         * PLAN ZAJÄ˜Ä† new ListItemObject(type, title, miniature)
         */
        listItems = new ArrayList<ListItemObject>();
        if (ifLogedin()) {
            listItems.add(new ListItemObject(2, getResources().getString(R.string.menu_user_plan), 0, getResources().getString(
                R.string.cover_user_plan)));
            // listItems.add(new ListItemObject(3, getResources().getString(
            // R.string.menu_user_plan_sub),
            // R.drawable.miniature_calendar, ""));
            listItems.add(new ListItemObject(0, getResources().getString(R.string.menu_user_favourities), R.drawable.miniature_favourites, ""));
        }
        listItems.add(new ListItemObject(2, getResources().getString(R.string.menu_today), 0, getResources().getString(R.string.cover_today)));
        listItems.add(new ListItemObject(0, getResources().getString(R.string.menu_today_sub), R.drawable.miniature_today,
            "http://www.napwr.pl/mobile/wydarzenia/dzis"));
        listItems.add(new ListItemObject(2, getResources().getString(R.string.menu_tomorrow), 0, getResources().getString(R.string.cover_tomorrow)));
        listItems.add(new ListItemObject(0, getResources().getString(R.string.menu_tomorrow_sub), R.drawable.miniature_tommorow,
            "http://www.napwr.pl/mobile/wydarzenia/jutro"));
        listItems.add(new ListItemObject(2, getResources().getString(R.string.menu_it), 0, getResources().getString(R.string.cover_informatyka)));
        listItems
            .add(new ListItemObject(
                1,
                getResources().getString(R.string.menu_it_sub),
                0,
                "http://www.napwr.pl/rss/czas/40320/kategorie/informatyka,programowanie,grykomputerowe,marketinginternetowy,grafikakomputerowa,technologiemobilne,android,linux,windows,apple/organizacje/asi,kn-temomuko,knsi,google-student-ambassador,osd,polishleaguegaming,knstit,pad-party/?type=mobile"));
        listItems.add(new ListItemObject(2, getResources().getString(R.string.menu_kultura), 0, getResources().getString(R.string.cover_kultura)));
        listItems
            .add(new ListItemObject(
                1,
                getResources().getString(R.string.menu_kultura_sub),
                0,
                "http://www.napwr.pl/rss/czas/40320/kategorie/kultura,kino,teatr,sztuka/organizacje/dkf-politechnika,miesiecznik-zak,strefa-kultury-studenckiej/?type=mobile"));
        listItems.add(new ListItemObject(2, getResources().getString(R.string.menu_sport), 0, getResources().getString(R.string.cover_sport)));
        listItems
            .add(new ListItemObject(
                1,
                getResources().getString(R.string.menu_sport_sub),
                0,
                "http://www.napwr.pl/rss/czas/40320/kategorie/flashmob,gry,grykomputerowe,narty,sport,zawodysportowe,bilard,kajakarstwo,koszykowka,pilkanozna,siatkowka,mecze/organizacje/studencki-klub-turystyczny-pwr,liga-pwr,pwr-racing-team,polishleaguegaming,pad-party,mkk-wrotka,kz-polwiatr,reprezentacja-pwr-koszykowka,ks-pwr/?type=mobile"));
        listItems.add(new ListItemObject(2, getResources().getString(R.string.menu_top10), 0, getResources().getString(R.string.cover_top10)));
        listItems.add(new ListItemObject(0, getResources().getString(R.string.menu_top10_sub), R.drawable.miniature_top10,
            "http://www.napwr.pl/json/topten"));
    }

    void addItemsOnLogIn() {
        finish();
        startActivity(new Intent(this, MenuActivity.class));
    }

    public Typeface getTypeFace() {
        return Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
    }

    private void setFont() {
        Typeface type = getTypeFace();

        btn_user.setTypeface(type);
        btn_today.setTypeface(type);
        btn_tomorrow.setTypeface(type);
        btn_it.setTypeface(type);
        btn_kultura.setTypeface(type);
        btn_sport.setTypeface(type);
        btn_top10.setTypeface(type);
        btn_refesh.setTypeface(type);
        btn_settings.setTypeface(type);
        btn_login.setTypeface(type);
        btn_about.setTypeface(type);
        btn_sponsor.setTypeface(type);
        btn_exit.setTypeface(type);
        btn_user_plan.setTypeface(type);
        btn_user_favourities.setTypeface(type);
        btn_user_facultity.setTypeface(type);
    }

    private void setMenu() {
        btn_user = (TextView) menu.findViewById(R.id.menu_user);
        btn_today = (TextView) menu.findViewById(R.id.menu_today);
        btn_tomorrow = (TextView) menu.findViewById(R.id.menu_tomorrow);
        btn_it = (TextView) menu.findViewById(R.id.menu_it);
        btn_kultura = (TextView) menu.findViewById(R.id.menu_kultura);
        btn_sport = (TextView) menu.findViewById(R.id.menu_sport);
        btn_top10 = (TextView) menu.findViewById(R.id.menu_top10);
        btn_refesh = (TextView) menu.findViewById(R.id.menu_refresh);
        btn_settings = (TextView) menu.findViewById(R.id.menu_settings);
        btn_login = (TextView) menu.findViewById(R.id.menu_login);
        btn_about = (TextView) menu.findViewById(R.id.menu_about);
        btn_sponsor = (TextView) menu.findViewById(R.id.menu_sponsor);
        btn_exit = (TextView) menu.findViewById(R.id.menu_exit);

        btn_user_plan = (TextView) menu.findViewById(R.id.menu_user_plan);
        btn_user_favourities = (TextView) menu.findViewById(R.id.menu_user_favourities);
        btn_user_facultity = (TextView) menu.findViewById(R.id.menu_user_facultity);

        if (ifLogedin()) {
            btn_user.setText(app.userName);
            btn_login.setText(getResources().getString(R.string.menu_logout));
        }

        btn_user.setOnClickListener(this);
        btn_today.setOnClickListener(this);
        btn_tomorrow.setOnClickListener(this);
        btn_it.setOnClickListener(this);
        btn_kultura.setOnClickListener(this);
        btn_sport.setOnClickListener(this);
        btn_top10.setOnClickListener(this);
        btn_refesh.setOnClickListener(this);
        btn_settings.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_sponsor.setOnClickListener(this);
        btn_exit.setOnClickListener(this);

        btn_user_plan.setOnClickListener(this);
        btn_user_favourities.setOnClickListener(this);
        btn_user_facultity.setOnClickListener(this);

        if (!ifLogedin()) {
            btn_user.setVisibility(View.GONE);
        }
        btn_user_plan.setVisibility(View.GONE);
        btn_user_favourities.setVisibility(View.GONE);
        btn_user_facultity.setVisibility(View.GONE);
        btn_refesh.setVisibility(View.GONE);
        btn_about.setVisibility(View.GONE);
        btn_sponsor.setVisibility(View.GONE);

        setFont();
    }

    public void showUserOptions() {
        if (userOptionsOpen) {
            btn_user_plan.setVisibility(View.GONE);
            btn_user_favourities.setVisibility(View.GONE);
            btn_user_facultity.setVisibility(View.GONE);
            userOptionsOpen = !userOptionsOpen;
        }
        else if (app.logedin) {
            btn_user_plan.setVisibility(View.VISIBLE);
            btn_user_favourities.setVisibility(View.GONE);
            btn_user_facultity.setVisibility(View.GONE);
            userOptionsOpen = !userOptionsOpen;
        }
    }

    private void markMenuOption(TextView toMark) {
        btn_user.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_today.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_tomorrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_it.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_kultura.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_sport.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_top10.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_refesh.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_settings.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_login.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_about.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_sponsor.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));
        btn_exit.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray));

        btn_user_plan.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray_sub));
        btn_user_favourities.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray_sub));
        btn_user_facultity.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_button_gray_sub));

        toMark.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_left_gradient_selected_green));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_user:
                showUserOptions();
                markMenuOption(btn_user);
                break;
            case R.id.menu_user_plan:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_user_plan));
                markMenuOption(btn_user_plan);
                break;
            case R.id.menu_user_favourities:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_user_favourities));
                markMenuOption(btn_user_favourities);
                break;
            case R.id.menu_user_facultity:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_user_facultity));
                markMenuOption(btn_user_facultity);
                break;
            case R.id.menu_today:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_today));
                markMenuOption(btn_today);
                break;
            case R.id.menu_tomorrow:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_tomorrow));
                markMenuOption(btn_tomorrow);
                break;
            case R.id.menu_it:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_it));
                markMenuOption(btn_it);
                break;
            case R.id.menu_kultura:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_kultura));
                markMenuOption(btn_kultura);
                break;
            case R.id.menu_sport:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_sport));
                markMenuOption(btn_sport);
                break;
            case R.id.menu_top10:
                menuSlider.close();
                setItem(getResources().getString(R.string.menu_top10));
                markMenuOption(btn_top10);
                break;
            case R.id.menu_refresh:
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_login:
                if (ifLogedin()) {
                    logoff();
                    btn_login.setText(getResources().getString(R.string.menu_login));
                    addItemsOnLogIn();
                }
                else {
                    if (isLoginAvailable()) {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                }
                break;
            case R.id.menu_about:
                break;
            case R.id.menu_sponsor:
                break;
            case R.id.menu_exit:
                closeApplication();
                break;
        }
    }

    private void setItem(String name) {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i)._title.equals(name)) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    /**
     * Helper for examples with a HSV that should be scrolled by a menu View's
     * width.
     */
    public static class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;

        View menu;

        /**
         * Menu must NOT be out/shown to start with.
         */
        boolean menuOut = false;

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
            super();
            this.scrollView = scrollView;
            this.menu = menu;
        }

        @Override
        public void onClick(View v) {
            int menuWidth = menu.getMeasuredWidth();

            // Ensure menu is visible
            menu.setVisibility(View.VISIBLE);

            if (!menuOut) {
                // Scroll to 0 to reveal menu
                int left = 0;
                scrollView.smoothScrollTo(left, 0);
            }
            else {
                // Scroll to menuWidth so menu isn't on screen.
                int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
            }
            menuOut = !menuOut;
        }

        public void click() {
            int menuWidth = menu.getMeasuredWidth();

            // Ensure menu is visible
            menu.setVisibility(View.VISIBLE);

            if (!menuOut) {
                // Scroll to 0 to reveal menu
                int left = 0;
                scrollView.smoothScrollTo(left, 0);
            }
            else {
                // Scroll to menuWidth so menu isn't on screen.
                int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
            }
            menuOut = !menuOut;
        }

        public boolean close() {
            if (menuOut) {
                int menuWidth = menu.getMeasuredWidth();
                // Scroll to 0 to reveal menu
                int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
                menuOut = false;
                return true;
            }
            return false;
        }

        public boolean isOpened() {
            return menuOut;
        }
    }

    /**
     * Helper that remembers the width of the 'slide' button, so that the
     * 'slide' button remains in view, even when the menu is showing.
     */
    static class SizeCallbackForMenu implements pl.wroc.pwr.na.tools.MyHorizontalScrollView.SizeCallback {
        int btnWidth;

        View menu;

        int screenSize;

        public SizeCallbackForMenu(View menu, int screenSize) {
            super();
            this.menu = menu;
            this.screenSize = screenSize;
        }

        @Override
        public void onGlobalLayout() {
            btnWidth = screenSize - menu.getMeasuredWidth();
        }

        @Override
        public void getViewSize(int idx, int w, int h, int[] dims) {
            dims[0] = w;
            dims[1] = h;
            final int menuIdx = 0;
            if (idx == menuIdx) {
                dims[0] = w - btnWidth;
            }
        }
    }

}
