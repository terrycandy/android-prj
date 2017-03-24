package com.candystudio.android.dxzs.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.candystudio.android.dxzs.teacher.database.DataBaseManager;
import com.candystudio.android.dxzs.teacher.examination.Grade;
import com.candystudio.android.dxzs.teacher.examination.Info;
import com.candystudio.android.dxzs.teacher.examination.Standard;
import com.candystudio.android.dxzs.teacher.position.Converter;
import com.candystudio.android.dxzs.teacher.ultility.LogUtil;
import com.candystudio.android.dxzs.teacher.app.R;

public class InfoActivity extends Activity {
	private ListView mListView;
	private SQLiteDatabase mDB;
	private String mAction;
	private List<Grade> grades;
	List<Info> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_info);
		mListView = (ListView) findViewById(R.id.listview_info);
		mDB = new DataBaseManager().getDb();
		Intent intent = getIntent();
		mAction = intent.getStringExtra("action");
		if (mAction.equals("info")) {
			InfoPrepare();
		} else if (mAction.equals("grade")) {
			gradePrepare();
		} else if (mAction.equals("person")) {
			personPrepare(intent.getStringExtra("username"));
		} else if (mAction.equals("standard")) {
			standardPrepare();
		}

		super.onCreate(savedInstanceState);
	}

	private void standardPrepare() {
		List<Standard> standards = new ArrayList<Standard>();
		StandardAdapter standardAdapter = new StandardAdapter(this,
				R.layout.info_item, standards);
		mListView.setAdapter(standardAdapter);
		Cursor cursor = mDB.rawQuery("select * from standard", null);
		if (cursor.moveToFirst()) {
			LogUtil.i("zzh", "获取到数据库信息");
			do {
				Standard standard = new Standard();
				standard.setX(cursor.getInt(cursor.getColumnIndex("x")));
				standard.setY(cursor.getInt(cursor.getColumnIndex("y")));
				standard.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
				standard.setMap(cursor.getInt(cursor.getColumnIndex("map")));
				standard.setRoute(cursor.getInt(cursor.getColumnIndex("route")));
				standard.setNumber(cursor.getInt(cursor
						.getColumnIndex("number")));

				standards.add(standard);

			} while (cursor.moveToNext());
			standardAdapter.notifyDataSetChanged();
		}

	}

	private void gradePrepare() {
		grades = new ArrayList<Grade>();
		GradeAdapter gradeAdapter = new GradeAdapter(this, R.layout.info_item,
				grades);
		mListView.setAdapter(gradeAdapter);
		Cursor cursor = mDB.rawQuery("select * from grade", null);
		if (cursor.moveToFirst()) {
			LogUtil.i("zzh", "获取到数据库信息");
			do {
				Grade grade = new Grade();
				grade.setGrade(cursor.getInt(cursor.getColumnIndex("grade")));
				grade.setUsername(cursor.getString(cursor
						.getColumnIndex("username")));
				grades.add(grade);

			} while (cursor.moveToNext());
			gradeAdapter.notifyDataSetChanged();
		}

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Grade grade = grades.get(position);
				String username = grade.getUsername();
				LogUtil.i("zzh", username);
				Intent intent = new Intent(InfoActivity.this,
						InfoActivity.class);
				intent.putExtra("action", "person");
				intent.putExtra("username", username);
				startActivity(intent);
			}
		});
	}

	private void personPrepare(String username) {
		infos = new ArrayList<Info>();
		InfoAdapter infoAdapter = new InfoAdapter(this, R.layout.info_item,
				infos);
		mListView.setAdapter(infoAdapter);
		Cursor cursor = mDB.rawQuery("select * from info where username=?",
				new String[] { username });
		if (cursor.moveToFirst()) {
			LogUtil.i("zzh", "获取到数据库信息");
			do {
				

			} while (cursor.moveToNext());
			infoAdapter.notifyDataSetChanged();
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// MainActivity.getmBaiduMap().clear();
				for (Info info : infos) {
					CoordinateConverter converter = new CoordinateConverter();
					converter.from(CoordType.GPS);
					// sourceLatLng待转换坐标

					converter.coord(new LatLng(info.getLatitude(), info
							.getLongitude()));
					LatLng desLatLng = converter.convert();
					LogUtil.i("zzh", "转换后的坐标：" + desLatLng.latitude + " "
							+ desLatLng.longitude);

					// LatLng desLatLng=new LatLng(latitude,
					// longitude);
					OverlayOptions textOption = new TextOptions()
							.bgColor(0xAAFFFF00).fontSize(30)
							.fontColor(0xFFFF00FF).text(info.getiDNumber())
							.position(desLatLng);
					// 在地图上添加该文字对象并显示
					MainActivity.getmBaiduMap().addOverlay(textOption);

				}

				finish();
				Intent intent = new Intent(InfoActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

	}

	private void InfoPrepare() {
		List<Info> infos = new ArrayList<Info>();
		InfoAdapter infoAdapter = new InfoAdapter(this, R.layout.info_item,
				infos);
		mListView.setAdapter(infoAdapter);
		try {
			Cursor cursor = mDB.rawQuery("select * from test_info", null);
			
			if (cursor.moveToFirst()) {
				do {				
					String iDNumber;
					String pointInfo;
					String dateStr; // hh:mm:ss
					String timeStr;
					int x2000;
					int y2000;
					double lon;
					double lat;
					String pointMsg;
					iDNumber = cursor.getString(cursor.getColumnIndex("idnumber"));
					pointInfo = cursor
							.getString(cursor.getColumnIndex("pointinfo"));
					dateStr = cursor.getString(cursor.getColumnIndex("datestr"));
					timeStr = cursor.getString(cursor.getColumnIndex("timestr"));
					x2000 = cursor.getInt(cursor.getColumnIndex("x2000"));
					y2000 = cursor.getInt(cursor.getColumnIndex("y2000"));
					lon=cursor.getDouble(cursor.getColumnIndex("longtitude"));
					lat=cursor.getDouble(cursor.getColumnIndex("latitude"));
					pointMsg = cursor.getString(cursor.getColumnIndex("pointmsg"));

					Info info = new Info(iDNumber, pointInfo, dateStr, timeStr,
							x2000, y2000, lon,lat,pointMsg);
					infos.add(info);				
				} while (cursor.moveToNext());
				infoAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, e.getMessage(), 5000).show();
		
		}
		
		
	}

	public class InfoAdapter extends ArrayAdapter<Info> {
		private int resourceId;

		public InfoAdapter(Context context, int resource, List<Info> objects) {

			super(context, resource, objects);
			resourceId = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//chengguo: 注释，未完成。
//			Info info = getItem(position);
//			View view = LayoutInflater.from(getContext()).inflate(resourceId,
//					null);
//			TextView textView = (TextView) view.findViewById(R.id.textview);
//			int x = 0, y = 0;
//			Converter converter = new Converter();
//			converter.Convert_84GEOto2000PLAT(info.getLongitude(),
//					info.getLatitude(), info.getAltitude());
//			x = converter.getPx2000();
//			y = converter.getPy2000();
//			textView.setText("ID:" + info.getUsername() + " " + "Time:"
//					+ info.getDate() + "\n" + "x:" + x + "\n" + "y:" + y + "\n"
//					+ "message:" + info.getMsg() + "    route:" + info.getMap()
//					+ ":" + info.getRoute() + ":" + info.getNunber());
//			return view;
			return null;
		}
	}

	public class GradeAdapter extends ArrayAdapter<Grade> {
		private int resourceId;

		public GradeAdapter(Context context, int resource, List<Grade> objects) {

			super(context, resource, objects);
			resourceId = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Grade grade = getItem(position);
			View view = LayoutInflater.from(getContext()).inflate(resourceId,
					null);
			TextView textView = (TextView) view.findViewById(R.id.textview);
			textView.setText("ID:" + grade.getUsername() + "  " + "grade:"
					+ grade.getGrade());
			return view;
		}
	}

	public class StandardAdapter extends ArrayAdapter<Standard> {
		private int resourceId;

		public StandardAdapter(Context context, int resource,
				List<Standard> objects) {

			super(context, resource, objects);
			resourceId = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Standard standard = getItem(position);
			View view = LayoutInflater.from(getContext()).inflate(resourceId,
					null);
			TextView textView = (TextView) view.findViewById(R.id.textview);
			textView.setText("Route: " + standard.getMap() + ":"
					+ standard.getRoute() + ":" + standard.getNumber()
					+ "    message:" + standard.getMsg() + "\n" + "x:"
					+ standard.getX() + "\n" + "y:" + standard.getY());
			return view;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.infomenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		SQLiteDatabase db = DataBaseManager.getDb();
		switch (id) {
		case R.id.id_clear_grade:

			db.execSQL("delete from grade");
		
			finish();
			break;
		case R.id.id_clear_standard:
			db.execSQL("delete from standard");

			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
