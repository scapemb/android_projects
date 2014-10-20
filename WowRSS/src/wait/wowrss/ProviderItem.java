package wait.wowrss;

public class ProviderItem {

	private String Name;
	private String URL;
	
	public ProviderItem(String name, String Url)
	{
		this.Name = name;
		this.URL = Url;
	}
	public ProviderItem()
	{
		
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public String getUrl()
	{
		return this.URL;
	}
	
	public void setName(String name)
	{
		this.Name = name;
	}
	
	public void setUrl(String url)
	{
		this.URL = url;
	}
}
