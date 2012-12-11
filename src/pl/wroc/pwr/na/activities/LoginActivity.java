package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {

	private static LoginActivity singleInstance = null;

	private EditText password;
	private String userPassword;
	private EditText login;

	public static LoginActivity getInstance() {
		return singleInstance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		singleInstance = this;

		Button loginButton = (Button) findViewById(R.id.login_button_login);
		login = (EditText) findViewById(R.id.login_editText_login);
		password = (EditText) findViewById(R.id.login_editText_password);

		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button_login:
			getUser(login.getText().toString());
			if(password.getText().toString().equals(userPassword)){
				((NAPWrActivity)((NAPWrActivity.getInstance()))).setLOOGED_IN(true);
				((NAPWrActivity)((NAPWrActivity.getInstance()))).setLogedInLabel(true);
				finish();
			} else {
				//TODO notyfikacja po błedzie
			}
			break;
		}

	}
	
	private void getUser(String loginToCheck){
		//TODO pobieranie danych użytkownika
		if(loginToCheck.equals("zygol")){
			userPassword = "conadata";
		}
	}
}