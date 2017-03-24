package com.candystudio.android.dxzs.teacher.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String CREATE_USERS = "create table users("
			+ "idnumber text primary key," 
			+ "password text,"
			+ "username text," 
			+ "classnumber text)";

	public static final String CREATE_POINT_LIB = "create table point_lib("
			+ "mapname text," 
			+ "latitude real," 
			+ "longitude real,"
			+ "altitude real," 
			+ "x2000 integer," 
			+ "y2000 integer,"
			+ "thing text," 
			+ "message text)";

	public static final String CREATE_STANDARD_LIB = "create table standard_lib("
			+ "mapname text,"
			+ "regionname text,"
			+ "datestr text,"
			+ "routenumber integer,"
			+ "pointnumber integer,"
			+ "latitude real,"
			+ "longitude real,"
			+ "altitude real," 
			+ "x2000 integer,"
			+ "y2000 integer," 
			+ "thing text," 
			+ "message text)";

	public static final String CREATE_ANSWERS = "create table answers("
			+ "idnumber text," 
			+ "answerstr text)";

	public static final String CREATE_GRADES = "create table grades("
			+ "idnumber text," 
			+ "username text,"
			+ "classnumber text,"
			+ "datestr text," 
			+ "grade integer)";

	/*
	 * public static final String CREATE_SCORE="create table isgetscore("
	 * +"username text ," +"id integer" +"getscore integer)";
	 */

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USERS);
		db.execSQL(CREATE_POINT_LIB);
		db.execSQL(CREATE_STANDARD_LIB);
		db.execSQL(CREATE_ANSWERS);
		db.execSQL(CREATE_GRADES);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
