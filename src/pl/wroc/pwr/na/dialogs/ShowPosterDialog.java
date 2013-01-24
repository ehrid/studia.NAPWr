package pl.wroc.pwr.na.dialogs;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.EventActivity;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ShowPosterDialog extends Dialog implements
		android.view.View.OnClickListener {
	EventActivity rootView;
	int possition;
	
	public ShowPosterDialog(Context context, int possition) {
		super(context);
		rootView = (EventActivity) context;
		this.possition = possition;
	}
	
	WebView poster;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_poster);
		
		poster = (WebView) findViewById(R.id.dialog_poster_poster);
		poster.setOnClickListener(this);
		poster.loadUrl(((MenuActivity) (MenuActivity.activityMain)).current.get(possition).poster.toString());
		
		this.findViewById(android.R.id.title).setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_poster_poster:
			dismiss();
			break;

		}
	}
}
