package pl.wroc.pwr.na.objects;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import pl.wroc.pwr.na.tools.RequestTaskBitmap;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class EventObject implements Serializable {
	private static final long serialVersionUID = 8758719650084506599L;

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

	BitmapDataObject bdo_poster;

	public EventObject(CharSequence name, int id, CharSequence content,
			CharSequence poster, CharSequence bigPoster, int likeSum,
			String startDate, String endDate, OrganizationObject organization,
			AddressObject address) {
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
	}

	public Bitmap getImagePoster(Context context) {

		if (bdo_poster != null) {
			return BitmapFactory.decodeByteArray(bdo_poster.imageByteArray, 0,
					bdo_poster.imageByteArray.length);
		} else {
			Bitmap poster = readPoster(context);
			if (poster == null) {
				poster = writePoster(context);
			}
			return poster;
		}
	}

	public void setImagePoster(Context context) {
		if (bdo_poster == null) {
			if (readPoster(context) == null) {
				writePoster(context);
			}
		}
	}

	private Bitmap writePoster(Context context) {
		try {
			Bitmap imagePoster = (Bitmap) new RequestTaskBitmap().execute(
					(String) bigPoster).get();

			Log.d("ZAPISYWANIE OBRAZU", imagePoster.toString());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			imagePoster.compress(Bitmap.CompressFormat.PNG, 100, stream);
			BitmapDataObject bdo = new BitmapDataObject();
			bdo.imageByteArray = stream.toByteArray();

			UseInternalStorage uis = new UseInternalStorage(context);
			uis.writeObject(bdo, "plakat_" + id);

			bdo_poster = bdo;
			return BitmapFactory.decodeByteArray(bdo.imageByteArray, 0,
					bdo.imageByteArray.length);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap readPoster(Context context) {
		UseInternalStorage uis = new UseInternalStorage(context);
		BitmapDataObject bdo = (BitmapDataObject) uis
				.readObject("plakat_" + id);

		if (bdo == null) {
			return null;
		} else {
			bdo_poster = bdo;
			return BitmapFactory.decodeByteArray(bdo.imageByteArray, 0,
					bdo.imageByteArray.length);
		}

	}

	protected class BitmapDataObject implements Serializable {
		private static final long serialVersionUID = 6022430163107957510L;

		public byte[] imageByteArray;
	}

}