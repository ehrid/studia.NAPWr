package pl.wroc.pwr.na.adapters;

import pl.wroc.pwr.na.fragments.EventListObjectFragment;
import pl.wroc.pwr.na.fragments.MenuObjectFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MenuCollectionPagerAdapter extends FragmentStatePagerAdapter {
	public MenuCollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new EventListObjectFragment();
		String title = " ";

		switch (i) {
			case 0:
				return new MenuObjectFragment();
			case 1:
				title = "Top 10";
				break;
			case 2:
				title = "Dzisiaj";
				break;
			case 3:
				title = "Jutro";
				break;
			case 4:
				title = "Kalendarz";
				break;
			case 5:
				title = "Ulubione";
				break;
		}

		Bundle args = new Bundle();
		args.putString(EventListObjectFragment.LIST_TITLE, title);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public int getCount() {
		return 6;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}
}
