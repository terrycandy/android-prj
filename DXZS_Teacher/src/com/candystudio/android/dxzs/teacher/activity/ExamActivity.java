package com.candystudio.android.dxzs.teacher.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.candystudio.android.dxzs.teacher.app.R;
import com.candystudio.android.dxzs.teacher.model.LocationClass;
import com.candystudio.android.dxzs.teacher.task.QueryLocationThread;

public class ExamActivity extends Activity implements OnClickListener {

	private ArrayList<BDLocation> studentLocations;

	private MapView examMapView;
	private BaiduMap map;
	public MyLocationListenner myListener = new MyLocationListenner();

	private LocationClient locationClient;
	private BDLocation curBdLocation;

	private Button curLocButton, SwitchButton, searchButton;
	private EditText searchET;

	private LocationMode curMode;
	private BitmapDescriptor curMarker;
	private MyLocationConfiguration curConfiguration;

	private QueryLocationThread queryLocationThread;
	private ArrayList<LocationClass> locations;

	private static final int accuracyCircleFillColor = 0xAAFFFF88;
	private static final int accuracyCircleStrokeColor = 0xAA00FF00;

	private boolean isFirstLoc = true; // �Ƿ��״ζ�λ

	private Handler mHandler;// ���¼����handler
	private String ReceiverString = "chengguo";

	// private Marker mMarkerA;
	// BitmapDescriptor bdA =
	// BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

