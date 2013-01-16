package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.fragments.MenuObjectFragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private static LoginActivity singleInstance = null;

	private EditText password;
	private String userPassword;
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

		loginButton = (Button) findViewById(R.id.login_button_login);
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
				((MenuObjectFragment)((MenuObjectFragment.getInstance()))).setLOOGED_IN(true);
				((MenuObjectFragment)((MenuObjectFragment.getInstance()))).setLogedInLabel(true);
				finish();
			} else {
				password.setError(getResources().getString(R.string.login_password_error));
			}
			break;
		}

	}
	
	private void getUser(String loginToCheck){
		//TODO pobieranie danych użytkownika
		if(loginToCheck.equals("zygol")){
			userPassword = "conadata";
		} else {
			login.setError(getResources().getString(R.string.login_email_error));
			
		}
	}
}