package pl.wroc.pwr.na.dialogs;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MenuDialog extends Dialog implements
		android.view.View.OnClickListener {
	MenuActivity rootView;

	public MenuDialog(Context context) {
		super(context);
		rootView = (MenuActivity) context;
	}

	LinearLayout refresh;
	LinearLayout exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_settings);

		refresh = (LinearLayout) findViewById(R.id.menu_refresh);
		refresh.setOnClickListener(this);
		exit = (LinearLayout) findViewById(R.id.menu_exit);
		exit.setOnClickListener(this);

		this.findViewById(android.R.id.title).setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_refresh:
			rootView.eventDownload();
			dismiss();
			break;
		case R.id.menu_exit:
			rootView.closeApplication();
			dismiss();
			break;

		}
	}
}
