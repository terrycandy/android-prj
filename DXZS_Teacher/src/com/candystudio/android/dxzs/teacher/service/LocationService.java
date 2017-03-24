package com.candystudio.android.dxzs.teacher.service;


import com.candystudio.android.dxzs.teacher.ultility.LogUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service
{
	private LocationManager mLocationManager;
	private String provider;
	private static Location location=null;
	private static boolean LocationSuccess=false;
	private static GPSLocationListener mLocationListener;
	public static void actionStart(Context context,GPSLocationListener listener)
	{
		Intent intent=new Intent(context,LocationService.class);
		context.startService(intent);
		mLocationListener=listener;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{

		mLocationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		location=mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location!=null)
		{
			LocationSuccess=true;
			mLocationListener.OnGetLocation(location);
			
		}
		else
		{
			LocationSuccess=false;
		}
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
		super.onCreate();
	}
	LocationListener locationListener=new LocationListener()
	{
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			if(status==LocationProvider.AVAILABLE)
			{
				LocationSuccess=true;
			}
			
		}
		
		@Override
		public void onProviderEnabled(String provider)
		{
			
		}
		
		@Override
		public void onProviderDisabled(String provider)
		{
			
		}
		
		@Override
		public void onLocationChanged(Location location)
		{
			LocationSuccess=true;
			LocationService.this.location=location;
			mLocationListener.OnGetLocation(location);
			
			
		}
	};

	
	@Override
	public void onDestroy()
	{
		mLocationManager.removeUpdates(locationListener);
		super.onDestroy();
	}
	public static Location getLocation()
	{
		return location;
	}
	public static boolean getLocationStatu()
	{
		return LocationSuccess;
	}

	public interface GPSLocationListener
	{
		void OnGetLocation(Location location);
	}
}
