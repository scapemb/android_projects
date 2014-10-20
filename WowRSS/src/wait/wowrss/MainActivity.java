package wait.wowrss;

import java.util.ArrayList;
import java.util.Iterator;
import wait.wowrss.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

public class MainActivity extends FragmentActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, LoaderCallbacks<Cursor> {


	private static ArrayList <RssItem> RSS = new ArrayList<RssItem>();
	private static ArrayList <ProviderItem> Providers = new ArrayList<ProviderItem>();
	private RssDB rssDB;
	private ResourcesDB resourcesDB;
	private Cursor c;
	private static SimpleCursorAdapter scAdapter;
	private static ListView listView;
	
	private static String currentFeedProvider = "all";
	private int currentDrawerPosition = 0;
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(rssDB == null)
		{
			rssDB = new RssDB(this);
			rssDB.open();
		}
		
		String[] from = new String[]{RssDB.COLUMN_TITLE, RssDB.COLUMN_DESCR, RssDB.COLUMN_DATE};
		int[] to = new int[]{R.id.textTitle, R.id.textDescription, R.id.textPubDate};
		
		scAdapter = new SimpleCursorAdapter(this, R.layout.item_layout, null, from, to, 0);
		
		getSupportLoaderManager().initLoader(0, null, this);
		
		PlaceholderFragment.setContext(getApplicationContext());
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
//		Log.v("lol","choose "+position);
		try
		{
			//Log.v("lol","choose " + resourcesDB.getName(position));
			currentFeedProvider = resourcesDB.getName(position);
			getSupportLoaderManager().getLoader(0).forceLoad();
		}
		catch(Exception e)
		{
			
		}
		currentDrawerPosition = position;
	}

	public void onSectionAttached(int number) {
		
			//mTitle = resourcesDB.getName(number);
			
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			rssDB.delTable();
			new DownloadRSSTask().execute();
		}
		else if (id == R.id.action_delete) {
			rssDB.delTable();
		}
		
		getSupportLoaderManager().getLoader(0).forceLoad();
		
		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment implements OnItemClickListener {
		
		private static final String ARG_SECTION_NUMBER = "section_number";

		public static Context mContext;
		public static void setContext(Context context)
		{
		        mContext = context;
		}
		private OnItemSelectedListener listener;
		
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			listView = (ListView) rootView.findViewById(R.id.rssListView);
			listView.setAdapter(scAdapter);

			 
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			//showNews(position, mContext);
		}


	}

	
	class DownloadRSSTask extends AsyncTask<Void,Void,Void>
	{


	    protected void onPreExecute() 
	    {
	      //display progress dialog.
	    }
	    protected Void doInBackground(Void... params) 
	    {
	    	int i = 0;
	    	
	    	if(Providers.size() <= 1) return null;
	    	
	    	else while(i < Providers.size())
	    	{
	    		ProviderItem pi = Providers.get(i);
	    		RSS.addAll( RssItem.getRssItems(pi.getUrl(),pi.getName()));
	    		i++;
	    	}
	    	return null;
	    }

	    protected void onPostExecute(Void result) 
	    {
	    	try
	    	{
	    		for(int i = 0; i < RSS.size(); i++)
	    		{
	    			rssDB.addRec(RSS.get(i));
	    		}
	    	}
	    	catch(Exception e){}
	    	getSupportLoaderManager().getLoader(0).forceLoad();
	    	if(getSupportLoaderManager().getLoader(0) == null)
	    	{
	    		Log.d(null, "problem with loader");
	    	}
	    }
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new MyCursorLoader(this, rssDB);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		scAdapter.swapCursor(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	
	
	static class MyCursorLoader extends CursorLoader {

	    RssDB db;
	    
	    public MyCursorLoader(Context context, RssDB db) {
	      super(context);
	      this.db = db;
	    }
	    
	    public Cursor loadInBackground() {
	    	Cursor cursor = null;
	    	if(currentFeedProvider.equals("all"))
	    	{
	    		cursor = db.getAllData();
	    	}
	    	else
	    	{
	    		cursor = db.getDataProvider(currentFeedProvider);
	    	}
	    	
	    	//Log.v("lol",DatabaseUtils.dumpCursorToString(cursor));
	        return cursor;
	      }
	  }
	
	public String[] getProviderNames()
	{
		if(resourcesDB == null)
		{
			resourcesDB = new ResourcesDB(this);
			resourcesDB.open();
		}
		
		c = resourcesDB.getAllData();
		String[] names = new String[c.getCount()];
		
		c.moveToFirst();
		
		int i = 0;
		
		while(!c.isAfterLast())
		{
			ProviderItem pi = new ProviderItem();
			names[i] = c.getString(c.getColumnIndex(resourcesDB.COLUMN_RES_NAME));
			pi.setName(names[i]);
			pi.setUrl(c.getString(c.getColumnIndex(resourcesDB.COLUMN_RES_LINK)));
			Providers.add(pi);
			i++;
			c.moveToNext();
		}
		//showProviderDialog();
		return names;	
	}

	public void showProviderDialog() {
		 LayoutInflater factory = LayoutInflater.from(this);
		    final View providerDialogView = factory.inflate(
		            R.layout.dialog_adding, null);
		    
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setView(providerDialogView);
		 // Add the buttons
		 builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) 
		            {
		            	try
		            	{
		    		    EditText name 	= (EditText)providerDialogView.findViewById(R.id.nameProvider);
		    		    EditText url	= (EditText)providerDialogView.findViewById(R.id.urlProvider);
		    		    
		            	resourcesDB.addRec(new ProviderItem(name.getText().toString(), url.getText().toString()));
		            		
		            	}
		            	catch(Exception e)
		            	{
		            		Log.v("lol", "no add to db");
		            	}
		        		
		            }
		        });
		 builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) 
		            {
		            	//return;
		            }
	        });
		// providerDialog.show();
		 builder.create();
		 builder.show();
    }

	public void deleteProvider()
	{
		try
		{
			resourcesDB.delRec(currentDrawerPosition + 1);
		}
		catch(Exception e)
		{
			Log.v("lol","coud not delete");
		}
	}
	
	public static void showNews(int position, Context context)
	{
		 LayoutInflater factory = LayoutInflater.from(context);
		    final View providerDialogView = factory.inflate(
		            R.layout.dialog_adding, null);
		    AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    builder.setView(providerDialogView);
		 // Add the buttons
	
		 builder.create();
		 builder.show();
	}
}
