package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.EventCollectionPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;

public class EventActivity extends FragmentActivity {

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
        
        activityMain = this;
		
		
	}
	
//	//Pawel
//			Button btn_show = (Button) findViewById(R.id.event_poster);
//			   btn_show.setOnClickListener(new OnClickListener() {
//			     @Override
//			     public void onClick(View arg0) {
//
//			       //Open popup window
//			       if (p != null)
//			       showPopup(EventActivity.this, p);
//			     }
//			   });
	
//	// Get the x and y position after the button is draw on screen
//	// (It's important to note that we can't get the position in the onCreate(),
//	// because at that stage most probably the view isn't drawn yet, so it will return (0, 0))
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//
//	   int[] location = new int[2];
//	   Button button = (Button) findViewById(R.id.event_poster);
//
//	   // Get the x, y location and store it in the location[] array
//	   // location[0] = x, location[1] = y.
//	   button.getLocationOnScreen(location);
//
//	   //Initialize the Point with x, and y positions
//	   p = new Point();
//	   p.x = location[0];
//	   p.y = location[1];
//	}
//
//	// The method that displays the popup.
//	private void showPopup(final Activity context, Point p) {
//	   int popupWidth = 200;
//	   int popupHeight = 150;
//
//	   // Inflate the popup_layout.xml
//	   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_event_img);
//	   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	   View layout = layoutInflater.inflate(R.layout.item_popup_img, viewGroup);
//
//	   // Creating the PopupWindow
//	   final PopupWindow popup = new PopupWindow(context);
//	   popup.setContentView(layout);
//	   popup.setWidth(popupWidth);
//	   popup.setHeight(popupHeight);
//	   popup.setFocusable(true);
//
//	   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
//	   int OFFSET_X = 30;
//	   int OFFSET_Y = 30;
//
//	   // Clear the default translucent background
//	   popup.setBackgroundDrawable(new BitmapDrawable());
//
//	   // Displaying the popup at the specified location, + offsets.
//	   popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
//
//	   // Getting a reference to Close button, and close the popup when clicked.
//	   Button close = (Button) layout.findViewById(R.id.close);
//	   close.setOnClickListener(new OnClickListener() {
//
//	     @Override
//	     public void onClick(View v) {
//	       popup.dismiss();
//	     }
//	   });
//	}

}
