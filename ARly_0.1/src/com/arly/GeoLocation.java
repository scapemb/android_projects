package com.arly;

public class GeoLocation {

	private double altitude = 0.0;
	private double longitude = 0.0;
	private double latitude = 0.0;
	
	public void set(double latitude, double longitude, double altitude)
	{
		this.altitude=altitude;
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	public void setAltitude(double altitude)
	{
		this.altitude=altitude;
	}
	public void setLongitude(double longitude)
	{
		this.longitude=longitude;
	}
	public void setLatitude(double latitude)
	{
		this.latitude=latitude;
	}
	
	public double getAltitude()
	{
		return altitude;
	}
	public double getLongitude()
	{
		return longitude;
	}
	public double getLatitude()
	{
		return latitude;
	}
	
}
