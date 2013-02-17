package pl.wroc.pwr.na.objects;

import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
			URL newurl = new URL((String) bigPoster);
			imagePoster = BitmapFactory.decodeStream(newurl
					.openConnection().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}