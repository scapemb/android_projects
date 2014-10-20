package com.arly;



import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AugmentedActivity extends Activity implements OnTouchListener {
	protected static AugmentedView augmentedView = null;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.activitymain);
	        
	        augmentedView = new AugmentedView(this);
            augmentedView.setOnTouchListener(this);
	        LayoutParams augLayout = new LayoutParams(  LayoutParams.WRAP_CONTENT, 
	                                                    LayoutParams.WRAP_CONTENT);
	        addContentView(augmentedView,augLayout);
	        
	 }
    public boolean onTouch(View view, MotionEvent me) {
        Log.w("TOUCH","Touch on screen");
        List<AR_Object> obj_list = new ArrayList<AR_Object>();
        obj_list = AR_activity.getMarkers();

       for(int i=0;i<obj_list.size();i++)
       {
           if(obj_list.get(i).handleClick(me.getX(),me.getY()))
           {
               if (me.getAction() == MotionEvent.ACTION_UP) objectTouched(obj_list.get(i));
               return true;
           }
       }
        return super.onTouchEvent(me);
    };

    protected void objectTouched(AR_Object obj) {
        Log.w("TOUCH","objectTouched() not implemented.");
    }
}
	 

