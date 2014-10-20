package com.arly;

public class Orientation {
	private double azimuth 	= 0.0;
	private double pitch 	= 0.0;
	private double roll 	= 0.0;
	
	public void set(double azimuth, double pitch, double roll)
	{
		this.roll=roll;
		this.azimuth=azimuth;
		this.pitch= -pitch;
	}
	
	public void setRoll(double roll)
	{
		this.roll=roll;
	}
	public void setPitch(double pitch)
	{
		this.pitch= -pitch;
	}
	public void setAzimuth(double azimuth)
	{
		this.azimuth=azimuth;
	}
	
	public double getRoll()
	{
		return roll;
	}
	public double getPitch()
	{
		return pitch;
	}
	public double getAzimuth()
	{
		return azimuth;
	}
}
