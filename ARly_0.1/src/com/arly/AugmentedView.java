package com.arly;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class AugmentedView extends View {
    private static final AtomicBoolean drawing = new AtomicBoolean(false);

    private static final float[] locationArray = new float[3];
    private static final List<AR_Object> cache = new ArrayList<AR_Object>(); 
    private static final int COLLISION_ADJUSTMENT = 100;

    public AugmentedView(Context context) {
        super(context);
    }

	@Override
    protected void onDraw(Canvas canvas) {
    	if (canvas==null) return;
    	List<AR_Object> obj_list = new ArrayList<AR_Object>();
    		
	        obj_list = AR_activity.getMarkers();
	       
	       ListIterator<AR_Object> iter = obj_list.listIterator(obj_list.size());
	        while (iter.hasPrevious()) {
	            AR_Object marker = iter.previous();
	            marker.draw(canvas);
	        } 
	}
	
	public void refresh()
	{
		this.invalidate();
	}
	
}

	