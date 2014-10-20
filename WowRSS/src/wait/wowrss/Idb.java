package wait.wowrss;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public interface Idb {

	String DB_NAME = null;
	int DB_VERSION = 1;
	String DB_TABLE = null;
	
	
	Context ctx = null;
	
	SQLiteDatabase db = null;
	
	public void open();
	
	public void close();
	
	public Cursor getAllData();
	
	public void addRec();
	
	public void delTable();
	
	public void delRec ();
}
