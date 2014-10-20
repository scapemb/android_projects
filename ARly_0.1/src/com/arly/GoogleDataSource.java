package com.arly;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;



public class GoogleDataSource extends NetworkDataSource {
	 private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/search/json";

	 private static Bitmap icon = null;
	 
	 public GoogleDataSource() {        
		    
	    }

	    protected void createIcon(Resources res) {
	        if (res==null) throw new NullPointerException();
	        
	        icon=BitmapFactory.decodeResource(res, R.drawable.google);
	    }
	 
	 @Override
	 public String createRequestURL(double lat, double lon, double alt, double radius, String locale) {
		 Log.d("LOCATION",lat+lon+alt+radius+locale);
		 
		return BASE_URL+
	        "?location=" + lat + "," + lon +
	        "&radius="+ radius +
	        "&sensor=true" +
	        "&language=" + locale +
	        "&key=AIzaSyCudvK7hOiGJFYjAziSpDHmedS2JH24wn4";
	}
	 public List<AR_Object> parse(JSONObject root) {
			if (root==null) return null;
			
			JSONObject jo = null;
			JSONArray dataArray = null;
	    	List<AR_Object> markers=new ArrayList<AR_Object>();

			try {
				if(root.has("results")) dataArray = root.getJSONArray("results");
				if (dataArray == null) return markers;
					int top = Math.min(MAX, dataArray.length());
					for (int i = 0; i < top; i++) {					
						jo = dataArray.getJSONObject(i);
						AR_Object ma = processJSONObject(jo);
						if(ma!=null) markers.add(ma);
					}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return markers;
	 } 
		
	 private AR_Object processJSONObject(JSONObject jo) {
			if (jo==null) return null;
			
			double lat = 0.0, lng = 0.0;
			String name = null;
			AR_Object ma = new AR_Object();
	        if (jo.has("geometry") && jo.has("name")) 
	        {
	        try {
	        	JSONObject geo = jo.getJSONObject("geometry");
	        	JSONObject coord = geo.getJSONObject("location");
	        	
	        	lat = Double.parseDouble(coord.getString("lat"));
	        	lng = Double.parseDouble(coord.getString("lng"));
	        	name = jo.getString("name");
	        	ma.set(
	        				name,
	        				"Google",
	        				icon,
	        				lat,
	        				lng);
	        	} catch (JSONException e) {
	        		e.printStackTrace();
	        	}
	        }
	        return ma;
		}
}
