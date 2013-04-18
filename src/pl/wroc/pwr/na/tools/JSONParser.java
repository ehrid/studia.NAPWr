package pl.wroc.pwr.na.tools;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.AddressObject;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.OrganizationObject;

public class JSONParser {

	private EventObject parser(JSONObject event, int i) {
		int wydarzenieId = 0;
		String wydarzenieTytul = "";
		String wydarzenieTresc = "";
		int wydarzenieSumaLajkow = 0;
		int wydarzenieWartoscPriorytet = 0;
		int wydarzeniePrzeczytalo = 0;
		String wydarzenieDataPoczatek = "";
		String wydarzenieDataKoniec = "";
		String linkToSmallPoster = "";
		String linkToBigPoster = "";
		String nazwaOrganizacji = "";
		String adresWydarzenia = "";
		AddressObject address = new AddressObject("");
		OrganizationObject organizacja = new OrganizationObject("");

		try {
			wydarzenieId = event.getInt("wydarzenieId");
			wydarzenieTytul = event.getString("wydarzenieTytul");
			wydarzenieTresc = event.getString("wydarzenieTresc");
			linkToSmallPoster = event.getJSONObject("plakat").getString(
					"plakatMiniatura");

			linkToBigPoster = event.getJSONObject("plakat").getString(
					"plakatPlik");

			wydarzenieSumaLajkow = event.getInt("wydarzenieSumaLajkow");
			wydarzenieWartoscPriorytet = event
					.getInt("wydarzenieWartoscPriorytet");
			wydarzeniePrzeczytalo = event.getInt("wydarzeniePrzeczytalo");

			String s = (String) event.getJSONObject("wydarzenieDataPoczatek")
					.get("date");

			wydarzenieDataPoczatek = s.substring(0, s.length() - 3);

			s = (String) event.getJSONObject("wydarzenieDataKoniec")
					.get("date");
			wydarzenieDataKoniec = s.substring(0, s.length() - 3);

			nazwaOrganizacji = (String) event.getJSONObject("organizacja")
					.getJSONObject("uzytkownik")
					.get("uzytkownikNazwaWyswietlana");

			organizacja = new OrganizationObject(nazwaOrganizacji);

			adresWydarzenia = (String) event.getJSONObject("adres").get(
					"adresBudynek");
			if (adresWydarzenia.equals("")) {
				adresWydarzenia = (String) event.getJSONObject("adres").get(
						"adresMiasto");
			}

			address = new AddressObject(adresWydarzenia);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new EventObject(wydarzenieTytul, wydarzenieId, wydarzenieTresc,
				"http://www.napwr.pl/" + linkToSmallPoster,
				"http://www.napwr.pl/" + linkToBigPoster, wydarzenieSumaLajkow
						+ wydarzenieWartoscPriorytet + wydarzeniePrzeczytalo
						/ 4, wydarzenieDataPoczatek, wydarzenieDataKoniec,
				organizacja, address);
	}

	public ArrayList<EventObject> getEvents(JSONArray completeJSONArr) {
		JSONObject event;
		ArrayList<EventObject> eventList = new ArrayList<EventObject>();

		for (int i = 0; i < completeJSONArr.length(); i++) {
			try {
				event = completeJSONArr.getJSONObject(i);
				eventList.add(parser(event, i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return eventList;
	}

	public ArrayList<EventObject> getEventsUlubione(JSONArray completeJSONArr) {
		JSONObject event;
		ArrayList<EventObject> eventList = new ArrayList<EventObject>();

		for (int i = 0; i < completeJSONArr.length(); i++) {
			try {
				event = completeJSONArr.getJSONObject(i).getJSONObject(
						"wydarzenie");
				eventList.add(parser(event, i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return eventList;
	}

	private JSONArray getItemJSONArray(String URL) {
		try {
			return new JSONArray((String) new RequestTaskString().execute(URL)
					.get());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<EventObject> getEventsJSON(String URL) {
		
		JSONArray completeJSONArr = getItemJSONArray(URL);
		
		JSONObject event;
		ArrayList<EventObject> eventList = new ArrayList<EventObject>();

		for (int i = 0; i < completeJSONArr.length(); i++) {
			try {
				event = completeJSONArr.getJSONObject(i);
				eventList.add(parser(event, i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return eventList;
	}

	public void addUlubione(NAPWrApplication app) {
		JSONArray completeJSONArr = null;
		try {
			if (app.logedin) {
				completeJSONArr = new JSONArray(
						(String) new RequestTaskString().execute(
								"http://www.napwr.pl/mobile/wydarzenia/ulubione/"
										+ app.userId).get());
				app.eventList.put(
						app.getResources().getString(
								R.string.menu_user_favourities),
						getEventsUlubione(completeJSONArr));
			} else {
				app.eventList.put(
						app.getResources().getString(
								R.string.menu_user_favourities),
						new ArrayList<EventObject>());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
