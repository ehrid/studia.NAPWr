package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.EventObject;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class EventActivity extends Activity implements OnClickListener {

	// Header
	Button back;
	Button share;

	// Event
	Button likeit;
	TextView title;
	TextView fromDate;
	TextView toDate;
	TextView address;
	TextView content;
	Button poster;

	EventObject event;
	
	//The "x" and "y" position of the "Show Button" on screen.
	Point p;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		back = (Button) findViewById(R.id.back);
		share = (Button) findViewById(R.id.share);

		title = (TextView) findViewById(R.id.event_title);
		likeit = (Button) findViewById(R.id.event_likeit);
		fromDate = (TextView) findViewById(R.id.event_fromDate);
		toDate = (TextView) findViewById(R.id.event_toDate);
		address = (TextView) findViewById(R.id.event_address);
		content = (TextView) findViewById(R.id.event_content);
		poster = (Button) findViewById(R.id.event_poster);

		event = ((EventListActivity)((EventListActivity.getInstance()))).event;

		if(event.name != null) title.setText(event.name.toString());
		if(event.startDate != null) fromDate.setText(event.startDate.toString());
		if(event.endDate != null) toDate.setText(event.endDate.toString());
		if(event.address != null) address.setText(event.address.toString());
		if(event.content != null) content.setText(event.content.toString());
		
		//Pawel
		Button btn_show = (Button) findViewById(R.id.event_poster);
		   btn_show.setOnClickListener(new OnClickListener() {
		     @Override
		     public void onClick(View arg0) {

		       //Open popup window
		       if (p != null)
		       showPopup(EventActivity.this, p);
		     }
		   });
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
	
	// Get the x and y position after the button is draw on screen
	// (It's important to note that we can't get the position in the onCreate(),
	// because at that stage most probably the view isn't drawn yet, so it will return (0, 0))
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

	   int[] location = new int[2];
	   Button button = (Button) findViewById(R.id.event_poster);

	   // Get the x, y location and store it in the location[] array
	   // location[0] = x, location[1] = y.
	   button.getLocationOnScreen(location);

	   //Initialize the Point with x, and y positions
	   p = new Point();
	   p.x = location[0];
	   p.y = location[1];
	}

	// The method that displays the popup.
	private void showPopup(final Activity context, Point p) {
	   int popupWidth = 200;
	   int popupHeight = 150;

	   // Inflate the popup_layout.xml
	   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_event_img);
	   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   View layout = layoutInflater.inflate(R.layout.item_popup_img, viewGroup);

	   // Creating the PopupWindow
	   final PopupWindow popup = new PopupWindow(context);
	   popup.setContentView(layout);
	   popup.setWidth(popupWidth);
	   popup.setHeight(popupHeight);
	   popup.setFocusable(true);

	   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
	   int OFFSET_X = 30;
	   int OFFSET_Y = 30;

	   // Clear the default translucent background
	   popup.setBackgroundDrawable(new BitmapDrawable());

	   // Displaying the popup at the specified location, + offsets.
	   popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

	   // Getting a reference to Close button, and close the popup when clicked.
	   Button close = (Button) layout.findViewById(R.id.close);
	   close.setOnClickListener(new OnClickListener() {

	     @Override
	     public void onClick(View v) {
	       popup.dismiss();
	     }
	   });
	}

}
