package pl.wroc.pwr.na.objects;

import java.util.concurrent.ExecutionException;

import pl.wroc.pwr.na.tools.RequestTaskBitmap;
import android.graphics.Bitmap;

public class EventObject {
	public CharSequence name;
	public int id;
	public CharSequence content;
	public OrganizationObject organization;
	public AddressObject address;
	public CharSequence poster;
	public CharSequence bigPoster;
	public int likeSum;
	public boolean isLiked;
	public String startDate;
	public String endDate;
	
	public Bitmap imagePoster;


	public EventObject(CharSequence name, int id, CharSequence content,
			CharSequence poster, CharSequence bigPoster, int likeSum, String startDate, String endDate, OrganizationObject organization, AddressObject address) {
		super();
		this.name = name;
		this.id = id;
		this.content = content;
		this.poster = poster;
		this.bigPoster = bigPoster;
		this.likeSum = likeSum;
		this.startDate = startDate;
		this.endDate = endDate;
		this.organization = organization;
		this.address = address;
		
		
		try {
			imagePoster = (Bitmap) new RequestTaskBitmap().execute((String) bigPoster).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}