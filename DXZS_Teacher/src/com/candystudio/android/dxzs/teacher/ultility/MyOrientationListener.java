package com.candystudio.android.dxzs.teacher.ultility;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener
{
	private SensorManager mSensorManager;
	private Context mContext;
	private Sensor mSensor;
	private float lastx;
	private OnOrientationListener mOnOrientationListener;
	
	public void start()
	{
		mSensorManager=(SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		if(mSensorManager!=null)
		{
			mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		if(mSensor!=null)
		{
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		}
	}
	
	public void stop()
	{
		mSensorManager.unregisterListener(this);
		
	}
	
	public MyOrientationListener(Context context)
	{
		this.mContext=context;
		
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if(event.sensor.getType()==Sensor.TYPE_ORIENTATION)
		{
			float x=event.values[SensorManager.DATA_X];
			if(Math.abs(x-lastx)>1.0)
			{
				if(mOnOrientationListener!=null)
				{
					mOnOrientationListener.OnOrientationChanged(x);
				}
				
			}
			lastx=x;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub

	}
	
	public interface OnOrientationListener
	{
		void OnOrientationChanged(float x);
	}

	public void setmOnOrientationListener(
			OnOrientationListener mOnOrientationListener)
	{
		this.mOnOrientationListener = mOnOrientationListener;
	}
	

}
