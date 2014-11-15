package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Map;

public abstract class BaseParser {
	
	protected String parselableString;
	protected  Map<String, ArrayList<Integer>> parsedMap;
	
	public void setParcelableString(String s) {
		parselableString = s;
	}
	
	public void parse(){};
	
	public Map<String,ArrayList<Integer>> getParcedData(){
		return parsedMap;
	}


	public String parse(String yearInFormat, String monthInFormat,
			String dayInFormat) {
				return dayInFormat;
		// TODO Auto-generated method stub
		
	};
}
