package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class GeoActivity extends FragmentActivity  {

	private CalendarView geoCalendar;
	
	private String monthInFormat;
	private String dayInFormat;
	private String yearInFormat;
	private OldParser op = new OldParser();
	private ForecastParser fp = new ForecastParser();
	private GetData gd = new GetData();
	
	private FragmentManager fm = getSupportFragmentManager();
	private Bundle sendBundle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo);
		
		geoCalendar = (CalendarView)findViewById(R.id.geoCalendarView);	
		
		 
		
		geoCalendar.setOnDateChangeListener(new OnDateChangeListener() {
			
			Long date = geoCalendar.getDate();
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
				
				if(geoCalendar.getDate() != date){
					date = geoCalendar.getDate(); 

								
					month++;
					setDataInFormat( year,  month,  dayOfMonth);
									
					if(year > 1996){
						Date currentDate = new Date();
						if(geoCalendar.getDate() < currentDate.getTime()){
							
							try {
								gd.connectToFtp("indices/old_indices/" + yearInFormat +"_DGD.txt");
								op.setParcelableString(gd.getDataString());
								ArrayList<Integer> indices = op.parse(String.valueOf(year), monthInFormat, dayInFormat);
								
								if(indices != null){
									sendBundle = new Bundle();
									sendBundle.putIntegerArrayList("numlist", indices);
									
									DataDialogFragment showBriefDialog = new DataDialogFragment();
									
									showBriefDialog.setArguments(sendBundle);
									showBriefDialog.show(fm, "fragment_send_message");
								}
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							try {
								gd.connectToFtp("latest/geomag_forecast.txt");
								fp.setParcelableString(gd.getDataString());
								ArrayList<ArrayList<Integer>> indices = fp.parse();
								
								if(indices != null){
									ArrayList<Integer> formattedIndices = new ArrayList<Integer>();
									for(ArrayList<Integer> i : indices)
									{
										formattedIndices.addAll(i);
									}
									sendBundle = new Bundle();
									sendBundle.putIntegerArrayList("numlist", formattedIndices);
									
									DataDialogFragment showBriefDialog = new DataDialogFragment();
									
									showBriefDialog.setArguments(sendBundle);
									showBriefDialog.show(fm, "fragment_send_message");
								}
							}catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
			
		});
	}

	private void setDataInFormat(int year, int month, int dayOfMonth){
		if(month < 10) {
			monthInFormat = "0" + month;
		}
		else{monthInFormat = "" + month;}
		
		if(dayOfMonth < 10) {
			dayInFormat = "0" + dayOfMonth;
		}
		else{dayInFormat = "" + dayOfMonth;}
		
		
		Calendar calendar = Calendar.getInstance();
		
		if(year == calendar.get(Calendar.YEAR)){
			if(month < 4){
				yearInFormat = year + "Q1";
			}
			else if(month >= 4 && month < 7){
				yearInFormat = year + "Q2";
			}
			else if(month >= 7 && month < 10){
				yearInFormat = year + "Q3";
			}
			else{
				yearInFormat = year + "Q4";
			}
		}
		else{yearInFormat = "" + year;}
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
