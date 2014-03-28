package com.uiterwyk.touchswirl.app.model;

import java.util.ArrayList;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 
 * AbstractGeometricShape is the base class for geometric shapes
 * @author johnuiterwyk
 *
 */
public class AbstractSimObject implements SimObject {
	
	protected double x = 0;
	protected double y = 0;
	protected double dx = 0;
	protected double dy = 0;
	protected int age;
	private  int color;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean alive = true;



	public AbstractSimObject() {

		color = Color.rgb(255, 0, 0);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setColor(color);
		//paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
	}


	@Override
	public void setPosition(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public void setVelocity(double dx, double dy)
	{

		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public int getColor()
	{
		return color;
	}
	
	@Override
	public void setColor(int color)
	{
		this.color = color;
		paint.setColor(color);
	}

	@Override
	public Paint getPaint() {
		return paint;
	}


	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}


	@Override
	public double getX() {
		return x;
	}


	@Override
	public double getY() {
		return y;
	}


	@Override
	public double getDX() {
		return dx;
		
	}


	@Override
	public double getDY() {
		return dy;
		
	}


	@Override
	public void update() {
		this.x+= this.dx;
		this.y+= this.dy;
		this.age++;
		
	}


	@Override
	public double getAge() {
		return age;
	}


	@Override
	public void checkWalls(Rect walls, float bounce) {
		if(this.x < walls.left && this.dx < 0)this.dx*=-1*bounce;
		if(this.y<walls.top && this.dy < 0)this.dy*=-1*bounce;
		if(this.x>walls.right && this.dx > 0)this.dx*=-1*bounce;
		if(this.y>walls.bottom && this.dy > 0)this.dy*=-1*bounce;
	}


	@Override
	public boolean isAlive() {
		
		return alive;
	}


	@Override
	public void setAlive(boolean state) {
		alive = state;
	}


	private double gx, gy,totalx,totaly, distance;
	@Override
	public void doGravity(ArrayList<SimObject> simPoints) {
		totalx = 0;
		totaly = 0;
		for (SimObject simObject : simPoints) 
		{
			if(simObject != this)
			{
				gx =  simObject.getX() - this.getX();
				gy =  simObject.getY() - this.getY();
				distance = Math.sqrt(gx * gx + gy * gy);
				
				if (distance > 100 && distance < 300 ) {
					totalx += .001*gx  / distance;
					totaly += .001*gy  / distance;
				}
			}
			
		} 
		this.setVelocity((totalx+this.getDX()),( totaly+this.getDY()));
		// TODO Auto-generated method stub
		
	}


}
