package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.widget.Toast;

public class OldParser extends BaseParser {

	@Override
	public ArrayList<Integer> parse(String yearInFormat, String monthInFormat, String dayInFormat) {

		Pattern pattern = Pattern.compile(yearInFormat+"[\\s]" + monthInFormat + "[\\s]" + dayInFormat + "[\\d\\s]{69}");//[.]{54}[.]{15}");
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
			if(c != ' '){
				indices.add(Character.getNumericValue(c));
			}
		}
		
		return indices;		
	}
}
