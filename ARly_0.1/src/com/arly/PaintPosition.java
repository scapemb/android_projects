package com.arly;


import android.graphics.Canvas;

public class PaintPosition extends PaintObject {
    private float width=0, height=0;
    private float objX=0, objY=0, objRotation=0, objScale=0;
    private PaintObject obj = null;
    
    public PaintPosition(PaintObject drawObj, float x, float y, float rotation, float scale) {
    	set(drawObj, x, y, rotation, scale);
    }

    public void set(PaintObject drawObj, float x, float y, float rotation, float scale) {
    	if (drawObj==null) throw new NullPointerException();
    	
        this.obj = drawObj;
        this.objX = x;
        this.objY = y;
        this.objRotation = rotation;
        this.objScale = scale;
        this.width = obj.getWidth();
        this.height = obj.getHeight();
    }

    public void move(float x, float y) {
        objX = x;
        objY = y;
    }

    public float getObjectsX() {
        return objX;
    }

    public float getObjectsY() {
        return objY;
    }
    
	@Override
    public void paint(Canvas canvas) {
    	if (canvas==null || obj==null) throw new NullPointerException();
    	
        paintObj(canvas, obj, objX, objY, objRotation, objScale);
    }

	@Override
    public float getWidth() {
        return width;
    }

	@Override
    public float getHeight() {
        return height;
    }

    @Override
	public String toString() {
	    return "objX="+objX+" objY="+objY+" width="+width+" height="+height;
	}
}