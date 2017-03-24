package com.candystudio.android.dxzs.teacher.examination;

import java.io.Serializable;

import android.R.integer;

public class Info implements Serializable
{
	private String iDNumber;
	private String pointInfo;
	private String dateStr;  //hh:mm:ss
	private String timeStr;
	private int x2000;
	private int y2000;
	private double longitude;
	private double latitude;
	private String pointMsg;
	
	
	public String getiDNumber() {
		return iDNumber;
	}

	public void setiDNumber(String iDNumber) {
		this.iDNumber = iDNumber;
	}

	public String getPointInfo() {
		return pointInfo;
	}

	public void setPointInfo(String pointInfo) {
		this.pointInfo = pointInfo;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public int getX2000() {
		return x2000;
	}

	public void setX2000(int x2000) {
		this.x2000 = x2000;
	}

	public int getY2000() {
		return y2000;
	}

	public void setY2000(int y2000) {
		this.y2000 = y2000;
	}

	public String getPointMsg() {
		return pointMsg;
	}

	public void setPointMsg(String pointMsg) {
		this.pointMsg = pointMsg;
	}
	public double getLongitude() {
		return longitude;
	}

	public void setLongtitude(double longtitude) {
		this.longitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	
	public Info(String IDNumber, String PointInfo,String DateStr
			,String TimeStr,int X2000,int Y2000,double Lon, double Lat,String PointMsg)
	{
		this.iDNumber=IDNumber;
		this.pointInfo=PointInfo;
		this.dateStr=DateStr;
		this.timeStr=TimeStr;
		this.x2000=X2000;
		this.y2000=Y2000;
		this.longitude=Lon;
		this.latitude=Lat;
		this.pointInfo=PointInfo;		
	}
	
	public Info()
	{
		// TODO Auto-generated constructor stub
	}
	
	

}
