package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForecastParser extends BaseParser{

	private ArrayList<ArrayList<Integer>> indices = new  ArrayList<ArrayList<Integer>>();
	private final int forecastDaysNumber = 3;
	
	@Override
	public ArrayList<ArrayList<Integer>> parse() {
		initializeArray();		
		
		//00-03UT        3         4         4
		Pattern pattern = Pattern.compile("[\\d]{2}" + "-" + "[\\d]{2}" + "UT" + "[\\d\\s]{29}");//[.]{54}[.]{15}");
		Matcher matcher = pattern.matcher(parselableString);
		while (matcher.find())
		{
			getIndices(matcher.group().substring(11));
			
		}
		return indices;
	}
	
	private void getIndices(String data){
		 //ArrayList<Integer> indices = new  ArrayList<Integer>();
        int i = 0;
		for(char c : data.toCharArray()){
			if(c != ' '){
				indices.get(i).add(Character.getNumericValue(c));
				i++;
			}
		}
		
	}
	
	private void initializeArray()
	{
		indices.clear();
		for(int i = 0; i < forecastDaysNumber; i++)
		{
			indices.add(new ArrayList<Integer>());
		}
	}
}
