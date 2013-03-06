package pl.wroc.pwr.na.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.util.Log;

public class UseInternalStorage {

	String TAG = "Media";
	Context rootActivity;

	public UseInternalStorage(Context rootActivity) {
		super();
		this.rootActivity = rootActivity;
	}

	public boolean writeObject(Object ob, String location) {
		try {
			FileOutputStream fos = rootActivity.openFileOutput(location,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(ob);

			oos.close();
			
			Log.d(TAG, "zaposano polik " +location);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Object readObject(String location) {
		try {
			Log.d("UseInternalStorage", location);
			FileInputStream fis = rootActivity.openFileInput(location);

			ObjectInputStream is = new ObjectInputStream(fis);

			Object ob = is.readObject();
			is.close();
			
			Log.d(TAG, "odczytano polik " +location);
			
			return ob;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

}
