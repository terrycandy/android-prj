package com.candystudio.android.dxzs.teacher.app;


//import com.studentclienttest.app.service.LocationService;

import android.app.Application;
import android.content.Context;
//import android.content.Intent;

public class MyApplication extends Application
{
	private static MyApplication appInstance;
//	private static HashMap<String, Object> CacheMap; //少量的缓存数据

	@Override
	public void onCreate()
	{
		super.onCreate();
		appInstance=this;
	}
	public static Context getInstance()
	{
		return appInstance;
	}


}
