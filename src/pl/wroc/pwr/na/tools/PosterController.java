package pl.wroc.pwr.na.tools;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import pl.wroc.pwr.na.activities.MenuActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class PosterController implements Serializable {
	private static final long serialVersionUID = -8539816194055753857L;
	BitmapDataObject bdo_poster;

	public Bitmap writePoster(String name, Context context) {
		String URL = "" + name;
		Bitmap imagePoster = null;

		try {
			imagePoster = (Bitmap) new RequestTaskBitmap().execute(URL).get();
			
			imagePoster = fitToX(imagePoster, -1);

			Log.d("ZAPISYWANIE OBRAZU", imagePoster.toString());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			imagePoster.compress(Bitmap.CompressFormat.PNG, 100, stream);
			BitmapDataObject bdo = new BitmapDataObject();
			bdo.imageByteArray = stream.toByteArray();

			UseInternalStorage uis = new UseInternalStorage(context);
			uis.writeObject(bdo, name);

			bdo_poster = bdo;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			return null;
		}

		return imagePoster;
	}

	public Bitmap writePoster(Bitmap imagePoster, String name, Context context, int width, int orientation) {
		
		if(orientation == 1) {
			imagePoster = fitToX(imagePoster, width);
		} else {
			imagePoster = fitToY(imagePoster, width);
		}

		Log.d("ZAPISYWANIE OBRAZU", imagePoster.toString());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imagePoster.compress(Bitmap.CompressFormat.PNG, 100, stream);
		BitmapDataObject bdo = new BitmapDataObject();
		bdo.imageByteArray = stream.toByteArray();

		UseInternalStorage uis = new UseInternalStorage(context);
		uis.writeObject(bdo, name);

		bdo_poster = bdo;

		return imagePoster;
	}

	public Bitmap readPoster(String name, Context context) {
		UseInternalStorage uis = new UseInternalStorage(context);
		BitmapDataObject bdo = (BitmapDataObject) uis.readObject(name);

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

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// CREATE A MATRIX FOR THE MANIPULATION

		Matrix matrix = new Matrix();

		// RESIZE THE BIT MAP

		matrix.postScale(scaleWidth, scaleHeight);

		// RECREATE THE NEW BITMAP

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;
	}
	
	public Bitmap fitToX(Bitmap bm, int widthX){
		int width = bm.getWidth();
		int height = bm.getHeight();
		
		int newWidth = widthX;
		if(widthX <= 0){
			newWidth = ((MenuActivity) (MenuActivity.activityMain)).getWidthOfScreen();
		}
		int newHeight = (int) (height * ((double)newWidth/(double)width));
		
		return getResizedBitmap(bm, newHeight, newWidth);
	}
	
	public Bitmap fitToY(Bitmap bm, int heightX){
		int width = bm.getWidth();
		int height = bm.getHeight();
		
		int newHeight = heightX;
		if(heightX <= 0){
			newHeight = ((MenuActivity) (MenuActivity.activityMain)).getWidthOfScreen();
		}
		int newWidth = (int) (width * ((double)newHeight/(double)height));
		
		return getResizedBitmap(bm, newHeight, newWidth);
	}
}
