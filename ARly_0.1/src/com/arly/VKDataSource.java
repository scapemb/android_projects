package com.arly;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

public class VKDataSource extends NetworkDataSource{
	private static final String URL = "https://api.vkontakte.ru/method/places.search";

	private static Bitmap icon = null;

	public VKDataSource() {        
	   
    }

    protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();
        
        icon=BitmapFactory.decodeResource(res,R.drawable.vk);
    }
	
	@Override
	public String createRequestURL(double lat, double lon, double alt, double radius, String locale) {
		int rad = 1;
		
		if(radius<=0.3) {rad = 1;}
		else if(radius>0.3 && radius<=2.4) {rad = 2;}
		else if(radius>2.4 && radius<=18) {rad = 3;}
		else if(radius>18) {rad = 4;}
		
		return URL+
        "?latitude=" + lat +
        "&longitude=" + lon +
        "&radius="+ rad +
        "&count=40"+
        "&access_token=7a9ce938814d5a60b759bf0e97ee189ce8720cc5df51a25a8e2a514d3ad2e8742196f143b1987e8a9b137";

	}

	@Override
	public List<AR_Object> parse(String url) {
		if (url==null) throw new NullPointerException();
		
		InputStream stream = null;
    	stream = getHttpGETInputStream(url);
    	if (stream==null) throw new NullPointerException();
    	
    	String string = null;
    	string = getHttpInputString(stream);
    	if (string==null) throw new NullPointerException();
    	
    	JSONObject json = null;
    	try {
    		json = new JSONObject(string);
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	}
    	if (json==null) throw new NullPointerException();
    	
    	return parse(json);
	}

	@Override
	public List<AR_Object> parse(JSONObject root) {
		if (root==null) throw new NullPointerException();
		
		JSONObject jo = null;
		JSONArray dataArray = null;
    	List<AR_Object> markers=new ArrayList<AR_Object>();

		try {
			if(root.has("response")) 
			{
				dataArray = root.getJSONArray("response");
			}
			if (dataArray == null) return markers;
			int top = Math.min(MAX, dataArray.length());
			for (int i =1; i < top; i++) {					
				jo = dataArray.getJSONObject(i);
				AR_Object ma = processJSONObject(jo);
				if(ma.getLocation().getLatitude()!=0.0) markers.add(ma);
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
        		jo.has("latitude") && 
        		jo.has("longitude")
        ) {
        	try {
        		ma.set(
        				jo.getString("title"),
        				"VKontakte",
        				icon,
        				jo.getDouble("latitude"),
        				jo.getDouble("longitude"));
        	} catch (JSONException e) {
        		e.printStackTrace();
        	}
        }
        return ma;
	}
}
