package pl.wroc.pwr.na;

import java.util.ArrayList;

import pl.wroc.pwr.na.objects.EventObject;
import android.app.Application;

public class NAPWrApplication extends Application {

	public ArrayList<EventObject> top10 = new ArrayList<EventObject>();
	public ArrayList<EventObject> dzisiaj = new ArrayList<EventObject>();
	public ArrayList<EventObject> jutro = new ArrayList<EventObject>();
	public ArrayList<EventObject> kalendarz = new ArrayList<EventObject>();
	public ArrayList<EventObject> ulubione = new ArrayList<EventObject>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

}
