package pl.wroc.pwr.na.dialogs;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.EventActivity;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

public class ShowPosterDialog extends Dialog implements
		android.view.View.OnClickListener {
	EventActivity rootView;
	Context context;
	int possition;

	public ShowPosterDialog(Context context, int possition, Context appContext) {
		super(context);
		rootView = (EventActivity) context;
		this.context = appContext;
		this.possition = possition;
	}

	ImageView poster;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_poster);

		poster = (ImageView) findViewById(R.id.dialog_poster_poster);
		poster.setOnClickListener(this);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		rootView.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int xSize = displaymetrics.widthPixels - 30;

		Bitmap bitmapPoster = ((MenuActivity) (MenuActivity.activityMain)).current
				.get(possition).getImagePoster(context);
		bitmapPoster = Bitmap.createScaledBitmap(bitmapPoster, xSize,
				bitmapPoster.getHeight() * xSize / bitmapPoster.getWidth(),
				true);

		poster.setImageBitmap(bitmapPoster);

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
