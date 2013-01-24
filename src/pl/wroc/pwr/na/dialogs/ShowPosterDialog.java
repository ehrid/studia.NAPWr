package pl.wroc.pwr.na.dialogs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.EventActivity;
import pl.wroc.pwr.na.activities.MenuActivity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

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
		
		String data="<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width="+300+", initial-scale=0.65 \" /></head>";
		data=data+"<body><center><img width=\""+300+"\" src=\""+((MenuActivity) (MenuActivity.activityMain)).current.get(possition).bigPoster.toString()+"\" /></center></body></html>";
		poster.loadData(data, "text/html", null);

		poster.getSettings().setBuiltInZoomControls(true);
		poster.getSettings().setSupportZoom(true);

		
		this.findViewById(android.R.id.title).setVisibility(View.GONE);

	}
	
	public Object fetch(String address) throws MalformedURLException,
    IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }  

    private Drawable ImageOperations(Context ctx, String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
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
