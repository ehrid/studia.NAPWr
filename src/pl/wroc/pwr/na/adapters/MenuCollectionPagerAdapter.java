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
		
		switch(obj.type){
		case 0:
			fragment = new EventListObjectFragment();
			args.putString(EventListObjectFragment.LIST_TITLE, obj.title);
			args.putInt(EventListObjectFragment.LIST_MINIATURE, obj.miniature);
			break;
		case 1:
			break;
		case 2:
			fragment = new EventListPosterFragment();
			args.putString(EventListPosterFragment.LIST_TITLE, obj.title);
			break;
		case 3: 
			return new PlanObjectFragment();
		}
		
		fragment.setArguments(args);
		return fragment;

//		boolean isPoster = false;
//		switch (i) {
//		case 0:
//			// return new MenuObjectFragment();
//			title = "TOP 10";
//			isPoster = true;
//			break;
//		case 1:
//			title = "Top 10";
//			miniature = R.drawable.miniature_top10;
//			break;
//		case 2:
//			title = "DZISIAJ";
//			isPoster = true;
//			break;
//		case 3:
//			title = "Dzisiaj";
//			miniature = R.drawable.miniature_today;
//			break;
//		case 4:
//			title = "JUTRO";
//			isPoster = true;
//			break;
//		case 5:
//			title = "Jutro";
//			miniature = R.drawable.miniature_tommorow;
//			break;
//		case 6:
//			title = "PLAN ZAJĘĆ";
//			isPoster = true;
//			break;
//		case 7:
//			return new PlanObjectFragment();
//		case 8:
//			title = "ULUBIONE";
//			isPoster = true;
//			break;
//		case 9:
//			title = "Ulubione";
//			miniature = R.drawable.miniature_favourites;
//			break;
//		}
//
//		if (isPoster) {
//			fragment = new EventListPosterFragment();
//			args.putString(EventListPosterFragment.LIST_TITLE, title);
//		} else {
//			fragment = new EventListObjectFragment();
//			args.putString(EventListObjectFragment.LIST_TITLE, title);
//			args.putInt(EventListObjectFragment.LIST_MINIATURE, miniature);
//		}
//		fragment.setArguments(args);
//		
//		return fragment;
	}

	@Override
	public int getCount() {
		return listItems.size();
		//return ((MenuActivity) (MenuActivity.activityMain)).ifLogedin() ? 10 : 6;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}
}
