package com.candystudio.android.dxzs.teacher.activity;

import org.xbill.DNS.tests.primary;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.candystudio.android.dxzs.teacher.database.DataBaseManager;
import com.candystudio.android.dxzs.teacher.position.Converter;
import com.candystudio.android.dxzs.teacher.service.LocationService;
import com.candystudio.android.dxzs.teacher.ultility.LogUtil;
import com.candystudio.android.dxzs.teacher.app.R;

public class StandardActivity extends Activity implements OnClickListener {
	EditText editTextX;
	EditText editTextY;
	EditText editTextMapName;
	EditText editTextRegionName;
	EditText editTextPointName;
	EditText editTextPointNumber;
	EditText editTextRouteNumber;
	EditText editTextPointMsg;

	Button buttonGetPosition;
	Button buttonSave;
	Button buttonQuery;
	
	private static String INSERT_STANDARD_STR=
			"insert into test_standard "
			+ "(pointinfo,mapname,regionname,routenumber,"
			+ "pointnumber,pointname,pointmsg,x2000,y2000) "
			+ "values(?,?,?,?,?,?,?,?,?)";

	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_standard);
		editTextX = (EditText) findViewById(R.id.editTextX2000);
		editTextY = (EditText) findViewById(R.id.editTextY2000);
		editTextMapName = (EditText) findViewById(R.id.editTextMapName);
		editTextRegionName = (EditText) findViewById(R.id.editTextRegionName);
		editTextPointName = (EditText) findViewById(R.id.editTextPointName);
		editTextRouteNumber = (EditText) findViewById(R.id.editTextRouteNumber);
		editTextPointNumber = (EditText) findViewById(R.id.editTextPointNumber);
		editTextPointMsg = (EditText) findViewById(R.id.editTextPointMsg);

		buttonGetPosition = (Button) findViewById(R.id.button_get_position);
		buttonSave = (Button) findViewById(R.id.button_save);
		buttonQuery = (Button) findViewById(R.id.button_query);

		buttonGetPosition.setOnClickListener(this);
		buttonSave.setOnClickListener(this);
		buttonQuery.setOnClickListener(this);
		super.onCreate(savedInstanceState);

	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("GPS");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	class getLocationTask extends AsyncTask<Void, Boolean, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			while (LocationService.getLocationStatu() == false
					|| LocationService.getLocation() == null) {

			}

			return true;

		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {

				// StandardActivity.this.location=LocationService.getLocation();
				int x, y;
				Converter converter = new Converter();
				converter.Convert_84GEOto2000PLAT(LocationService.getLocation()
						.getLongitude(), LocationService.getLocation()
						.getLatitude(), LocationService.getLocation()
						.getAltitude());
				x = converter.getPx2000();
				y = converter.getPy2000();
				editTextX.setText(String.valueOf(x));
				editTextY.setText(String.valueOf(y));
				closeProgressDialog();
			}
		}

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.button_get_position:
			new getLocationTask().execute();
			break;
		case R.id.button_save:
			saveStandardPoint();
			break;
		case R.id.button_query:
			Intent intent = new Intent(StandardActivity.this,
					InfoActivity.class);
			intent.putExtra("action", "standard");
			startActivity(intent);
			break;
		default:
			break;
		}

	}
	
	private void saveStandardPoint() {
				String mapName,	regionName,	pointName, pointMsg, pointInfo;
				int routeNumber,	pointNumber;
				int x2000,	y2000;
				try {
					mapName = editTextMapName.getText().toString();
					regionName = editTextRegionName.getText().toString();
					pointName = editTextPointName.getText().toString();
					pointMsg = editTextPointMsg.getText().toString();

					routeNumber = Integer.valueOf(editTextRouteNumber.getText()
							.toString());
					pointNumber = Integer.valueOf(editTextPointNumber.getText()
							.toString());

					x2000 = Integer.valueOf(editTextX.getText().toString());
					y2000 = Integer.valueOf(editTextY.getText().toString());
					pointInfo = mapName + "-" + regionName + "-"
							+ String.valueOf(routeNumber) + "-"
							+ String.valueOf(pointNumber);

				} catch (NumberFormatException e) {
				

					e.printStackTrace();
					return;
				}

				try {

					SQLiteDatabase db = DataBaseManager.getDb();
					Cursor cursor = db.rawQuery(
							"select * from standard where pointinfo = ?",
							new String[] { pointInfo });
					if (!cursor.moveToFirst()) {
						db.execSQL(INSERT_STANDARD_STR,
								new Object[] { pointInfo, mapName, regionName,
										routeNumber, pointNumber, pointName,
										pointMsg, x2000, y2000 });
						
					} else {
					
					}
//					cursor.moveToFirst();
//					int result = cursor.getInt(cursor.getColumnIndex("id"));
//					LogUtil.i("zzh", "" + result);
//					LogUtil.i("zzh", "数据更新");
//					db.execSQL("update standard set x=?,y=?,msg=? where id=?",
//							new Object[] { x, y, msg, ID });

				} catch (Exception e) {

				}
	}


}
