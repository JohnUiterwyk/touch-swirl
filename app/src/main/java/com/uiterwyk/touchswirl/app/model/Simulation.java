package com.uiterwyk.touchswirl.app.model;

import java.util.ArrayList;
import java.util.List;

import com.uiterwyk.touchswirl.app.utils.RandomNumbers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.SparseArray;

public class Simulation implements Runnable
{
	private ArrayList<SimObject> simPoints = new ArrayList<SimObject>();
	
	private volatile SparseArray<TouchPoint> touchPointMap = new  SparseArray<TouchPoint>(10);
	private volatile ArrayList<TouchPoint> touchPointArray = new ArrayList<TouchPoint>(10);
	
	private Rect walls = new Rect();
	
	private volatile float gravityX = 0;
	private volatile float gravityY = 0;
	private volatile boolean gravityEnabled = false;
	private volatile boolean interruptThread = false; 
	boolean initialized = false; 
	
	private Thread tickThread;

	private static Simulation singletonInstance;
	
	private int objectCountDelta = 0;
	
	public static Simulation getInstance()
	{
		if(singletonInstance == null)
		{
			singletonInstance = new Simulation();
			
		}
		return singletonInstance;
	}
	
	public Simulation()
	{
		for(int i = 0;i<10;i++)
		{
			TouchPoint point = new TouchPoint();
			touchPointArray.add(i, point);
			
		}
	}
	
	public void initialize(int width, int height)
	{
		setWalls(width, height);
		createShapes(2000);
		initialized = true;
	}
	
	public void createShapes(int count) {
		for (int i = 0; i < count; i++) {
			addShape((int) randomRange(0, walls.width()),
					(int) randomRange(0, walls.height()));

		}
	}
	public void removeShapes(int count)
	{
		for (int i = 0; i < count; i++) {
			if(simPoints.size() > 0)simPoints.remove(0);
		}
	}
	public void addShape(int x, int y) {

		AbstractSimObject simPoint = new AbstractSimObject();

		double dx, dy;
		do {
			dx = randomRange(-1, 1);
			dy = randomRange(-1, 1);
		} while (dx == 0 && dy == 0);

		simPoint.setVelocity(dx, dy);
		simPoint.setPosition(x, y);
		simPoint.setColor(Color.rgb((int) randomRange(0, 256), (int) randomRange(0,
					256), (int) randomRange(0, 256)));
		simPoints.add(simPoint);
	}

	public void onPause()
	{
		interruptThread = true;
		
		boolean retry = true;
		while(retry)
		{
			try{
				tickThread.join();
				retry = false;
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		tickThread = null;
	}
	
	public void onResume()
	{
		if(tickThread == null) tickThread = new Thread(this);
		tickThread.start();
	}
	@Override
	public void run() {
		interruptThread = false;
		while(!interruptThread)
		{
			if(initialized)tick();
			
			//The following Thread.sleep throttles the physics tick to 60fps (1000/60 = 16.6)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private double gx, gy,totalx,totaly, distance;
	public void tick()
	{
		//float bounce = randomRange(0.9f,1f);
		for (SimObject simPoint : simPoints) {
			totalx=0;
			totaly=0;
			simPoint.update();
			for (int i = 0; i < 10; i++) {
				if(touchPointArray.get(i).isEnabled())
				{
					gx = touchPointArray.get(i).getPoint().x - simPoint.getX();
					gy = touchPointArray.get(i).getPoint().y - simPoint.getY();
					distance = gx * gx + gy * gy;
					if (distance > 50 && distance < 200000) {
						totalx += gx * 10 / distance;
						totaly += gy * 10 / distance;
					}
				}
			}
			if(gravityEnabled)
			{
				totalx += gravityX * .003f;
				totaly += gravityY * .003f;
			}
			//simPoint.doGravity(simPoints);
			simPoint.setVelocity(
					(simPoint.getDX() + totalx)*.993,
					(simPoint.getDY() + totaly)*.993 );
			//check walls
			//shape.checkWalls(walls,randomRange(0.97f,0.99f));
			simPoint.checkWalls(walls,0.95f);
		}

	}

	public void setWalls(int width, int height)
	{

		walls.bottom = height;
		walls.right = width;
	}
	

	public synchronized void removeTouch(int id)
	{
		touchPointMap.get(id).setEnabled(false);
		touchPointMap.delete(id);		
	}


	public void updateTouchPoint(float x, float y, int id) 
	{
		if(touchPointMap.get(id) == null)
		{
			// get disabled point
			for (TouchPoint point : touchPointArray) 
			{
				if(point.isEnabled() == false)
				{
					point.setEnabled(true);
					point.setId(id);
					point.setPoint(x, y);
					touchPointMap.put(id, point);
					break;
				}
			}
			
		}
		try {
			touchPointMap.get(id).setPoint(x, y);
		} catch (IndexOutOfBoundsException e) {
		}
	}



	public void updateGravity(float gx, float gy)
	{
         gravityX = gx;
         gravityY = gy;
	}
	   
	public boolean isGravityEnabled()
	{
		return gravityEnabled;
	}
	public void toggleGravity()
	{
		gravityEnabled = !gravityEnabled;
		gravityX = 0;
		gravityY = 0;
	}
	
	public List<SimObject> getSimPoints()
	{
		return this.simPoints;
	}

	private float randomRange(float min, float max) {
		//return (float)Math.random() * (max - min) + min;
		return RandomNumbers.getNext() * (max - min) + min;
		
	}
	
	public void interruptRun()
	{
		interruptThread = true;
		
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return initialized;
	}

	public int getObjectCountDelta() {
		return objectCountDelta;
	}

	public void setObjectCountDelta(int objectCountDelta) {
		this.objectCountDelta = objectCountDelta;
	}
	
}
