package wait.wowrss;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RssDB implements Idb {

	private static final String DB_NAME = "verydb";
	private static final int DB_VERSION = 1;
	private static final String DB_TABLE = "muchtable";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_DESCR = "_descr";
	public static final String COLUMN_DATE = "_date";
	public static final String COLUMN_LINK = "_link";
	public static final String COLUMN_PROVIDER = "_provider";
	
	private static final String DB_CREATE = 
			"create table " + DB_TABLE + "(" + 
			COLUMN_ID 		+ " integer primary key autoincrement, "  +
			COLUMN_TITLE 	+ " text, " +
			COLUMN_DESCR 	+ " text, " +
			COLUMN_DATE 	+ " text, " +
			COLUMN_LINK 	+ " text, " +
			COLUMN_PROVIDER	+ " text" +");";
	
	private Context ctx;
	
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	
	public RssDB(Context ctx)
	{
		this.ctx = ctx;
	}
	
	public void open()
	{
		dbHelper = new DBHelper (ctx, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		if(dbHelper != null) dbHelper.close();
	}
	
	public Cursor getAllData()
	{
		return db.query(DB_TABLE, null, null, null, null, null, null);
	}
	
	public Cursor getDataProvider(String provider)
	{
		String selection =  COLUMN_PROVIDER + " = ?";
		String selectionArg[] = new String[]{provider};
		return db.query(DB_TABLE, null, selection, selectionArg, null, null, null);
//		return db.rawQuery("SELECT * FROM " + DB_TABLE + " WHERE " + COLUMN_PROVIDER + " = '" + provider + "'", null);
		
	}
	
	public void addRec(RssItem item)
	{
		ContentValues cv = new ContentValues();
		
		cv.put(COLUMN_TITLE, 	item.getTitle());
		cv.put(COLUMN_DESCR, 	item.getDescription());
		cv.put(COLUMN_DATE, 	item.getPubDate().toGMTString());
		cv.put(COLUMN_LINK, 	item.getLink());
		cv.put(COLUMN_PROVIDER,	item.getProvider());
		db.insert(DB_TABLE, null, cv);
	}
	
	public void delTable()
	{
		db.delete(DB_TABLE, null, null);
	}
	
	public void delRec (long id)
	{
		db.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
	}
	
	private class DBHelper extends SQLiteOpenHelper
	{

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
			
			ContentValues cv = new ContentValues();
			
			cv.put(COLUMN_TITLE, 	"No news");
			cv.put(COLUMN_DESCR, 	"There are no news in your feed. Please, press 'Resfresh' in action bar ");
			cv.put(COLUMN_DATE, 	" ");
			cv.put(COLUMN_LINK, 	" ");
			cv.put(COLUMN_PROVIDER,	" ");
			db.insert(DB_TABLE, null, cv);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void addRec() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delRec() {
		// TODO Auto-generated method stub
		
	}
}
