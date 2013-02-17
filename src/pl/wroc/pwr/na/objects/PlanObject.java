package pl.wroc.pwr.na.objects;

public class PlanObject {
	public String time;
	public String title;
	public String place;
	public String lecturer;
	public String type;
	public String date;
	public int day;
	public int week;
	public int startHour;

	public PlanObject(String type, String date) {
		super();
		this.type = type;
		this.date = date;
	}

	public PlanObject(String time, String title, String place, String lecturer,
			String type, int day, int week, int startHour) {
		super();
		this.time = time;
		this.title = title;
		this.place = place;
		this.lecturer = lecturer;
		this.type = type;
		this.day = day;
		this.week = week;
		this.startHour = startHour;
	}

	
}
