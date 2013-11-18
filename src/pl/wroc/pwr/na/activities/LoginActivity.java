package pl.wroc.pwr.na.activities;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.tools.RequestTaskString;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	private static LoginActivity singleInstance = null;
	UseInternalStorage uis;

	private EditText password;
	private EditText login;
	Button loginButton;

	public static LoginActivity getInstance() {
		return singleInstance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		singleInstance = this;

		uis = new UseInternalStorage(getApplicationContext());

		loginButton = (Button) findViewById(R.id.login_button_login);
		login = (EditText) findViewById(R.id.login_editText_login);
		password = (EditText) findViewById(R.id.login_editText_password);

		this.findViewById(android.R.id.title).setVisibility(View.GONE);

		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button_login:
			if (login()) {
				finish();
			}
			break;
		}

	}

	// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// API 11
	String startMyTask(String s) {
		try {
			// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			// return (String) new RequestTaskString().executeOnExecutor(
			// AsyncTask.THREAD_POOL_EXECUTOR, s).get();
			// else

			return new RequestTaskString().execute(s).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean login() {

		boolean ifLogin = true;
		if (password.getText().toString().equals("")) {
			password.setError(getResources().getString(
					R.string.login_password_error));
			ifLogin = false;

		}
		if (login.getText().toString().equals("")) {
			login.setError(getResources().getString(R.string.login_email_error));
			ifLogin = false;
		}
		if (!ifLogin) {
			return false;
		}
		try {
			String s = "http://www.napwr.pl/mobile/login/"
					+ login.getText().toString() + "/"
					+ password.getText().toString();
			s = startMyTask(s);
			Log.d("login", s);
			JSONArray completeJSONArr = new JSONArray("[" + s + "]");

			JSONObject event = completeJSONArr.getJSONObject(0);
			if (event.getBoolean("approved")) {

				String wydzial = event.getString("wydzial");
				if (wydzial.contains("W-")) {
					wydzial = wydzial.substring(2);
					((NAPWrApplication) getApplication()).setWydzial(Integer
							.parseInt(wydzial));
				}

				((NAPWrApplication) getApplication()).rememberUser(
						event.getInt("id"), event.getString("nik"));

				((MenuActivity) (MenuActivity.activityMain)).btn_login
						.setText(getResources().getString(R.string.menu_logout));
				((MenuActivity) (MenuActivity.activityMain)).addItemsOnLogIn();

				return true;
			} else if (event.getBoolean("login")) {
				password.setError(getResources().getString(
						R.string.login_password_error));
			} else {
				login.setError(getResources().getString(
						R.string.login_email_error));
				password.setError(getResources().getString(
						R.string.login_password_error));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
}