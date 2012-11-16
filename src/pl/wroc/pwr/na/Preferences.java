package pl.wroc.pwr.na;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {
	private static final String OPT_FULSCREEN = "fulscreen";
	private static final boolean OPT_FULSCREEN_DEF = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	/** Get the current value of the fulscreen option */
	public static boolean getFulscreen(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_FULSCREEN, OPT_FULSCREEN_DEF);
	}
}
