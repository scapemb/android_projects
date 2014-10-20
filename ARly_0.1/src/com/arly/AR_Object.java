package com.arly;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.util.Log;

import java.lang.Math;


public class AR_Object {

	protected String name = null;
	protected GeoLocation location = new GeoLocation();
	protected String source = null;
	
	private double azimuth = 0.0;						//azimuth angle (angle between POI, user and North)
	private double minViewAngle = 0.0;					//min angle for set POI on the screen
	private double maxViewAngle = 0.0;					//max angle for set POI on the screen
	
	private boolean inRadius = false;							//get POI in radius or not
	private boolean inView = false;								//get POI in view angle or not

    private float x = 0;                                //x coord. of icon
    private float y = 0;                                //y coord. of icon
    private int icon_size = 0;                          //size of icon in proportions

	protected double distance = 0.0;					//distance between POI and user
	
	public Bitmap icon = null;
	public AR_Object(){
	
	}
	
	public AR_Object(String name,String source, Bitmap icon, double latitude,double longitude)
	{
		set(name,source,icon,latitude,longitude);
	}
	
	public synchronized void set(String name, String source, Bitmap icon, double latitude,double longitude) 
	{
		if (name==null) throw new NullPointerException();

		this.name=name;
		this.source=source;
		this.icon=icon;
		this.location.setLatitude(latitude);
		this.location.setLongitude(longitude);
	}
	
	public synchronized void setAzimuth(GeoLocation userLoc)			//set azimuth angle (oriented on quarters of Decart coord. system)
	{
		
		double y0 = userLoc.getLatitude(), y1 = this.getLocation().getLatitude();
		double x0 = userLoc.getLongitude(),x1 = this.getLocation().getLongitude();
		if(y0<y1)				//1 & 2 quarters
		{
			if(x0<x1)			//1 quarter
			{
				this.setAzimuth(Math.toDegrees(Math.atan((x1-x0)/(y1-y0))));
			}
			else if(x0>x1)		//2 quarter
			{
				this.setAzimuth(360-Math.toDegrees(Math.atan((x0-x1)/(y1-y0))));
			}
		}
		else if(y0>y1)			//3 & 4 quarters
		{
			if(x0<x1)			//4 quarter
			{
				this.setAzimuth(90+Math.toDegrees(Math.atan((y0-y1)/(x1-x0))));
			}
			else if(x0>x1)		//3 quarter
			{
				this.setAzimuth(270-Math.toDegrees(Math.atan((y0-y1)/(x0-x1))));
			}
		}
		
		this.setDistance(x0, x1, y0, y1);		
		this.setViewAngles();
	}
	
	private synchronized void  setDistance(double x0, double x1, double y0, double y1)	//set distance 
	{
		this.distance = Math.sqrt(Math.pow(x0+x1, 2)+Math.pow(y0+y1, 2));
	}
	
	public synchronized double getDistance()
	{
		Location locA = new Location("POI");
		Location locB = new Location("User");
		
		locA.setLatitude(this.getLocation().getLatitude());
		locA.setLongitude(this.getLocation().getLongitude());
		
		locB.setLatitude(AR_activity.getLocation().getLatitude());
		locB.setLongitude(AR_activity.getLocation().getLongitude());
		
		return locA.distanceTo(locB);
	}
	
	private synchronized void setViewAngles()
	{
		this.setMinViewAngle(minViewAngle);
		this.setMaxViewAngle(maxViewAngle);
	}
	
	public GeoLocation getLocation()
	{
		return this.location;
	}
	
	public synchronized void setLocation(GeoLocation location)
	{
		this.location.setAltitude(location.getAltitude());
		this.location.setLatitude(location.getLatitude());
		this.location.setLongitude(location.getLongitude());
	}
	
	private synchronized void setAzimuth(double azimuth)
	{
		this.azimuth=azimuth;
	}
	
	public synchronized double getAzimuth()
	{
		return this.azimuth;
	}
	
	private synchronized void setMinViewAngle(double minViewAngle)
	{
		this.minViewAngle=this.getAzimuth()+(45/2);
	}
	
	private synchronized void setMaxViewAngle(double maxViewAngle)
	{
		this.maxViewAngle=this.getAzimuth()-(45/2);
	}
	
	private synchronized void setInView()
	{
		inView = true;
	}
	
	private synchronized void setOutView()
	{
		inView = false;
	}

    public synchronized String getName()
    {
        return this.name;
    }
	
	private synchronized void refreshInfo()
	{
		if(AR_activity.getPitch()<90-AR_activity.cameraHeight/2 || AR_activity.getPitch()>90+AR_activity.cameraHeight/2) 
		{
			Log.d("Pitch",String.valueOf(AR_activity.getPitch()));
			this.setOutView();
			return;
		}
		
		if(AR_activity.getAzimuth()-this.getAzimuth()>AR_activity.cameraWidth || AR_activity.getAzimuth()-this.getAzimuth()<-AR_activity.cameraWidth)
		{
			Log.d("Azimuth",String.valueOf(AR_activity.getAzimuth()-this.getAzimuth())+" name: "+this.name+" source: "+this.source);
			this.setOutView();
			return;
		}
		
		setInView();
	}

    public synchronized boolean handleClick(float x, float y) {
        if (!inView) return false;
        return isPointOnMarker(x,y);
    }

    private synchronized boolean isPointOnMarker(float x, float y) {
        float myX = this.x;
        float myY = this.y;
        float adjWidth = this.icon_size/2;
        float adjHeight = this.icon_size/2;

        float x1 = myX-adjWidth;
        float y1 = myY-adjHeight;
        float x2 = myX+adjWidth;
        float y2 = myY+adjHeight;

        if (x>=x1 && x<=x2 && y>=y1 && y<=y2) return true;

        return false;
    }
	
	synchronized void draw(Canvas canvas)
	{
		if (canvas==null) throw new NullPointerException();

		refreshInfo();
		if(!inView) return;
        drawIcon(canvas);
	}
	
	protected synchronized void drawIcon(Canvas canvas) {
    	if (canvas==null) throw new NullPointerException();

    	

    	if(Math.abs(AR_activity.getAzimuth()-this.getAzimuth())<5)
    	{
    		icon_size =(int) (AR_activity.displayHeight/5);
    	}
    	else
    	{
    		icon_size =(int) (AR_activity.displayHeight/10);
    	}
    	PaintIcon drawIcon = new PaintIcon(icon, icon_size, icon_size);



    	if((AR_activity.getAzimuth()-this.getAzimuth()) >0)
    	{
    		x = (float) (AR_activity.displayWidth/2 - (AR_activity.displayWidth/AR_activity.cameraWidth)*Math.abs(AR_activity.getAzimuth()-this.getAzimuth()));
    	}
    	else
    	{
    		x = (float) (AR_activity.displayWidth/2 + (AR_activity.displayWidth/AR_activity.cameraWidth)*Math.abs(AR_activity.getAzimuth()-this.getAzimuth()));
    	}
    	
    	y = (float) ((AR_activity.displayHeight/AR_activity.cameraHeight)*(AR_activity.getPitch()-70)-(getDistance()*0.2));
    	
    	PaintPosition PaintContainer = new PaintPosition(drawIcon,x,y,(float)AR_activity.getRoll(),1);
    	PaintContainer.paint(canvas);
    	PaintContainer.paintText(canvas, x, y+(icon_size /2)+50, this.name);
    }

   
}
