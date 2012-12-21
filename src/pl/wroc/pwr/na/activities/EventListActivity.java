package pl.wroc.pwr.na.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.adapters.EventListAdapter;
import pl.wroc.pwr.na.objects.EventObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class EventListActivity extends Activity implements OnClickListener {

	public static final String LIST_TITLE = "pl.wroc.pwr.na.list_title";

	private TextView title;
	private ListView eventListView;
	private EventListAdapter adapter;
	private Context context;

	public EventObject event;

	private static EventListActivity singleInstance = null;

	public static EventListActivity getInstance() {
		return singleInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		context = this.getApplicationContext();

		title = (TextView) findViewById(R.id.eventlist_title);
		title.setText(getIntent().getStringExtra(LIST_TITLE));
		eventListView = (ListView) findViewById(R.id.event_list_events);
		addEvents();

		singleInstance = this;

		eventListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				event = adapter.getEvent(position);
				startActivity(new Intent(context, EventActivity.class));
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public void addEvents(){
		String wydarzenieTytul = "sciema";
		String wydarzenieTresc = "sciema";
		int wydarzenieSumaLajkow = 0;
		Date wydarzenieDataPoczatek = new Date();
		JSONObject event;
		
		JSONArray completeJSONArr = null;
		try {
			//new RequestTask().execute("http://na.pwr.wroc.pl/mobile/wydarzenia/jutro");
			
			if(getIntent().getStringExtra(LIST_TITLE).equals("Dzisiaj"))
			{
				completeJSONArr = new JSONArray((String) new RequestTask().execute("http://na.pwr.wroc.pl/mobile/wydarzenia/dzis").get());
			}
			else
			{
				completeJSONArr = new JSONArray((String) new RequestTask().execute("http://na.pwr.wroc.pl/mobile/wydarzenia/jutro").get());
			}
			

			
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// Log.d(TAG, "httpclient.execute failed");
			e.printStackTrace();
			
		}

		ArrayList<EventObject> eventList = new ArrayList<EventObject>();

		for (int i = 0; i < completeJSONArr.length(); i++) {
			
			try {
				event = completeJSONArr.getJSONObject(i);
				wydarzenieTytul = event.getString("wydarzenieTytul");
				wydarzenieTresc = event.getString("wydarzenieTresc");
				
				wydarzenieSumaLajkow = event.getInt("wydarzenieSumaLajkow");
				
				//wydarzenieDataPoczatek = event.get("wydarzenieDataPoczatek");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			eventList
					.add(new EventObject(
							wydarzenieTytul,
							i,
							wydarzenieTresc,
							"http://www.na.pwr.wroc.pl/Symfony/web/bundles/cona/img/plakaty/small-5733534440398.jpg",
							wydarzenieSumaLajkow, new Date()));
		}

		adapter = new EventListAdapter(this, R.layout.item_event_list,
				eventList);
		eventListView.setAdapter(adapter);
	}

	class RequestTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					
					responseString = out.toString();
					out.close();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase()+ uri[0]);
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..
		}
	}

}
