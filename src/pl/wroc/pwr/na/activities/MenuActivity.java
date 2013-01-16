package pl.wroc.pwr.na.activities;

import java.util.ArrayList;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.MenuCollectionPagerAdapter;
import pl.wroc.pwr.na.objects.EventObject;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends FragmentActivity {

	public ArrayList<EventObject> current = new ArrayList<EventObject>();
	public ArrayList<EventObject> top10;
	public ArrayList<EventObject> dzisiaj;
	public ArrayList<EventObject> jutro;
	public ArrayList<EventObject> kalendarz;

	MenuCollectionPagerAdapter mCollectionPagerAdapter;
	public ViewPager mViewPager;
	Button login;
	TextView loginText;

	public static MenuActivity activityMain;	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		mCollectionPagerAdapter = new MenuCollectionPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager_menu);
		mViewPager.setAdapter(mCollectionPagerAdapter);

		activityMain = this;

		top10 = ((NAPWrApplication) getApplication()).top10;
		dzisiaj = ((NAPWrApplication) getApplication()).dzisiaj;
		jutro = ((NAPWrApplication) getApplication()).jutro;
		kalendarz = ((NAPWrApplication) getApplication()).kalendarz;
	}

}
