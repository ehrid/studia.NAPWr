package pl.wroc.pwr.na.dialogs;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CloseAppDialog extends Dialog implements
		android.view.View.OnClickListener {
	MenuActivity rootView;

	public CloseAppDialog(Context context) {
		super(context);
		rootView = (MenuActivity) context;
	}

	Button yes;
	Button no;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_basic);
		
		title = (TextView) findViewById(R.id.dialog_title);

		yes = (Button) findViewById(R.id.dialog_logout_yes);
		yes.setOnClickListener(this);
		no = (Button) findViewById(R.id.dialog_logout_no);
		no.setOnClickListener(this);
		this.findViewById(android.R.id.title).setVisibility(View.GONE);
		
		title.setText(rootView.getResources().getString(R.string.dialog_close));

//		yes.setBackgroundResource(R.drawable.send);
//		yes.setTextSize(20);
//		yes.setTextColor(Color.WHITE);
//		no.setBackgroundResource(R.drawable.send);
//		no.setTextSize(20);
//		no.setTextColor(Color.WHITE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_logout_yes:
			rootView.closeApplication();
			dismiss();
			break;
		case R.id.dialog_logout_no:
			dismiss();
			break;

		}
	}
}
