package pl.wroc.pwr.na;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Login extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// odszukujemy przyciski i podpinamy pod nie listenery
		View login = findViewById(R.id.button_login2);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		// Wykonywanie akcji w przypadku nacisniecia ktoregos z przyciskow
		switch (v.getId()) {
		case R.id.button_login2: // wcisniecie przycisku zaloguj
			// TODO logowanie
			break;
		}
	}
}