package com.mytv.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.mytv.model.Schedules;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String TAG = DatabaseHandler.class.getSimpleName();
	private static final int DATABASE_VERSION = 1;

	// database name
	private static final String DATABASE_NAME = "FavoriteManager";

	// table name
	private static final String TABLE_FAVORITE = "Favorite";
	// key primary
	private static final String KEY_ID = "id";

	private static final String KEY_CHANELURL = "chanelurl";
	private static final String KEY_CHANELNAME = "chanelname";
	private static final String KEY_CHANELTYPE = "chaneltype";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_FAVORITE + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY," + KEY_CHANELURL + " TEXT," + KEY_CHANELNAME + " TEXT,"
			+ KEY_CHANELTYPE + " TEXT" + ")";
	@Override
	public void onCreate(SQLiteDatabase db) {
		// table contact
		db.execSQL(CREATE_FAVORITE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
		// create new tables
		onCreate(db);
	}

	/**
	 * Create a account user
	 * 
	 * @param user
	 */
	public long createAccount(Schedules sche) {
		if (getChanel(sche.getName()) != null) {
			Log.v(TAG, "error when create new account: " + sche.getName()
					+ " was created to DB");
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CHANELURL, sche.getLogoUrl());
		values.put(KEY_CHANELNAME,sche.getName());
		values.put(KEY_CHANELTYPE,sche.getChanelType());
		
		Log.i("KeyChanel",sche.getName());
		Log.i("Chanel_Type",sche.getChanelType());
		
		return db.insert(TABLE_FAVORITE, null, values);
	}

	/**
	 * GET ALL ACCOUNT IN DATABASE
	 * 
	 * @return
	 */
	public List<Schedules> getAllChanel() {
		List<Schedules> listSche = new ArrayList<Schedules>();
		String selectQuery = "SELECT * FROM " + TABLE_FAVORITE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				Schedules sche = new Schedules();
				
				sche.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				sche.setLogoUrl(c.getString(c.getColumnIndex(KEY_CHANELURL)));
				sche.setName(c.getString(c.getColumnIndex(KEY_CHANELNAME)));
				sche.setChanelType(c.getString(c.getColumnIndex(KEY_CHANELTYPE)));
				sche.setDes(null);
				sche.setLinkList(null);
				sche.setTime(null);
				sche.setTitles(null);
				sche.setiDelete(0);
				Log.i("Key_c_type", c.getString(c.getColumnIndex(KEY_CHANELTYPE)));
				// add to list
				listSche.add(sche);
			} while (c.moveToNext());
		}
		return listSche;
	}

	// SELECT * FROM accounts WHERE email = search
	/**
	 * Get a account user
	 * 
	 * @param search
	 * @return a user
	 */
	public Schedules getChanel(String name) {
		String selectQuery = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Schedules sche = null;
		selectQuery = "SELECT * FROM " + TABLE_FAVORITE + " WHERE " + KEY_CHANELNAME
				+ " = '" + name + "'";
		if (selectQuery == "")
			return null;
		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null && c.moveToFirst()) {
			sche = new Schedules();
			sche.setId(c.getInt(c.getColumnIndex(KEY_ID)));
			sche.setLogoUrl(c.getString(c.getColumnIndex(KEY_CHANELURL)));
			sche.setName(c.getString(c.getColumnIndex(KEY_CHANELNAME)));
			sche.setChanelType(c.getString(c.getColumnIndex(KEY_CHANELTYPE)));
			sche.setLinkList(null);
			sche.setTime(null);
			sche.setTitles(null);
			sche.setDes(null);
		} else
			return null;
		return sche;
	}
	/**
	 * Update a account info
	 * 
	 * @param user
	 * @return number row affected
	 */
	
	public int updateAccountInfo(Schedules sche) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, sche.getId());
		values.put(KEY_CHANELURL, sche.getLogoUrl());
		values.put(KEY_CHANELNAME, sche.getName());
		values.put(KEY_CHANELTYPE, sche.getChanelType());
		// updating row
		return db.update(TABLE_FAVORITE, values, KEY_ID + " = ?",
				new String[] { String.valueOf(sche.getId()) });
	}

	/**
	 * Delete a user
	 * 
	 * @param user
	 */
	
	public void deleteToDo(Schedules sche) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FAVORITE, KEY_CHANELNAME + " = ?",
				new String[] { String.valueOf(sche.getName()) });
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
}