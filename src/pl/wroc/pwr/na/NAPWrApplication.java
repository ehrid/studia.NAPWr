package pl.wroc.pwr.na;

import java.util.ArrayList;

import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.PlanObject;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class NAPWrApplication extends Application {
	public boolean logedin = false;
	public int userId = 0;
	
	String PREFS = "MyPrefs";
	SharedPreferences mPrefs;
	
	public boolean firstLoad = false;

	public ArrayList<EventObject> top10 = new ArrayList<EventObject>();
	public ArrayList<EventObject> dzisiaj = new ArrayList<EventObject>();
	public ArrayList<EventObject> jutro = new ArrayList<EventObject>();
	public ArrayList<PlanObject> kalendarz = new ArrayList<PlanObject>();
	public ArrayList<EventObject> ulubione = new ArrayList<EventObject>();

	@Override
	public void onCreate() {
		super.onCreate();
		mPrefs = getSharedPreferences(PREFS, 0);
		
		boolean rememberMe = mPrefs.getBoolean("rememberMe", false);

	    if(rememberMe == true){
	        //get previously stored login details
	        int login = mPrefs.getInt("login", 0);
	        
	        logedin = true;
	        userId = login;

	    }
	}

	public void rememberUser(int id) {
		saveLoginDetails(id);
		logedin = true;
		userId = id;
	}

	public void forgetUser() {
		removeLoginDetails();
		logedin = false;
		userId = 0;
	}
	
	private void saveLoginDetails(int id){
	    Editor e = mPrefs.edit();
	    e.putBoolean("rememberMe", true);
	    e.putInt("login", id);
	    e.commit();
	}

	private void removeLoginDetails(){
	    Editor e = mPrefs.edit();
	    e.putBoolean("rememberMe", false);
	    e.remove("login");
	    e.commit();
	}
}
