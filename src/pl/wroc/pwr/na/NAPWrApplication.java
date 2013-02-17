package pl.wroc.pwr.na;

import java.util.ArrayList;

import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.PlanObject;
import android.app.Application;

public class NAPWrApplication extends Application {
	public boolean logedin = false;
	public int userId = 0;

	public ArrayList<EventObject> top10 = new ArrayList<EventObject>();
	public ArrayList<EventObject> dzisiaj = new ArrayList<EventObject>();
	public ArrayList<EventObject> jutro = new ArrayList<EventObject>();
	public ArrayList<PlanObject> kalendarz = new ArrayList<PlanObject>();
	public ArrayList<EventObject> ulubione = new ArrayList<EventObject>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public void rememberUser(int id) {
		logedin = true;
		userId = id;
	}

	public void forgetUser() {
		logedin = false;
		userId = 0;
	}

	public void restoreUser(){
	}
}
