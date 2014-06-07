package com.example.first;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseAdapter {
	
	private Context mContext = null;
	private SQLiteDatabase mSQLiteDataBase = null;
	private DataBaseHelper mDataBaseHelper = null;
	
	private static class DataBaseHelper extends SQLiteOpenHelper {
		DataBaseHelper(Context context) {
			super(context, "mDataBase.db", null, 13);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("create table if not exists entry_kind "
					+ "(kind_id integer primary key autoincrement,"
					+ "kind_text text)");
			
			db.execSQL("create table if not exists entry "
					+ "(entry_id integer primary key autoincrement,"
					+ "kind_text text,"
					+ "entry_text text,"
					+ "emergency integer,"
					+ "voicepath text,"
					+ "deadline text)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists entry_kind");
			db.execSQL("drop table if exists entry");
			onCreate(db);
		}
	}
	
	public MyDataBaseAdapter(Context context) {
		mContext = context;
	}
	
	public void open() throws SQLException {
		mDataBaseHelper = new DataBaseHelper(mContext);
		mSQLiteDataBase = mDataBaseHelper.getWritableDatabase();
	}
	
	public void close() {
		mDataBaseHelper.close();
	}
	
	public long insertIntoKind(String kind_text) {
		ContentValues cv = new ContentValues();
		cv.put("kind_text", kind_text);
		return mSQLiteDataBase.insert("entry_kind", null, cv);
	}
	
	public long insertIntoEntry(String kind_text, String entry_text,
			int emergency, String voicePath, String deadline) {
		ContentValues cv = new ContentValues();
		cv.put("kind_text", kind_text);
		cv.put("entry_text", entry_text);
		cv.put("emergency", emergency);
		cv.put("voicepath", voicePath);
		cv.put("deadline", deadline);
		return mSQLiteDataBase.insert("entry", null, cv);
	}
	
	public void deleteFromKind(long position) {
		mSQLiteDataBase.execSQL("delete from entry_kind where kind_id=" + position);
	}
	
	public void deleteFromEntry(String position) {
		mSQLiteDataBase.execSQL("delete from entry where entry_id=" + position);
	}
	
	public Cursor queryFromKind(long position) {
		return mSQLiteDataBase.query("entry_kind",
				new String[] {"kind_id", "kind_text"}, "kind_id=" + position, null, null, null, null, null);
	}
	
	public Cursor queryFromEntry(String position) {
		return mSQLiteDataBase.query("entry",
				new String[] {"entry_id", "kind_text", "entry_text", "emergency", "voicepath", "deadline"},
				"entry_id=" + position,
				null, null, null, null, null);
	}

	public Cursor queryAllFromKind() {
		return mSQLiteDataBase.query("entry_kind",
				new String[] {"kind_id", "kind_text"}, null, null, null, null, null);
	}
	
	public Cursor queryAllFromEntry() {
		return mSQLiteDataBase.query("entry",
				new String[] {"entry_id", "kind_text", "entry_text", "emergency", "voicepath", "deadline"},
				null, null, null, null, null);
	}

	public void updateFromEntry(long position, String entry_text, String kind_text,
			String deadline, String voicePath, int emergency) {
		ContentValues cv = new ContentValues();
		cv.put("entry_text", entry_text);
		cv.put("kind_text", kind_text);
		cv.put("deadline", deadline);
		cv.put("voicepath", voicePath);
		cv.put("emergency", emergency);
		mSQLiteDataBase.update("entry", cv, "entry_id=" + position, null);
	}
}
