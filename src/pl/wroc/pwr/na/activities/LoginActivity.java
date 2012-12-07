package pl.wroc.pwr.na.activities;

import pl.wroc.pwr.na.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// odszukujemy przyciski i podpinamy pod nie listenery
		View login = findViewById(R.id.login_button_login);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		// Wykonywanie akcji w przypadku nacisniecia ktoregos z przyciskow
		switch (v.getId()) {
		case R.id.login_button_login: // wcisniecie przycisku zaloguj
			// TODO logowanie
			break;
		}
	}
}