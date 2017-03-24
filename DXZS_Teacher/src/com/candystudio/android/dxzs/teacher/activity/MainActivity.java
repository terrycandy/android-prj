package com.candystudio.android.dxzs.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.candystudio.android.dxzs.teacher.database.DataBaseManager;
import com.candystudio.android.dxzs.teacher.position.Converter;
import com.candystudio.android.dxzs.teacher.service.ListenMessageService;
import com.candystudio.android.dxzs.teacher.service.LocationService;
import com.candystudio.android.dxzs.teacher.service.ListenMessageService.MyMessageListener;
import com.candystudio.android.dxzs.teacher.service.LocationService.GPSLocationListener;
import com.candystudio.android.dxzs.teacher.ultility.LogUtil;
import com.candystudio.android.dxzs.teacher.ultility.MyOrientationListener;
import com.candystudio.android.dxzs.teacher.ultility.MyOrientationListener.OnOrientationListener;
import com.candystudio.android.dxzs.teacher.app.R;

public class MainActivity extends Activity {
	private MapView mMapView = null;
	private static BaiduMap mBaiduMap;
	private boolean isfirstin = true;
	private Double latitude = 28.2;
	private Double longitude = 113.04;
	private Double altitude = 50.0;
	private float mCurrentX;
	private MyOrientationListener mMyOrientationListener;
	// 定位相关
	private LocationManager mLocationManager;
	private LocationClient mLocationClient;
	protected final LocationListener mLocationListener = new LocationListener() {

		// 当位置发生变化时，输出位置信息
		public void onLocationChanged(Location location) {
			showLocation(location);
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	};

	private LocationMode mLocationMode;
	private LocationService mLocationService;
	private Location curLocation;

	private TextView mTextView;
	private TextView mTextViewGps;
	List<String> come;

	String username;

	Converter converter;

	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		// 初始化百度地图
		InitView();
		// 初始化百度定位参数
		// InitLocation();

		// 获取用户名
//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//		username = bundle.getString("username");

//		mTextViewGps = (TextView) findViewById(R.id.textView_gps);

		// 初始化坐标转换器
		converter = new Converter();
		// come = new ArrayList<String>();

		// mLocationService=new LocationService();
		// Intent serviceIntent=new Intent(this,LocationService.class);
		// startService(serviceIntent);
		//
		// LocationService.actionStart(this, new GPSLocationListener() {
		//
		// @Override
		// public void OnGetLocation(Location location) {
		// if (location != null) {
		// int x = 0;
		// int y = 0;
		// // 将84经纬坐标转换成2000平面坐标
		// converter.Convert_84GEOto2000PLAT(location.getLongitude(),
		// location.getLatitude(), location.getAltitude());
		// x = converter.getPx2000();
		// y = converter.getPy2000();
		// mTextViewGps.setText("WGS84：" + location.getLongitude()
		// + " " + location.getLatitude() + "\n" + "CGCS2000："
		// + x + " " + y);
		// }
		//
		// }
		// });

		// 登陆任务
		// new LoginTask().execute("teacher", "teacher");

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		curLocation = mLocationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		showLocation(curLocation);

		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 1, mLocationListener);

	}

	private void showLocation(Location CurLocation) {
		converter.Convert_84GEOto2000PLAT(CurLocation.getLongitude(),
				CurLocation.getLatitude(), CurLocation.getAltitude());
		int x = converter.getPx2000();
		int y = converter.getPy2000();
		mTextViewGps.setText("WGS84：" + CurLocation.getLongitude() + " "
				+ CurLocation.getLatitude() + "\n" + "CGCS2000：" + x + " " + y);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// mBaiduMap.setMyLocationEnabled(true);
		// if (!mLocationClient.isStarted()) {
		// mLocationClient.start();
		// }
		// mMyOrientationListener.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
		// mBaiduMap.setMyLocationEnabled(false);
		// mLocationClient.stop();
		// mMyOrientationListener.stop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
		// stopService(new Intent(this, ListenMessageService.class));
		// stopService(new Intent(this, LocationService.class));
		// ConnectionTool.closeConnection();

	}

	// 百度地图view初始化
	private void InitView() {

		mMapView = (MapView) findViewById(R.id.id_bmapView);
		mBaiduMap = mMapView.getMap();
		LatLng latLng = new LatLng(latitude, longitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);

	}

	// 百度地图定位初始化
	private void InitLocation() {

		mLocationMode = mLocationMode.NORMAL;
		// mLocationClient = new LocationClient(this);
		// mLocationListener = new MyLocationListener();
		// mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		// mIconDirection=BitmapDescriptorFactory.fromResource(R.drawable.direction);
		mMyOrientationListener = new MyOrientationListener(this);
		mMyOrientationListener
				.setmOnOrientationListener(new OnOrientationListener() {

					@Override
					public void OnOrientationChanged(float x) {
						mCurrentX = x;
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.id_map_comcom:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.id_map_traffic:
			if (mBaiduMap.isTrafficEnabled()) {
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通（off）");
			} else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通（on）");
			}
			break;
		case R.id.id_map_back:
			LatLng latLng = new LatLng(latitude, longitude);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.animateMapStatus(msu);
			break;
		case R.id.id_normal:
			mLocationMode = mLocationMode.NORMAL;
			break;
		case R.id.id_following:
			mLocationMode = mLocationMode.FOLLOWING;
			break;
		case R.id.id_campass:
			mLocationMode = mLocationMode.COMPASS;
			break;
		case R.id.id_info:
			Intent intent = new Intent(MainActivity.this, InfoActivity.class);
			intent.putExtra("action", "info");
			startActivity(intent);
			break;
		case R.id.id_grade:
			Intent intent2 = new Intent(MainActivity.this, InfoActivity.class);
			intent2.putExtra("action", "grade");
			startActivity(intent2);
			break;
		case R.id.id_standard:
			Intent intent3 = new Intent(MainActivity.this,
					StandardActivity.class);
			startActivity(intent3);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("连接中...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	class LoginTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				closeProgressDialog();
				Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_LONG)
						.show();
				ListenMessageService.actionStart(MainActivity.this,
						new MyMessageListener() {

							@Override
							public void OnMessageListener(Message message) {
								LogUtil.i("zzh", message.toXML());
								//
								Double _longitude = (Double) message
										.getProperty("longitude");
								Double _latitude = (Double) message
										.getProperty("latitude");
								Double _altitude = (Double) message
										.getProperty("altitude");

								int map = (Integer) message.getProperty("map");
								int route = (Integer) message
										.getProperty("Route");
								int number = (Integer) message
										.getProperty("Number");
								String msg = (String) message
										.getProperty("Msg");
								String username = (String) message
										.getProperty("username");
								LogUtil.i("zzh", "收到的信息：" + _latitude + " "
										+ _longitude + " " + msg);
								// message.getProperty("Data");
								//
								// message.getProperty("Number");
								// message.getProperty("Route" );
								SQLiteDatabase db = DataBaseManager.getDb();
								int ID = map * 1000 + route * 10 + number;

								Cursor cursor = db
										.rawQuery(
												"select x,y,msg from standard where id=?",
												new String[] { String
														.valueOf(ID) });

								int stdx = 0, stdy = 0;
								String stdMsg = "";
								try {
									cursor.moveToFirst();
									stdx = cursor.getInt(cursor
											.getColumnIndex("x"));
									stdy = cursor.getInt(cursor
											.getColumnIndex("y"));
									stdMsg = cursor.getString(cursor
											.getColumnIndex("msg"));
									LogUtil.i("zzh", "考核标准：" + stdx + " "
											+ stdy + " " + stdMsg);
								} catch (Exception e1) {
									LogUtil.i("zzh", "未从数据库中得到考核标准");
									e1.printStackTrace();
								}
								Converter _convert = new Converter();
								_convert.Convert_84GEOto2000PLAT(_longitude,
										_latitude, _altitude);
								if (Math.abs(stdx - _convert.getPx2000()) < 50
										&& Math.abs(stdy - _convert.getPy2000()) < 50
										&& stdMsg.equals(msg)) {
									LogUtil.i("zzh", "得分");
									Cursor cursor2 = db
											.rawQuery(
													"select * from grade where username=?",
													new String[] { username });
									try {
										cursor2.moveToFirst();
										String result = cursor2.getString(0);
										int grade = cursor2.getInt(1);
										LogUtil.i("zzh", "数据库中得到" + result
												+ " " + grade);
										if (!come.contains(ID + username)) {
											LogUtil.i("zzh", "数据更新");
											db.execSQL(
													"update grade set grade=? where username=?",
													new String[] {
															String.valueOf(grade + 5),
															username });
											come.add(ID + username);

										}

									} catch (Exception e) {
										if (!come.contains(ID + username)) {
											LogUtil.i("zzh", "数据插入");
											db.execSQL(
													"insert into grade (username,grade) values(?,?)",
													new Object[] { username,
															"5" });
											e.printStackTrace();
											come.add(ID + username);
										}

									}
								} else {
									LogUtil.i("zzh", "未得分");
									Cursor cursor3 = db
											.rawQuery(
													"select * from grade where username=?",
													new String[] { username });
									try {
										cursor3.moveToFirst();
										String result = cursor3.getString(0);
										int grade = cursor3.getInt(1);
										LogUtil.i("zzh", "数据库中得到" + result
												+ " " + grade);

										/*
										 * LogUtil.i("zzh", "数据更新"); db.execSQL(
										 * "update grade set grade=? where username=?"
										 * , new String[] { String.valueOf(grade
										 * + 5), username });
										 */
									} catch (Exception e) {
										LogUtil.i("zzh", "数据插入");
										db.execSQL(
												"insert into grade (username,grade) values(?,?)",
												new Object[] { username, "0" });
										e.printStackTrace();
									}

								}

								db.execSQL(
										"insert into info(latitude,longitude,altitude,date,msg,map,route,number,username) values(?,?,?,?,?,?,?,?,?)",
										new Object[] {

												_latitude,
												_longitude,
												_altitude,
												(String) message
														.getProperty("Data"),
												(String) message
														.getProperty("Msg"),
												(Integer) message
														.getProperty("map"),
												(Integer) message
														.getProperty("Route"),
												(Integer) message
														.getProperty("Number"),
												(String) message
														.getProperty("username") });

								CoordinateConverter converter = new CoordinateConverter();
								converter.from(CoordType.GPS);
								// sourceLatLng待转换坐标

								converter.coord(new LatLng(_latitude,
										_longitude));
								LatLng desLatLng = converter.convert();
								LogUtil.i("zzh", "转换后的坐标：" + desLatLng.latitude
										+ " " + desLatLng.longitude);

								// LatLng desLatLng=new LatLng(latitude,
								// longitude);
								// 构建Marker图标
								BitmapDescriptor bitmap = BitmapDescriptorFactory
										.fromResource(R.drawable.postion);
								// 构建MarkerOption，用于在地图上添加Marker
								OverlayOptions option = new MarkerOptions()
										.position(desLatLng).icon(bitmap);
								// 在地图上添加Marker，并显示
								mBaiduMap.addOverlay(option);

							}
						});
			} else {
				closeProgressDialog();
				Toast.makeText(MainActivity.this, "登陆失败", 1000).show();
			}
		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean loginSuccess = false;
//			loginSuccess = ConnectionMethod.Login(params[0], params[1]);
			return loginSuccess;

		}
	}

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder()
					.direction(mCurrentX).accuracy(location.getRadius())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(data);
			MainActivity.this.latitude = location.getLatitude();
			MainActivity.this.longitude = location.getLongitude();
			MainActivity.this.altitude = location.getAltitude();
			// int x1;
			// int y1;
			// converter.Convert_84GEOto2000PLAT(longitude, latitude, altitude);
			// x1 = converter.getPx2000();
			// y1 = converter.getPy2000();
			// mTextView.setText("百度坐标：" + latitude + " " + longitude + "\n"
			// + "百度坐标转换：" + x1 + " " + y1);
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, null);
			mBaiduMap.setMyLocationConfigeration(config);

			if (isfirstin) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isfirstin = false;
			}

		}

	}

	public static BaiduMap getmBaiduMap() {
		return mBaiduMap;
	}

}
