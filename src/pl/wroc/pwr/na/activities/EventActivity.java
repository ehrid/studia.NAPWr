package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.EventCollectionPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

public class EventActivity extends FragmentActivity {
	public static final String START_ITEM = "list_title";
	
    EventCollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    
    public static EventActivity activityMain;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mCollectionPagerAdapter = new EventCollectionPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        
        mViewPager.setCurrentItem(getIntent().getExtras().getInt("START_ITEM"));
        
        activityMain = this;
		
		
	}
    
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			finish();
			((MenuActivity) (MenuActivity.activityMain)).menuSlider.close();
			((MenuActivity) (MenuActivity.activityMain)).menuSlider.click();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	};
	


}
