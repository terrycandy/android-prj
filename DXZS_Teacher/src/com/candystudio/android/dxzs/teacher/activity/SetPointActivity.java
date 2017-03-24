package com.candystudio.android.dxzs.teacher.activity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.candystudio.android.dxzs.teacher.app.R;
import com.candystudio.android.dxzs.teacher.position.Converter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetPointActivity extends Activity {

	private Button setPointButton, curLocButton;
	private TextView x2000TextView, y2000TextView;
	private TextView dlg_x2000TextView, dlg_y2000TextView;
	private EditText dlg_markerEditText, dlg_msgEditText;

	private MapView setMapView;
	private BaiduMap bdmap;
	private AlertDialog setPointAlertDialog;
	private int curX2000, curY2000;
	private double curLon, curLat, curAlt;

	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private LatLng myCurLocation;
	boolean isFirstLoc = true;

	private static String FILE_NAME = "pointlib.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_set_point);
		initBD();
		mLocClient.start();

	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || setMapView == null) {
				return;
			}
			curLat = location.getLatitude();
			curLon = location.getLongitude();
			curAlt = location.getAltitude();
			myCurLocation = new LatLng(curLat, curLon);
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection()).latitude(curLat)
					.longitude(curLon).build();
			bdmap.setMyLocationData(locData);

			Converter converter = new Converter();
			converter.Convert_84GEOto2000PLAT(curLon, curLat, curAlt);
			curX2000 = converter.getPx2000();
			curY2000 = converter.getPy2000();

			x2000TextView.setText("X:" + String.valueOf(curX2000));
			y2000TextView.setText("Y:" + String.valueOf(curY2000));
			if (isFirstLoc) {
				isFirstLoc = false;
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLng(myCurLocation);
				bdmap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

	}

	private void InitialiseDialog() {
		LinearLayout dialoglayout = (LinearLayout) getLayoutInflater().inflate(
				(R.layout.dialog_set_point), null);
		dlg_x2000TextView = (TextView) dialoglayout.findViewById(R.id.tv_x);
		dlg_y2000TextView = (TextView) dialoglayout.findViewById(R.id.tv_y);
		dlg_markerEditText = (EditText) dialoglayout.findViewById(R.id.et_marker);
		dlg_msgEditText = (EditText) dialoglayout.findViewById(R.id.et_message);
		dlg_x2000TextView.setText("X:" + String.valueOf(curX2000));
		dlg_y2000TextView.setText("Y:" + String.valueOf(curY2000));
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(
				SetPointActivity.this);
		mBuilder.setTitle("设点")
				.setView(dialoglayout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						String marker = dlg_markerEditText.getText().toString();
						String msg = dlg_msgEditText.getText().toString();
						String record = curLat + "-" + curLon + "-" + curAlt
								+ "-" + curX2000 + "-" + curY2000 + "-"
								+ marker + "-" + msg + "\r\n";
						savePoint(record);
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						setPointAlertDialog.dismiss();
					}
				});
		setPointAlertDialog = mBuilder.create();
	}

	private void savePoint(String record) {
		try {
			String pathString = Environment
					.getExternalStorageDirectory() + FILE_NAME;
			FileOutputStream fos = openFileOutput(pathString,
					Activity.MODE_PRIVATE);
			fos.write(record.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initBD() {
		setPointButton = (Button) findViewById(R.id.set_button);
		curLocButton = (Button) findViewById(R.id.loc_button);
		x2000TextView = (TextView) findViewById(R.id.Tv_x);
		y2000TextView = (TextView) findViewById(R.id.Tv_y);
		setMapView = (MapView) findViewById(R.id.set_mapview);
		bdmap = setMapView.getMap();
		setPointButton.setOnClickListener(new Mylistener());
		curLocButton.setOnClickListener(new Mylistener());
		bdmap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		bdmap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);

	}

	private class Mylistener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.set_button:
				InitialiseDialog();
				setPointAlertDialog.show();
				break;

			case R.id.loc_button:
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLng(myCurLocation);
				bdmap.animateMapStatus(u);
				break;
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.set_point, menu);
		return true;
	}

}