	ArrayList<Overlay> overlays = new ArrayList<Overlay>();
	int i = 0;// ��ǰ���
	private SDKReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_exam);
		initLayoutMember();
		initLBS();
		// showLocations("tangtang", "chengguo");
		// initOverlay();
		checkSDK();
		// update();
	}

	private void update() {
		mHandler = new Handler();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.postDelayed(this, 2000);
				showLocations("update", ReceiverString);
			}
		});
	}

	private void checkSDK() {
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE);
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
	}

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(getApplicationContext(),
						"key ��֤����! ���� AndroidManifest.xml �ļ��м�� key ����",
						Toast.LENGTH_LONG).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(getApplicationContext(), "�������",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exam, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_vec_map:
			map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.action_rs_img:
			map.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;

		}
		return true;
	}

	private void initLBS() {
		// initial baidu map params
		examMapView = (MapView) findViewById(R.id.exam_mapview);
		examMapView.showScaleControl(false);
		map = examMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14);
		map.animateMapStatus(msu);
		map.setMyLocationEnabled(true);
		curMode = LocationMode.NORMAL;
		curMarker = null;
		curConfiguration = new MyLocationConfiguration(curMode, true, null);

		locationClient = new LocationClient(this);
		locationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
		option.setOpenGps(true); // ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);

		locationClient.setLocOption(option);
		locationClient.start();

	}

	private void showLocations(String Sender, String Receiver) {

		if (locations != null)
			locations.clear();
		try {
			// testing.
			Receiver = "chengguo";
			queryLocationThread = new QueryLocationThread(Sender, Receiver);
			queryLocationThread.start();
			queryLocationThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (queryLocationThread.res == -1) {
			Toast.makeText(this, "Server socket is closed!", Toast.LENGTH_LONG)
					.show();
		} else if (queryLocationThread.res == -2) {
			Toast.makeText(this, "Server IO is closed!", Toast.LENGTH_LONG)
					.show();
		} else {
			int num = queryLocationThread.res;
			locations = queryLocationThread.locations;
			for (int i = 0; i < num; i++) {
				// double lon=locations.get(i).Longitude;
				// double lat=locations.get(i).Latitude;
				// System.out.println(lon+lat);
				// Log.v("loc",String.valueOf(lon)+String.valueOf(lat));
				drawLocation(locations.get(i), locations.get(i).ReportType);
			}
		}

	}

	private void drawLocation(LocationClass loc, int which_icon)// 0-->help
																// 1-->wrong
																// 2-->right
	{
		Overlay mol = null;
		switch (which_icon) {
		case 0:
			mol = SetOverlay(new LatLng(loc.Latitude, loc.Longitude),
					R.drawable.help);
			// ���������źţ�������ź����ڵ�ͼ�м�����������
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(
					loc.Latitude, loc.Longitude));
			map.animateMapStatus(u);
			break;
		case 1:
			mol = SetOverlay(new LatLng(loc.Latitude, loc.Longitude),
					R.drawable.wrong);
			break;
		case 2:
			mol = SetOverlay(new LatLng(loc.Latitude, loc.Longitude),
					R.drawable.right);
			break;
		default:
			Toast.makeText(getApplicationContext(), "ERROR:�޷�ʶ���ReportType!!",
					Toast.LENGTH_LONG).show();
			break;
		}

		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				// method 1 �Զ���view
				// View view = (LinearLayout)
				// getLayoutInflater().inflate(R.layout.info_item,
				// null);
				// TextView textView=(TextView)view.findViewById(R.id.textview);
				// textView.setText(locations.get(overlays.indexOf(arg0)).Sender+"\n"+locations.get(overlays.indexOf(arg0)).ReportTimeString);

				// method 2 ֱ����textview
				TextView textView = new TextView(getApplicationContext());
				// ���maker ��ʾ��Ϣ
				textView.setText(locations.get(overlays.indexOf(arg0)).Sender
						+ "\n"
						+ locations.get(overlays.indexOf(arg0)).ReportTimeString);
				textView.setPadding(30, 20, 30, 50);
				textView.setBackgroundResource(R.drawable.corner_bg);
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						map.hideInfoWindow();// ���infowindow���ر�
					}
				});
				InfoWindow mInfoWindow = new InfoWindow(textView, arg0
						.getPosition(), 0);
				map.showInfoWindow(mInfoWindow);
				return false;
			}
		});
		overlays.add(mol);
	}

	public Overlay SetOverlay(LatLng latLng, int icon_id) {
		BitmapDescriptor mBitmapDescriptor = BitmapDescriptorFactory
				.fromResource(icon_id);
		MarkerOptions mOptions = new MarkerOptions().icon(mBitmapDescriptor)
				.position(latLng).zIndex(7);
		// .animateType(MarkerAnimateType.drop);
		Overlay overlay = map.addOverlay(mOptions);
		return overlay;

	}

	// public void initOverlay() {
	// // add marker overlay
	// LatLng llA = new LatLng(28.963175, 113.400244);
	//
	// MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
	// .zIndex(9).draggable(true);
	//
	// mMarkerA = (Marker) (map.addOverlay(ooA));
	//
	//
	// }

	private void initLayoutMember() {
		curLocButton = (Button) findViewById(R.id.exam_curloc_button);
		SwitchButton = (Button) findViewById(R.id.exam_swtloc_button);
		searchButton = (Button) findViewById(R.id.exam_btn_search);
		searchET = (EditText) findViewById(R.id.exam_et_search);
		curLocButton.setOnClickListener(this);
		SwitchButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);

		// MyLocationData locationData=new
		// MyLocationData.Builder().accuracy(location.getRadius())
		// .direction(100).latitude(location.getLatitude())
		// .longitude(location.getLongitude()).build();

		// ������ͨͼ map.setTrafficEnabled(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		examMapView.onDestroy();
		super.onDestroy();
		// ���� bitmap ��Դ
		// bdA.recycle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		examMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		examMapView.onPause();
	}

	private void go2CurLoc() {
		if (curBdLocation != null) {
			LatLng curPoint = new LatLng(curBdLocation.getLatitude(),
					curBdLocation.getLongitude());
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(curPoint);
			map.animateMapStatus(msu);
		}
		// double lon, lat;
		// float acc;
		// curLocation = locationManager
		// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
		// if (curLocation != null) {
		// acc = curLocation.getAccuracy();
		// lon = curLocation.getLongitude();
		// lat = curLocation.getLatitude();
		//
		// LatLng curPoint = new LatLng(lat, lon);
		// MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(curPoint);
		// map.setMapStatus(msu);
		//
		// map.setMyLocationEnabled(true);
		// // ���춨λ����
		// MyLocationData locData = new MyLocationData.Builder()
		// .accuracy(acc)
		// // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
		// .direction(100).latitude(lat)
		// .longitude(lon).build();
		// // ���ö�λ����
		// map.setMyLocationData(locData);
		// // ���ö�λͼ������ã���λģʽ���Ƿ���������Ϣ���û��Զ��嶨λͼ�꣩
		// curMarker =
		// BitmapDescriptorFactory.fromResource(R.drawable.logoimage);
		// LocationMode curMode;

		//
		// map.setMyLocationConfiguration();
		// // ������Ҫ��λͼ��ʱ�رն�λͼ��
		// mBaiduMap.setMyLocationEnabled(false);

		// } else {
		// Toast.makeText(this, "��δ������Ƕ�λ", Toast.LENGTH_SHORT).show();
		// }
	}

	private void switchLoc(int i) {
		if (locations != null) {

			LatLng curPoint = new LatLng(locations.get(i).Latitude,
					locations.get(i).Longitude);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(curPoint);
			map.animateMapStatus(msu);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.exam_curloc_button: {
			go2CurLoc();
			// locationClient.updateLocation(curLocation);
			break;
		}
		case R.id.exam_swtloc_button: {
			if (locations == null) {
				Toast.makeText(getApplicationContext(), "û��λ����Ϣ	",
						Toast.LENGTH_SHORT).show();
				break;
			}
			if (locations.size() == 0) {
				Toast.makeText(getApplicationContext(), "û��λ����Ϣ	",
						Toast.LENGTH_SHORT).show();
				break;
			}
			i = i + 1;
			if (i >= locations.size())
				i = 0;
			switchLoc(i);
			break;
		}
		case R.id.exam_btn_search:
			if (searchET.getText().toString() == "")
				Toast.makeText(getApplicationContext(), "������Ҫ����������",
						Toast.LENGTH_SHORT).show();
			else {
				showLocations(searchET.getText().toString(), ReceiverString);
			}
			break;
		}

	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || examMapView == null) {
				return;
			}
			curBdLocation = location;

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(location.getDirection())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			map.setMyLocationData(locData);

			// go2CurLoc();

			// LatLng ll = new LatLng(location.getLatitude(),
			// location.getLongitude());
			// MapStatus.Builder builder = new MapStatus.Builder();
			// builder.target(ll).zoom(18.0f);
			// map.animateMapStatus(MapStatusUpdateFactory
			// .newMapStatus(builder.build()));

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}

	}
}
