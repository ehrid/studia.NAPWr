package pl.wroc.pwr.na.adapters;

import java.util.ArrayList;

import pl.wroc.pwr.na.fragments.EventListObjectFragment;
import pl.wroc.pwr.na.fragments.EventListPosterFragment;
import pl.wroc.pwr.na.fragments.PlanObjectFragment;
import pl.wroc.pwr.na.objects.ListItemObject;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MenuCollectionPagerAdapter extends FragmentStatePagerAdapter {
	ArrayList<ListItemObject> listItems;
	
	public MenuCollectionPagerAdapter(FragmentManager fm,ArrayList<ListItemObject> listItems) {
		super(fm);
		this.listItems = listItems;
	}

	public void setListItems(ArrayList<ListItemObject> listItems) {
		this.listItems = listItems;
	}


	@Override
	public Fragment getItem(int i) {	
		Fragment fragment = null;
		Bundle args = new Bundle();
		
		ListItemObject obj = listItems.get(i);
		
		switch(obj._type){
		case 0:
			fragment = new EventListObjectFragment();
			args.putString(EventListObjectFragment.LIST_TITLE, obj._title);
			args.putInt(EventListObjectFragment.LIST_MINIATURE, obj._miniature);
			args.putInt(EventListObjectFragment.LIST_TYPE, obj._type);
			args.putString(EventListObjectFragment.LIST_URL, obj._url);
			break;
		case 1:
			fragment = new EventListObjectFragment();
			args.putString(EventListObjectFragment.LIST_TITLE, obj._title);
			args.putInt(EventListObjectFragment.LIST_MINIATURE, obj._miniature);
			args.putInt(EventListObjectFragment.LIST_TYPE, obj._type);
			args.putString(EventListObjectFragment.LIST_URL, obj._url);
			break;
		case 2:
			fragment = new EventListPosterFragment();
			args.putString(EventListPosterFragment.LIST_TITLE, obj._title);
			args.putString(EventListPosterFragment.LIST_URL, obj._url);
			break;
		case 3: 
			return new PlanObjectFragment();
		}
		
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}
}
