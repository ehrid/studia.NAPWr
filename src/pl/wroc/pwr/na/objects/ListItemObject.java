package pl.wroc.pwr.na.objects;

public class ListItemObject {
	/*
	 * 0 - LISTA WYDARZEŃ JSON
	 * 1 - LISTA WYDARZEŃ RSS
	 * 2 - POSTER
	 * 3 - PLAN ZAJĘĆ
	 */
	public int type;
	public String title;
	public int miniature;
	public String url;
	
	public ListItemObject(int type, String title, int miniature, String url) {
		super();
		this.type = type;
		this.title = title;
		this.miniature = miniature;
		this.url = url;
	}
	
}
