package pl.wroc.pwr.na.adapters;

import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.fragments.EventObjectFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class EventCollectionPagerAdapter extends FragmentStatePagerAdapter {
	public EventCollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new EventObjectFragment();
		Bundle args = new Bundle();
		args.putInt(EventObjectFragment.ARG_OBJECT, i);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return ((MenuActivity) (MenuActivity.activityMain)).current.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = (String) ((MenuActivity) (MenuActivity.activityMain)).current.get(position)._name;
		
		if(title.length() > 18){
			title = title.substring(0, 15);
			title += "...";
		}
		
		return title;
	}
}
