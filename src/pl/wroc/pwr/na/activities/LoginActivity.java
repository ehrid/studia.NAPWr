package pl.wroc.pwr.na.activities;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.wroc.pwr.na.NAPWrApplication;
import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.fragments.MenuObjectFragment;
import pl.wroc.pwr.na.tools.EventController;
import pl.wroc.pwr.na.tools.RequestTaskString;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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

	ProgressDialog pd;

	public boolean login() {
		pd = new ProgressDialog(this);
		pd.setMessage("Trwa logowanie");
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				pd.dismiss();
			}
		});
		pd.show();
		
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
			String s = (String) new RequestTaskString().execute(
					"http://www.napwr.pl/mobile/login/"
							+ login.getText().toString() + "/"
							+ password.getText().toString()).get();
			Log.d("login", s);
			JSONArray completeJSONArr = new JSONArray("[" + s + "]");

			JSONObject event = completeJSONArr.getJSONObject(0);

			((NAPWrApplication) getApplication()).rememberUser(event
					.getInt("id"));

			if (event.getBoolean("approved")) {
				((MenuObjectFragment) ((MenuObjectFragment.getInstance())))
						.setLogedInLabel(true);
				downloadUserData();
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void downloadUserData() {
		EventController ep = new EventController();

		NAPWrApplication app = (NAPWrApplication) getApplication();

		ep.addUlubione(app);
		uis.writeObject(app.ulubione, "ulubione");
		ep.addKalendarz(app);
		uis.writeObject(app.kalendarz, "kalendarz");

		((MenuActivity) (MenuActivity.activityMain)).ulubione = app.ulubione;
		((MenuActivity) (MenuActivity.activityMain)).kalendarz = app.kalendarz;
		((MenuActivity) (MenuActivity.activityMain)).mViewPager
				.refreshDrawableState();
		
		if (app.ulubione.size() > 0) {
			app.ulubione.get(0).setImagePoster(
					getApplicationContext());
		}
	}
}