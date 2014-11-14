package wait.geo_forecast;


import java.util.ArrayList;
import java.util.List;

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

   public interface PickNumberDialogListener {
       void onFinishPickNumberDialog(int position, boolean isDelete);
   }

   private ArrayList<String> numbers = new ArrayList<String>(); 
   public DataDialogFragment() {
       // Empty constructor required for DialogFragment
   }
//TODO: set onClickListener
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_geo, container);
       getDialog().setTitle("Сообщение");

       Bundle bundle=getArguments(); 
       
       numbers = bundle.getStringArrayList("numlist");  
       
       return view;
   }


}