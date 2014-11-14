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
	
	protected void connectToFtp(String endUrl)
	{
		new GetDataFromUrl().execute(baseUrl + endUrl);	
	}
	
	public String getDataString()
	{
		return dataString;
	}
	
	private class GetDataFromUrl extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet httpGet = new HttpGet(params[0]);
	            HttpResponse response = httpClient.execute(httpGet);
	            HttpEntity entity = response.getEntity();

	            BufferedHttpEntity buf = new BufferedHttpEntity(entity);

	            InputStream is = buf.getContent();

	            BufferedReader r = new BufferedReader(new InputStreamReader(is));

	            StringBuilder total = new StringBuilder();
	            String line;
	            while ((line = r.readLine()) != null) {
	                total.append(line + "\n");
	            }
	            String result = total.toString();
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
	        	dataString = result;
	        }

		}
	}
}
