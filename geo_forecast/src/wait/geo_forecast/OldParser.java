package wait.geo_forecast;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class OldParser extends BaseParser {

	@Override
	public void parse(int year, String monthInFormat, String dayInFormat) {

		Pattern pattern = Pattern.compile(year+"[\\s]" + monthInFormat + "[\\s]" + dayInFormat + "[\\d\\s]{69}");//[.]{54}[.]{15}");
		Matcher matcher = pattern.matcher(parselableString);
		if (matcher.find())
		{
			  String raw =  matcher.group().substring(0, 11) + matcher.group().substring(64);
		}
		
	}
}
