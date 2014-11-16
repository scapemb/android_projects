package wait.geo_forecast;


import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class DataDialogFragment extends DialogFragment {

   private ArrayList<Integer> indices = new ArrayList<Integer>(); 
   private ArrayList<String> dates = new ArrayList<String>(); 
   private final int NUMBER_IN_SET = 8;
   
   private int[] colors = {Color.BLUE, Color.YELLOW, Color.GREEN};
   public DataDialogFragment() {
       // Empty constructor required for DialogFragment
   }
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.data_dialog, container);
       BarChart chart = (BarChart) view.findViewById(R.id.chart);
       TextView kpText = (TextView) view.findViewById(R.id.Kp_text);
       TextView infoText = (TextView) view.findViewById(R.id.info_text);
       
       getDialog().setTitle("Geomagnetic brief");

       Bundle bundle=getArguments(); 
       
       dates = bundle.getStringArrayList("datelist");  			//get dates from Activity
       indices = bundle.getIntegerArrayList("numlist");  		//get indices from Activity

       ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
       ArrayList<String> xVals = new ArrayList<String>();
       int i = 0, sum = 0;

       for(int index : indices) {								//
    	   yVals.add(new BarEntry(index , i));
    	   xVals.add(""+i);
    	   if(sum < index){sum = index;}						//getting the highest Kp index
    	   i++;
       }
       
       
       kpText.setText("The Highest Kp index = " + sum);
       
       infoText.setText(getInfoText(sum));
       
       ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
       BarDataSet chartData = null;
       	if(dates.size() > 1){
           chartData = new BarDataSet(yVals,"Forecast from " + dates.get(0) + " to " + dates.get(2));
       	}
       	else{
       		chartData = new BarDataSet(yVals,"Forecast for " + dates.get(0));
       	}
       	
           dataSets.add(chartData);
       

       
       
       
       BarData data = new BarData(xVals, dataSets);
       
       chart.setData(data);
       return view;
   }


   private String getInfoText(int Kp){
	   
	   switch(Kp){
	   case 1:{}
	   case 2:{}
	   case 3:{}
	   case 4:{return "G0 - No Geomagnetic Storm";}
	   case 5:{return "G1 - Low Geomagnetic Storm. In high latitude can be auroras. Can be low failures in electronic";}
	   case 6:{return "G2 - Medium Geomagnetic Storm. Can be failures in transformers & other electronics";}
	   case 7:{return "G3 - High Geomagnetic Storm. Overload of electrics. Errors in GPS & radio";}
	   case 8:{return "G4 - Large Geomagnetic Storm. Bad GPS & radio fore some time. Medium frequency radio doesn't work";}
	   case 9:{return "G5 - Very large Geomagnetic Storm. Huge problems with GPS, radio & electronics for some days";}
	   }
	   
	   return "error";
   }
}