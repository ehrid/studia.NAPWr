package pl.wroc.pwr.na.tools;

import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class RequestTaskBitmap extends AsyncTask<String, String, Bitmap> {

	@Override
	protected Bitmap doInBackground(String... uri) {
		Bitmap responseBitmap = null;
		try {
			URL url = new URL(uri[0]);

			responseBitmap = BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		// Do anything with response..
	}
}