package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.widget.Toast;

public class OldParser extends BaseParser {
	/**
	  * <p>Parsing need old data from raw string.</p>
	  * <p>With regex method get data from binding date </p>
	  *
	  * @param yearInFormat 	year in format YYYY
	  * @param monthInFormat 	month in format MM 
	  * @param dayInFormat 		day in format DD  
	  * @return ArrayList<Integer> 
	  */
	@Override
	public ArrayList<Integer> parse(String yearInFormat, String monthInFormat, String dayInFormat) {

		Pattern pattern = Pattern.compile(yearInFormat+"[\\s]" + monthInFormat + "[\\s]" + dayInFormat + "[\\d\\s\\-]{69}");//[.]{54}[.]{15}");
		Matcher matcher = pattern.matcher(parselableString);
		if (matcher.find())
		{
			return getIndices(matcher.group().substring(0, 11) + matcher.group().substring(64));
			
		}
		return null;
	}
	
	private ArrayList<Integer> getIndices(String data){
		 ArrayList<Integer> indices = new  ArrayList<Integer>();
		for(char c : data.substring(11).toCharArray()){
			if(c != ' ' && c != '-'){
				indices.add(Character.getNumericValue(c));
			}
		}
		
		return indices;		
	}
}
