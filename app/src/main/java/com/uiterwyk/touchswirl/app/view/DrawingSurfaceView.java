package com.uiterwyk.touchswirl.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//import com.uiterwyk.touchswirl.R;
import com.uiterwyk.touchswirl.app.model.SimObject;
import com.uiterwyk.touchswirl.app.model.Simulation;

public class DrawingSurfaceView extends SurfaceView implements Runnable
{
	Thread drawThread = null;
	Thread tickThread = null;
	SurfaceHolder surfaceHolder;
	Simulation simulation;
	volatile boolean interruptThread = false; 
	
	
	public DrawingSurfaceView(Context context)
	{
		super(context);
		surfaceHolder = getHolder();
		//drawThread = new Thread(this);		
	}

	public void onPause()
	{
		interruptThread = true;
		
		boolean retry = true;
		while(retry)
		{
			try{
				drawThread.join();
				retry = false;
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		drawThread = null;
	}
	
	public void onResume()
	{
		if(simulation == null) simulation = Simulation.getInstance();
		if(drawThread == null)drawThread = new Thread(this);
		drawThread.start();
	}

	public Rect getDimensions()
	{
		Rect result = new Rect();
		if(surfaceHolder.getSurface().isValid())
		{
			
			Canvas canvas = surfaceHolder.lockCanvas();
			result.set(0, 0, canvas.getWidth(), canvas.getHeight());
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
		return result;
			
	}
	
	@Override
	public void run()
	{
		interruptThread = false;
		while(!interruptThread)
		{
			if(surfaceHolder.getSurface().isValid())
			{
				
				Canvas canvas = surfaceHolder.lockCanvas();
				//fill the screen with semi transparent black, creates trail effect
				// the lower the first number, the longer the trails
				canvas.drawARGB(65, 0, 0, 0);
				if(simulation.isInitialized() == false)
				{
					simulation.initialize(canvas.getWidth(), canvas.getHeight());
				}else
				{
					simulation.setWalls(canvas.getWidth(), canvas.getHeight());
				}
				//loop through the sim points and draw them
				for (SimObject simPoint : simulation.getSimPoints()) 
				{
					//canvas.drawLine((float)simPoint.getX(), (float)simPoint.getY(), (float)simPoint.getX()+(float)simPoint.getDX()*10, (float)simPoint.getY()+(float)simPoint.getDY()*10, simPoint.getPaint());
					canvas.drawPoint((float)simPoint.getX(),(float)simPoint.getY(),simPoint.getPaint());
					//canvas.drawCircle((float)simPoint.getX(),(float)simPoint.getY(),10,simPoint.getPaint());
					
				}
				
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
			
		}	
	}	
	
}
