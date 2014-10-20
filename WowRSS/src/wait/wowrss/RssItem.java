package wait.wowrss;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RssItem {

	private String title;
	private String description;
	private Date pubDate;
	private String link;
	private String provider;
	
	public RssItem (String title, String description, Date pubDate, String link, String provider)
	{
		this.title = title;
		this.description = description;
		this.pubDate = pubDate;
		this.link = link;
		this.provider = provider;
	}
	
	public String getTitle ()
	{
		return this.title;
	}
	
	public String getDescription ()
	{
		return this.description;
	}
	
	public Date getPubDate ()
	{
		return this.pubDate;
	}
	
	public String getLink ()
	{
		return this.link;
	}
	
	public String getProvider ()
	{
		return this.provider;
	}
	
	@Override
	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");

	    String result = getTitle() + "  ( " + sdf.format(this.getPubDate()) + " )";
	    return result;
	}
	
	public static ArrayList<RssItem> getRssItems(String feedUrl, String provider)
	{
		ArrayList<RssItem> RssItems = new ArrayList<RssItem>();
		
		try
		{
			URL url = new URL(feedUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				InputStream is = conn.getInputStream();
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document document = db.parse(is);
				Element element = document.getDocumentElement();
				
				NodeList nodeList = element.getElementsByTagName("item");
				
				if(nodeList.getLength() > 0)
				{
					for(int i = 0; i < nodeList.getLength(); i++)
					{
						Element entry = (Element) nodeList.item(i);
						
						Element titleElement		 = (Element) entry.getElementsByTagName("title").item(0);
						Element descriptionElement	 = (Element) entry.getElementsByTagName("description").item(0);
						Element pubDateElement		 = (Element) entry.getElementsByTagName("pubDate").item(0);
						Element linkElement			 = (Element) entry.getElementsByTagName("link").item(0);

						String title		 	= titleElement.getFirstChild().getNodeValue();
						String description	 	= descriptionElement.getFirstChild().getNodeValue();
						Date pubDate 		 	= new Date(pubDateElement.getFirstChild().getNodeValue());
						String link		 		= linkElement.getFirstChild().getNodeValue();
						
						RssItem rssItem = new RssItem(title, description, pubDate, link, provider);
						
						RssItems.add(rssItem);
					}
				}
			}
		}
		catch(Exception e)
		{
			
		}
		return RssItems;
	}
}
