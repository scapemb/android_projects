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


public class WikipediaDataSource extends NetworkDataSource {
	private static final String BASE_URL = "http://ws.geonames.org/findNearbyWikipediaJSON";

	private static Bitmap icon = null;
	
	public WikipediaDataSource() {        

    }
 
	protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();
        
        icon=BitmapFactory.decodeResource(res, R.drawable.wikipedia);
    }

	@Override
	public String createRequestURL(double lat, double lon, double alt, double radius, String locale) {
		Log.d("LOCATION",lat+lon+alt+radius+locale);
		return BASE_URL+
        "?lat=" + lat +
        "&lng=" + lon +
        "&radius="+ radius +
        "&maxRows=40" +
        "&lang=" + locale;

	}

	@Override
	public List<AR_Object> parse(JSONObject root) {
		if (root==null) return null;
		
		JSONObject jo = null;
		JSONArray dataArray = null;
    	List<AR_Object> markers=new ArrayList<AR_Object>();

		try {
			if(root.has("geonames")) dataArray = root.getJSONArray("geonames");
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
		
        AR_Object ma = new AR_Object();
        if (	jo.has("title") && 
        		jo.has("lat") && 
        		jo.has("lng")
        ) {
        	try {
        		ma.set(
        				jo.getString("title"),
        				"Wikipedia",
        				icon,
        				jo.getDouble("lat"),
        				jo.getDouble("lng"));
        	} catch (JSONException e) {
        		e.printStackTrace();
        	}
        }
        return ma;
	}
}