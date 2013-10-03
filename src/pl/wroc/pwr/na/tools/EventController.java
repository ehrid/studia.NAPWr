package pl.wroc.pwr.na.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.objects.AddressObject;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.OrganizationObject;
import pl.wroc.pwr.na.objects.PlanObject;

public class EventController {

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
			if(adresWydarzenia.equals("")){
				adresWydarzenia = (String) event.getJSONObject("adres").get(
						"adresMiasto");
			}

			address = new AddressObject(adresWydarzenia);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new EventObject(
				wydarzenieTytul,
				wydarzenieId,
				wydarzenieTresc,
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

	private PlanObject planParser(JSONObject event) {
		String time = "";
		String title = "";
		String place = "";
		String lecturer = "";
		String type = "";
		int week = 0;
		int day = 0;
		int startHour = 0;

		try {
			startHour = event.getInt("start_hour");

			time = event.getInt("start_hour") + ":";

			if (event.getInt("start_min") < 10)
				time += "0";

			time += event.getInt("start_min") + " - "
					+ event.getInt("end_hour") + ":";

			if (event.getInt("end_min") < 10)
				time += "0";

			time += event.getInt("end_min");

			title = event.getString("course_name");
			place = event.getString("building") + " / "
					+ event.getString("room");
			lecturer = event.getString("lecturer");
			type = event.getString("course_type");

			week = event.getInt("week");
			day = event.getInt("week_day");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new PlanObject(time, title, place, lecturer, type, day, week,
				startHour);
	}

	private ArrayList<PlanObject> sortPlan(ArrayList<PlanObject> toSort) {
		ArrayList<PlanObject> sorted = new ArrayList<PlanObject>();

		for (PlanObject po : toSort) {
			if (sorted.size() == 0) {
				sorted.add(po);
			} else {
				boolean added = false;
				for (int i = 0; i < sorted.size(); i++) {
					if (po.startHour < sorted.get(i).startHour && !added) {
						sorted.add(i, po);
						added = true;
					}
				}
				if (!added) {
					sorted.add(po);
				}
			}
		}

		return sorted;
	}

	private String getSeparatorName(int day, int week, int dayIncremented) {
		String separatorName = "";

		switch (day) {
		case 0:
			separatorName += "Niedziela, ";
			break;
		case 1:
			separatorName += "Poniedziałek, ";
			break;
		case 2:
			separatorName += "Wtorek, ";
			break;
		case 3:
			separatorName += "Środa, ";
			break;
		case 4:
			separatorName += "Czwartek, ";
			break;
		case 5:
			separatorName += "Piątek, ";
			break;
		case 6:
			separatorName += "Sobota, ";
			break;
		}

		switch (week) {
		case 0:
			separatorName += "TP, ";
			break;
		case 1:
			separatorName += "TN, ";
			break;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, dayIncremented); // number of days to add
		separatorName += sdf.format(c.getTime()); // dt is now the new date

		return separatorName;
	}

	private int vaildDaysInPlanDay(ArrayList<PlanObject> planDay, int week) {
		int vaildDays = 0;
		for (PlanObject po : planDay) {
			if (po.week == 0 || po.week == week || po.week % 2 == week) {
				vaildDays++;
			}
		}
		return vaildDays;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PlanObject> getPlan(JSONObject completeObject) {
		JSONObject event;
		ArrayList<PlanObject> planList = new ArrayList<PlanObject>();

		JSONArray completeJSONArr;
		try {
			completeJSONArr = completeObject.getJSONObject("schedule")
					.getJSONArray("entries");

			for (int i = 0; i < completeJSONArr.length(); i++) {
				event = completeJSONArr.getJSONObject(i);
				planList.add(planParser(event));
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		if (planList.size() == 0) {
			return null;
		}

		// inicjalizowanie
		Object[] dayOfWeek = new Object[7];
		dayOfWeek[0] = new ArrayList<PlanObject>();
		dayOfWeek[1] = new ArrayList<PlanObject>();
		dayOfWeek[2] = new ArrayList<PlanObject>();
		dayOfWeek[3] = new ArrayList<PlanObject>();
		dayOfWeek[4] = new ArrayList<PlanObject>();
		dayOfWeek[5] = new ArrayList<PlanObject>();
		dayOfWeek[6] = new ArrayList<PlanObject>();

		// dzielenie do tablic z dniami
		for (PlanObject po : planList) {
			switch (po.day) {
			case 0:
				((ArrayList<PlanObject>) dayOfWeek[1]).add(po);
				break;
			case 1:
				((ArrayList<PlanObject>) dayOfWeek[2]).add(po);
				break;
			case 2:
				((ArrayList<PlanObject>) dayOfWeek[3]).add(po);
				break;
			case 3:
				((ArrayList<PlanObject>) dayOfWeek[4]).add(po);
				break;
			case 4:
				((ArrayList<PlanObject>) dayOfWeek[5]).add(po);
				break;
			case 5:
				((ArrayList<PlanObject>) dayOfWeek[6]).add(po);
				break;
			case 6:
				((ArrayList<PlanObject>) dayOfWeek[0]).add(po);
				break;
			}
		}

		dayOfWeek[0] = sortPlan((ArrayList<PlanObject>) dayOfWeek[0]);
		dayOfWeek[1] = sortPlan((ArrayList<PlanObject>) dayOfWeek[1]);
		dayOfWeek[2] = sortPlan((ArrayList<PlanObject>) dayOfWeek[2]);
		dayOfWeek[3] = sortPlan((ArrayList<PlanObject>) dayOfWeek[3]);
		dayOfWeek[4] = sortPlan((ArrayList<PlanObject>) dayOfWeek[4]);
		dayOfWeek[5] = sortPlan((ArrayList<PlanObject>) dayOfWeek[5]);
		dayOfWeek[6] = sortPlan((ArrayList<PlanObject>) dayOfWeek[6]);

		// Tworzenia aktualnego planu
		ArrayList<PlanObject> plan = new ArrayList<PlanObject>();

		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int week = calendar.get(Calendar.WEEK_OF_YEAR) % 2;
		int dayIncremented = 1;

		// w week 0 - TP, 1 - TN
		// w planie 0 - T, 1 - TN, 2 - TP
		// w day 0 - niedziela, 1 - poniedzialek, ... , 6 - sobota

		day--;
		dayIncremented--;
		if (day < 0) {
			day = 6;
			week = Math.abs(week - 1);

		}
		for (int i = 0; i < 7; i++) {
			//Log.d("Day", dayIncremented + "");
			// do {
			if (day > 6) {
				day = 0;
				week = (week + 1) % 2;
			}

			Log.d("plan", day + " " + dayIncremented);
			if (vaildDaysInPlanDay(((ArrayList<PlanObject>) dayOfWeek[day]),
					week) > 0) {
				plan.add(new PlanObject("separator", getSeparatorName(day,
						week, dayIncremented)));
				for (PlanObject po : ((ArrayList<PlanObject>) dayOfWeek[day])) {
					if (po.week == 0 || po.week == week || po.week % 2 == week) {
						plan.add(po);
					}
				}
			}
			day++;
			dayIncremented++;
			// } while (vaildDaysInPlanDay(((ArrayList<PlanObject>)
			// dayOfWeek[day-1]), week) == 0);
		}

		return plan;
	}

	public void addUlubione(NAPWrApplication app) {
		JSONArray completeJSONArr = null;
		try {
			if (app.logedin) {
				completeJSONArr = new JSONArray(
						(String) new RequestTaskString().execute(
								"http://www.napwr.pl/mobile/wydarzenia/ulubione/"
										+ app.userId).get());

				app.eventList.put(app.getResources().getString(
						R.string.menu_user_favourities), getEventsUlubione(completeJSONArr));
			} else {
				app.eventList.put(app.getResources().getString(
						R.string.menu_user_favourities), new ArrayList<EventObject>());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void addDzisiaj(NAPWrApplication app) {
		JSONArray completeJSONArr = null;
		try {
			completeJSONArr = new JSONArray((String) new RequestTaskString()
					.execute("http://www.napwr.pl/mobile/wydarzenia/dzis")
					.get());

			app.eventList.put(app.getResources().getString(
					R.string.menu_today_sub), getEvents(completeJSONArr));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void addJutro(NAPWrApplication app) {
		JSONArray completeJSONArr = null;
		try {
			completeJSONArr = new JSONArray((String) new RequestTaskString()
					.execute("http://www.napwr.pl/mobile/wydarzenia/jutro")
					.get());

			app.eventList.put(app.getResources().getString(
					R.string.menu_tomorrow_sub), getEvents(completeJSONArr));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void addKalendarz(NAPWrApplication app) {
		JSONObject completeObject = null;
		try {
			if (app.logedin) {
				completeObject = new JSONObject(
						(String) new RequestTaskString()
								.execute(
										"http://www.napwr.pl/mobile/plan/"
												+ app.userId).get());

				app.kalendarz = getPlan(completeObject);
			} else {
				app.kalendarz = new ArrayList<PlanObject>();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void addTop(NAPWrApplication app) {
		JSONArray completeJSONArr = null;
		try {
			completeJSONArr = new JSONArray((String) new RequestTaskString()
					.execute("http://www.napwr.pl/json/topten").get());

			app.eventList.put(app.getResources().getString(
					R.string.menu_top10_sub), getEvents(completeJSONArr));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
