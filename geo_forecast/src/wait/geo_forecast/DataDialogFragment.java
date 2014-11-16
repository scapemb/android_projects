package wait.geo_forecast;


import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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
   //private 
   public DataDialogFragment() {
       // Empty constructor required for DialogFragment
   }
//TODO: set onClickListener
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.data_dialog, container);
       BarChart chart = (BarChart) view.findViewById(R.id.chart);
       
       getDialog().setTitle("Geomagnetic brief");

       Bundle bundle=getArguments(); 
       
       //numbers = bundle.getStringArrayList("numlist");  
       indices = bundle.getIntegerArrayList("numlist");  
       int setNumber = (indices.size() / 8);
       ArrayList<ArrayList<BarEntry>> yVals = new ArrayList<ArrayList<BarEntry>>();
       
       for(int i=0; i < setNumber; i++)
       {
    	   yVals.add(new ArrayList<BarEntry>() );
       }
       
       ArrayList<String> xVals = new ArrayList<String>();
       int i = 0, ind = 0, num = 0;
       for(int index : indices)
       {
    	   if(num > 7)
    	   {ind++;num = 0;}
    	   yVals.get(ind).add(new BarEntry(index , i));
    	   xVals.add(""+i);
    	   i++; num++;
       }
       ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
       
       for(ArrayList<BarEntry> index : yVals) {
           BarDataSet chartData = new BarDataSet(index,"Kp-indices");
          // chartData.setColor(color);
           dataSets.add(chartData);
       }

       
       
       
       BarData data = new BarData(xVals, dataSets);
       
       chart.setData(data);
       return view;
   }


}