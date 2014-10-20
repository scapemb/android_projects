package com.arly;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PaintIcon extends PaintObject {
	private Bitmap bitmap=null;
	
	public PaintIcon(Bitmap bitmap, int width, int height) {
    	set(bitmap,width,height);
    }
	
	 public void set(Bitmap bitmap, int width, int height) {
	    	if (bitmap==null) throw new NullPointerException();
	    	
	        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
	 }
	 
	@Override
	public float getWidth() {
		return bitmap.getWidth();
	}

	@Override
	public float getHeight() {
		return bitmap.getHeight();
		
	}

	@Override
	public void paint(Canvas canvas) {
		if (canvas==null || bitmap==null) throw new NullPointerException();

        paintBitmap(canvas, bitmap, -(bitmap.getWidth()/2), -(bitmap.getHeight()/2));
    
	}

}
