package wait.geo_forecast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class GetData {

	protected final String baseUrl = "http://www.swpc.noaa.gov/ftpdir/";
	
	protected String dataString;
	
	/**
	  * <p>Async connect to FTP server to get information about K-indices</p>
	  * <p>Information data provider - NOAA (National Oceanic & Atmospheric Administration).
	  * Base FTP address is binded in GetData class. You need to add ending of URL.
	  * Get raw String with all data (need to parse). </p>
	  * <p>Use "indices/old_indices/yyyy_DGD.txt" for getting indices of year
	  * or "latest/geomag_forecast.txt" for getting 3-days forecast.
	  *
	  * @param endUrl 	ending of FTP's Url 
	  * @return void
	  */
	protected void connectToFtp(String endUrl)
	{
		new GetDataFromUrl().execute(baseUrl + endUrl);	
	}
	
	/**
	  * <p>Return String with raw data (need to parse).
	  *
	  * @return 	raw String with indicies
	  */
	public String getDataString()
	{
		return dataString;
	}
	
	/**
	  * <p>Async getting data.
	  * Main processes in doInBackground. 
	  * Returns raw String with data.
	  * 
	  *	@param 	dataString 	full FTP's Url (base + end)
	  * @return raw String with indicies
	  */
	private class GetDataFromUrl extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {		//start Async task (used for notification about task's start)
				
		}
		
		@Override
		protected String doInBackground(String... params) {		//main body of Async task (getting raw String from FTP via URL)
			try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet httpGet = new HttpGet(params[0]);						//binding URL of FTP to connect
	            HttpResponse response = httpClient.execute(httpGet);			//getting response (raw data)
	            HttpEntity entity = response.getEntity();

	            BufferedHttpEntity buf = new BufferedHttpEntity(entity);

	            InputStream is = buf.getContent();								//initiate InputStream to read response

	            BufferedReader r = new BufferedReader(new InputStreamReader(is));

	            StringBuilder total = new StringBuilder();
	            String line;
	            while ((line = r.readLine()) != null) {							//reading the line of response
	                total.append(line + "\n");										//& append line to String 
	            }
	            String result = total.toString();								//get result String
	            Log.i("Get URL", "Downloaded string: " + result);
	            return result;
	        } catch (Exception e) {
	            Log.e("Get Url", "Error in downloading: " + e.toString());
	        }
	        return null;
		}
	
		@Override
	    protected void onPostExecute(String result) {
			if (result == null) {
	           dataString = "Err";
				
	        } else {
	        	dataString = result;											//bind result data to dataString
	        }

		}
	}
}
