package pl.wroc.pwr.na;

import java.util.ArrayList;
import java.util.HashMap;

import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.PlanObject;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class NAPWrApplication extends Application {
	public boolean logedin = false;
	public int userId = 0;
	public String userName = "";

	String PREFS = "MyPrefs";
	SharedPreferences mPrefs;

	public boolean firstLoad = false;

	public HashMap<String, ArrayList<EventObject>> eventList;
	public HashMap<String, Long> eventListUpdateTime;
	public ArrayList<PlanObject> kalendarz;

	@Override
	public void onCreate() {
		super.onCreate();

		mPrefs = getSharedPreferences(PREFS, 0);

		boolean rememberMe = mPrefs.getBoolean("rememberMe", false);

		if (rememberMe == true) {
			// get previously stored login details
			logedin = true;
			userId = mPrefs.getInt("login", 0);
			userName = mPrefs.getString("name", "");
		}

		if (getSettingsOdswierzanie() == 0
				|| (eventList = getEventList()) != null) {
			eventListUpdateTime = new HashMap<String, Long>();
			eventList = new HashMap<String, ArrayList<EventObject>>();
		} else {
			eventListUpdateTime = getEventListTimeUpdate();
		}
	}

	/*
	 * ZAPISYWANIE USTAWIEŃ
	 */

	public boolean getFirstLoad() {
		return mPrefs.getBoolean("firstLoad", false);
	}

	public void makeFirstLoad() {
		Editor e = mPrefs.edit();
		e.putBoolean("firstLoad", true);
		e.commit();
	}

	public void rememberUser(int id, String name) {
		saveLoginDetails(id, name);
		logedin = true;
		userId = id;
		userName = name;
	}

	public void forgetUser() {
		removeLoginDetails();
		logedin = false;
		userId = 0;
		userName = "";
	}

	private void saveLoginDetails(int id, String name) {
		Editor e = mPrefs.edit();
		e.putBoolean("rememberMe", true);
		e.putInt("login", id);
		e.putString("name", name);
		e.commit();
	}

	private void removeLoginDetails() {
		Editor e = mPrefs.edit();
		e.putBoolean("rememberMe", false);
		e.remove("login");
		e.remove("name");
		e.commit();
	}

	public void saveSettings(String plan, int wydzial, int zamknij,
			boolean powiadomienia, int odswierzanie) {
		Editor e = mPrefs.edit();
		e.putInt("wydzial", wydzial);
		e.putInt("zamknij", zamknij);
		Log.d("powiadomienia", powiadomienia + "");
		e.putBoolean("powiadomienia", powiadomienia);
		e.putInt("odswierzanie", odswierzanie);
		e.putString("plan", plan);
		e.commit();
	}

	public String getSettingsPlan() {
		return mPrefs.getString("plan", "http://plan-pwr.pl/[xXxXxXxXx]");
	}

	public int getSettingsWydzial() {
		return mPrefs.getInt("wydzial", 0);
	}
	
	public void setWydzial(int wydzial) {
		Editor e = mPrefs.edit();
		e.putInt("wydzial", wydzial-1);
		e.commit();
	}

	public int getSettingsZamknij() {
		return mPrefs.getInt("zamknij", 1);
	}

	public int getSettingsOdswierzanie() {
		return mPrefs.getInt("odswierzanie", 0);
	}

	public boolean getSettingsPowiadomienia() {
		Log.d("powiadomienia", mPrefs.getBoolean("powiadomienia", true) + "");
		return mPrefs.getBoolean("powiadomienia", true);
	}

	/*
	 * ZAPISYWANIE WYDARZEŃ
	 */
	public void savePlanObject(ArrayList<PlanObject> plan) {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		uis.writeObject(plan, "PlanZajecNAPWr");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PlanObject> getPlanObject() {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		return (ArrayList<PlanObject>) uis.readObject("PlanZajecNAPWr");
	}

	public void removePlanObject() {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		uis.writeObject(null, "PlanZajecNAPWr");
	}

	public void saveEventList() {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		uis.writeObject(eventList, "WydarzeniaNAPWr");
		uis.writeObject(eventListUpdateTime, "WydarzeniaCzasPobraniaZNAPWr");
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, ArrayList<EventObject>> getEventList() {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		return (HashMap<String, ArrayList<EventObject>>) uis
				.readObject("WydarzeniaNAPWr");
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Long> getEventListTimeUpdate() {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		return (HashMap<String, Long>) uis
				.readObject("WydarzeniaCzasPobraniaZNAPWr");
	}

	public void removeEventList() {
		UseInternalStorage uis = new UseInternalStorage(getApplicationContext());
		uis.writeObject(null, "WydarzeniaNAPWr");
		uis.writeObject(null, "WydarzeniaCzasPobraniaZNAPWr");
	}
}
