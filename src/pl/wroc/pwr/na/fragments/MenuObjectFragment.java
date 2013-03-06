package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.LoginActivity;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MenuObjectFragment extends Fragment {

	Button top10;
	Button today;
	Button tomorrow;
	Button calendar;
	Button favourites;
	Button login;
	TextView loginText;

	private static MenuObjectFragment singleInstance = null;

	public static MenuObjectFragment getInstance() {
		return singleInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		final View rootView = inflater.inflate(R.layout.fragment_menu,
				container, false);

		top10 = (Button) rootView.findViewById(R.id.main_button_top10);
		today = (Button) rootView.findViewById(R.id.main_button_today);
		tomorrow = (Button) rootView.findViewById(R.id.main_button_tomorrow);
		calendar = (Button) rootView.findViewById(R.id.main_button_calendar);
		favourites = (Button) rootView
				.findViewById(R.id.main_button_favourites);
		login = (Button) rootView.findViewById(R.id.main_button_login);
		loginText = (TextView) rootView.findViewById(R.id.main_textView_login);

		top10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).mViewPager
						.setCurrentItem(1);
			}
		});
		today.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).mViewPager
						.setCurrentItem(2);
			}
		});
		tomorrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MenuActivity) (MenuActivity.activityMain)).mViewPager
						.setCurrentItem(3);
			}
		});
		calendar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((MenuActivity) (MenuActivity.activityMain)).ifLogedin()) {
					((MenuActivity) (MenuActivity.activityMain)).mViewPager
							.setCurrentItem(4);
				} else {
					if(((MenuActivity) (MenuActivity.activityMain)).isLoginAvailable()){
						startActivity(new Intent(rootView.getContext(),
								LoginActivity.class));}
				}
			}
		});
		favourites.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((MenuActivity) (MenuActivity.activityMain)).ifLogedin()) {
					((MenuActivity) (MenuActivity.activityMain)).mViewPager
							.setCurrentItem(5);
				} else {
					if(((MenuActivity) (MenuActivity.activityMain)).isLoginAvailable()){
						startActivity(new Intent(rootView.getContext(),
								LoginActivity.class));}
				}

			}
		});
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((MenuActivity) (MenuActivity.activityMain)).ifLogedin()) {
					((MenuActivity) (MenuActivity.activityMain)).logoff();
					
					setLogedInLabel(false);
				} else {
					if(((MenuActivity) (MenuActivity.activityMain)).isLoginAvailable()){
					startActivity(new Intent(rootView.getContext(),
							LoginActivity.class));}
				}
			}
		});
		
		if(((MenuActivity) (MenuActivity.activityMain)).ifLogedin()){
			setLogedInLabel(true);
		}

		singleInstance = this;

		return rootView;
	}

	public void setLogedInLabel(boolean logged) {
		if (logged) {
			loginText.setText(R.string.label_logout);
		} else {
			loginText.setText(R.string.label_login2);
		}
	}
}
