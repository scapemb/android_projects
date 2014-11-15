package wait.geo_forecast;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class GeoActivity extends Activity {

	private CalendarView geoCalendar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo);
		
		geoCalendar = (CalendarView)findViewById(R.id.geoCalendarView);			
		geoCalendar.setOnDateChangeListener(new OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
				GetData gd = new GetData();
				if(year > 1996){
					try {
						gd.connectToFtp("indices/old_indices/" + year +"_DGD.txt");
						OldParser op = new OldParser();
						op.setParcelableString(gd.getDataString());
						
						String monthInFormat;
						String dayInFormat;
						if(month < 10) {
							monthInFormat = "0" + month;
						}
						else{monthInFormat = "" + month;}
						
						if(dayOfMonth < 10) {
							dayInFormat = "0" + dayOfMonth;
						}
						else{dayInFormat = "" + dayOfMonth;}
						
						op.parse(year, monthInFormat, dayInFormat);
						
						} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}

				}
			
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.geo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
