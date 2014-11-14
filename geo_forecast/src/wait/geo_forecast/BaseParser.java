package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Map;

public abstract class BaseParser {
	
	private String parselableString;
	private  Map<String, ArrayList<Integer>> parsedMap;
	
	public void setParcelableString(String s) {
		parselableString = s;
	}
	
	public void parse(){};
	
	public Map<String,ArrayList<Integer>> getParcedData(){
		return parsedMap;
	};
}
