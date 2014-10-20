package com.arly;


import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;



public class AR_activity extends AugmentedActivity{
	SurfaceView cameraPreview=null;
	SurfaceHolder previewHolder=null;
	SensorManager sensorManager;
	Camera camera=null;
	boolean inPreview;
	
	String wiki_url = null, google_url = null, twitter_url = null, vk_url = null, osm_url = null;
    
	static final String locale = "ru";
	private Resources res = null;
    DialogFragment infoDlg, radiusDlg;
	static double radius = 1.0;
	int POI_choose = 1;
	static AR_Object nowObj = null;
	
	static Context context = null;

	int orientationSensor;
	static Orientation orientation = new Orientation(); 				//position of user's device

	LocationManager locationManager;
	static GeoLocation location = new GeoLocation();					//geoposition of user
	
	public static List<AR_Object> AR_objects_sum 	= new ArrayList<AR_Object>();	//summary list of parsed objects (google, wiki, twitter, vk)
	public static List<AR_Object> AR_objects_cache 	= new ArrayList<AR_Object>();	//cache of parsed objects (google, wiki, twitter, vk)

	public static float displayWidth;
	public static float displayHeight;
	
	public static float cameraWidth;
	public static float cameraHeight;
	
	GoogleDataSource googleSource = new GoogleDataSource();
	WikipediaDataSource wikiSource = new WikipediaDataSource();
	TwitterDataSource twitterSource = new TwitterDataSource();
	VKDataSource vkSource = new VKDataSource();
    OsmDataSource osmSource = new OsmDataSource();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        res = this.getResources();
        
        googleSource.createIcon(res);
    	wikiSource.createIcon(res);
    	twitterSource.createIcon(res);
    	vkSource.createIcon(res);
        osmSource.createIcon(res);
    	
