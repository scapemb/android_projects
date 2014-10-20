package wait.wowrss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RssItemAdapter extends BaseAdapter{

	private ArrayList<RssItem> rssItems;
	private LayoutInflater layoutInflater;
	
	public RssItemAdapter (Context context, ArrayList<RssItem> rssItems)
	{
		this.rssItems = rssItems;
		layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rssItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return rssItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		
		if(view == null)
		{
			view = layoutInflater.inflate(R.layout.item_layout, parent, false);			
		}
		
		RssItem rssItem = getRSSItem(position);
		
		TextView titleText			 = (TextView) view.findViewById(R.id.textTitle);
		TextView descrText			 = (TextView) view.findViewById(R.id.textDescription);
		
		titleText.setText(rssItem.toString());
		descrText.setText(rssItem.getDescription());
/*		
		TextView descriptionText	 = (TextView) view.findViewById(R.id.textDescription);
		titleText.setText(rssItem.getDescription());

		TextView linkText			 = (TextView) view.findViewById(R.id.textLink);
		titleText.setText(rssItem.getLink());*/
		return view;
	}

	private RssItem getRSSItem(int position)
	{
		return (RssItem)getItem(position);
	}
}
