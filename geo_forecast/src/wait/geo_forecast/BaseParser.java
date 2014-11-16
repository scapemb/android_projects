package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Map;

public abstract class BaseParser {
	
	protected String parselableString;
	protected  Map<String, ArrayList<Integer>> parsedMap;
	
	public void setParcelableString(String s) {
		parselableString = s;
	}

	
	public Map<String,ArrayList<Integer>> getParcedData(){
		return parsedMap;
	}


	public ArrayList<Integer> parse(String yearInFormat, String monthInFormat,
			String dayInFormat) {
				return null;
		// TODO Auto-generated method stub
		
	};
	
	public ArrayList<ArrayList<Integer>> parse() {
				return null;
		// TODO Auto-generated method stub
		
	};
}