    	Display display = getWindowManager().getDefaultDisplay();
    	Log.d("Resolution", "resolution: "+display.getWidth()+" x "+ display.getHeight());
    	displayWidth=display.getWidth();
    	displayHeight=display.getHeight();
    	try{
    	cameraWidth = camera.getParameters().getHorizontalViewAngle();
    	cameraHeight = camera.getParameters().getVerticalViewAngle();
    	}
    	catch(Exception e)
    	{
    		cameraWidth=45;
    		cameraHeight=45;
    	}
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 50, locationListener);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        orientationSensor = Sensor.TYPE_ORIENTATION;
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(orientationSensor), SensorManager.SENSOR_DELAY_NORMAL);
       
        inPreview = false;
        
        cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
        
        previewHolder = cameraPreview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        context = getApplicationContext();
        
        infoDlg = new InfoDialog();
        radiusDlg = new RadiusDialog();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.activitymenu, menu);
       return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     
     	if (item.getItemId()        == R.id.google)     {POI_choose = 1; }
     	else if (item.getItemId()   == R.id.wikipedia)  {POI_choose = 2;}
     	else if (item.getItemId()   == R.id.twitter)    {POI_choose = 3; }
     	else if (item.getItemId()   == R.id.vk)         {POI_choose = 4; }
        else if (item.getItemId()   == R.id.osm)        {POI_choose = 5; }
     	//case R.id.google:{}
     	
        else if(item.getItemId() == R.id.radius) onRadiusChange();
     
    	refreshList();
    	return super.onOptionsItemSelected(item);
    }
    
    @SuppressLint("NewApi")
    public void onRadiusChange()
    {
    	 radiusDlg.show(getFragmentManager(), "dlg");
    	 refreshList();
    }
    
    public static List<AR_Object> getMarkers() { 	
    	return AR_objects_sum; 	
    }
    
    public void refreshList()
    {
    	try{	
			wiki_url = wikiSource.createRequestURL(location.getLatitude(), location.getLongitude(), 0.0, radius, locale);
			google_url = googleSource.createRequestURL(location.getLatitude(), location.getLongitude(), 0.0, radius*1000, locale);
			twitter_url = twitterSource.createRequestURL(location.getLatitude(), location.getLongitude(), 0.0, radius, locale);
			vk_url = vkSource.createRequestURL(location.getLatitude(), location.getLongitude(), 0.0, radius, locale);
            osm_url = osmSource.createRequestURL(location.getLatitude(), location.getLongitude(), 0.0, radius, locale);
			
		} catch (NullPointerException e) {
			Log.d("LOCATION","Request URL doesn't create!");
		}
		try {
		
			if(isNetworkAvailable())
			{
				
				Log.d("_NET","Net connection is available");
				
				if(POI_choose==1)       {AR_objects_sum=googleSource.parse(google_url);}
				else if(POI_choose==2)  {AR_objects_sum=wikiSource.parse(wiki_url);}
                else if(POI_choose==3)  {AR_objects_sum=twitterSource.parse(twitter_url);}
                else if(POI_choose==4)  {AR_objects_sum=vkSource.parse(vk_url);}
                else if(POI_choose==5)  {AR_objects_sum=osmSource.parse(osm_url);}

				for(int i=0;i<AR_objects_sum.size();i++)
				{
					AR_objects_sum.get(i).setAzimuth(location);
				}
				
			}
			else
			{
				Log.d("_NET","Error with net connection");
			}
		} catch (NullPointerException e) {
			Log.d("LOCATION","Source isn't parsed!!");
		}
    }

    @SuppressLint("NewApi")
	@Override
    protected void objectTouched(AR_Object obj) {
        Toast t = Toast.makeText(getApplicationContext(), obj.getName(), Toast.LENGTH_SHORT);
        nowObj = obj;
        
    infoDlg.show(getFragmentManager(), "dlg");

        t.show();
    }

    private boolean isNetworkAvailable(){
        boolean available = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
 
        if(networkInfo !=null && networkInfo.isAvailable())
            available = true;
 
        return available;
    }
    
    LocationListener locationListener = new LocationListener() {
    	public void onLocationChanged(Location loc) {
    		location.set(loc.getLatitude(),loc.getLongitude(),loc.getAltitude());
    		double lat = location.getLatitude();
    		double lon = location.getLongitude();
    		double alt = location.getAltitude();
    		
    		refreshList();
    		
    	}

    	
    	
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
    };
    
    final SensorEventListener sensorEventListener = new SensorEventListener() {
    	public void onSensorChanged(SensorEvent sensorEvent) {
    		if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION)
    		{
    			orientation.set(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
    		
    			try
    			{
    				for(int i=0;i<AR_objects_sum.size();i++)
    				{
    					augmentedView.refresh();
    				}
    			}
    			catch(Exception ex)
    			{
    				Log.d("ARRAY_","AR_objects is not available");
    			}
    			
    		}
    	}
    	
    	public void onAccuracyChanged (Sensor sensor, int accuracy) {
    		//Not used
    	}
    };
    
    public static double getRoll()
    {
    	return orientation.getRoll();
    }
    public static double getPitch()
    {
    	return orientation.getPitch();
    }
    public static double getAzimuth()
    {
    	return orientation.getAzimuth();
    }
    public static GeoLocation getLocation()
    {
    	return location;
    }
    
    @Override
    public void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 50, locationListener);
      sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(orientationSensor), SensorManager.SENSOR_DELAY_NORMAL);
      camera=Camera.open();
    }
      
    @Override
    public void onPause() {
      if (inPreview) {
        camera.stopPreview();
      }
      locationManager.removeUpdates(locationListener);
      sensorManager.unregisterListener(sensorEventListener);
      camera.release();
      camera=null;
      inPreview=false;
            
      super.onPause();
    }
 
    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
    	Camera.Size result=null;

    	for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
    		if (size.width<=width && size.height<=height) {
    			if (result==null) {
    				result=size;
    			}
    			else {
    				int resultArea=result.width*result.height;
    				int newArea=size.width*size.height;

    				if (newArea>resultArea) {
    					result=size;
    				}
    			}
    		}
    	}

    	return(result);
    }
    
    SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
    	public void surfaceCreated(SurfaceHolder holder) {
    		try {
    			camera.setPreviewDisplay(previewHolder);	
    		}
    		catch (Throwable t) {
    			Log.e("CAMERA", "Exception in setPreviewDisplay()", t);
    		}
    	}
    	
    	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
    		Camera.Parameters parameters=camera.getParameters();
    		Camera.Size size=getBestPreviewSize(width, height, parameters);

    		if (size!=null) {
    			parameters.setPreviewSize(size.width, size.height);
    			camera.setParameters(parameters);
    			camera.startPreview();
    			inPreview=true;
    		}
	}
    	
    	public void surfaceDestroyed(SurfaceHolder holder) {
    		// not used
    	}
    	
    };
}