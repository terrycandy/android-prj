package com.candystudio.android.dxzs.teacher.database;

//import android.R;
import android.database.sqlite.SQLiteDatabase;

import com.candystudio.android.dxzs.teacher.app.R;

public class DataBaseManager {

	private static DatabaseHelper databaseHelper = null;
	private static SQLiteDatabase db = null;
	private static String databaseName;

	public DataBaseManager() {
		if (databaseName == null) {
//			databaseName = MyApplication.getContext().getResources()
//					.getString(R.string.database_name);
		}
		if (databaseHelper == null) {
//			databaseHelper = new DatabaseHelper(MyApplication.getContext(),
//					databaseName, null, 1);
		}

		if (db == null) {
			db = databaseHelper.getWritableDatabase();
		}

	}

	public static SQLiteDatabase getDb() {
		return db;
	}

}
