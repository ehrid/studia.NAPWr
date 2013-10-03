package pl.wroc.pwr.na.objects;

import java.io.Serializable;

public class PlanObject implements Serializable {
	private static final long serialVersionUID = 5650591533076865127L;
	
	public String _time;
	public String _title;
	public String _place;
	public String _lecturer;
	public String _type;
	public String _date;
	public int _day;
	public int _week;
	public int _startHour;

	public PlanObject(String type, String date) {
		super();
		_type = type;
		_date = date;
	}

	public PlanObject(String time, String title, String place, String lecturer,
			String type, int day, int week, int startHour) {
		super();
		_time = time;
		_title = title;
		_place = place;
		_lecturer = lecturer;
		_type = type;
		_day = day;
		_week = week;
		_startHour = startHour;
	}
}
