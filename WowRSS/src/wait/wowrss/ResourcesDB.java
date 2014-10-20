package wait.wowrss;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ResourcesDB implements Idb {
	private static final String DB_NAME = "muchdb";
	private static final int DB_VERSION = 1;
	private static final String DB_TABLE = "sotable";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_RES_NAME = "_title";
	public static final String COLUMN_RES_LINK = "_link";
	
	private static final String DB_CREATE = 
			"create table " + DB_TABLE + "(" + 
			COLUMN_ID 		+ " integer primary key autoincrement, "  +
			COLUMN_RES_NAME	+ " text, " +
			COLUMN_RES_LINK	+ " text" + ");";
	
	private Context ctx;
	
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	
	public ResourcesDB(Context ctx)
	{
		this.ctx = ctx;
//		create();
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
	
	public String getLink(int pos)
	{
		Cursor c = getAllData();
		c.moveToPosition(pos);
		return c.getString(c.getColumnIndex(COLUMN_RES_LINK));
	}
	
	public String getName(int pos)
	{
		Cursor c = getAllData();
		c.moveToPosition(pos);
		return c.getString(c.getColumnIndex(COLUMN_RES_NAME));
	}
	
	public void addRec(ProviderItem pi)
	{
		ContentValues cv = new ContentValues();
		
		cv.put(COLUMN_RES_NAME, 	pi.getName());
		cv.put(COLUMN_RES_LINK, 	pi.getUrl());
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
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);	
			
			ContentValues cv = new ContentValues();
			
			cv.put(COLUMN_RES_NAME, 	"all");
			cv.put(COLUMN_RES_LINK, 	"all");
			db.insert(DB_TABLE, null, cv);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}

	@Override
	public void addRec() {
	}

	@Override
	public void delRec() {
	}
}
