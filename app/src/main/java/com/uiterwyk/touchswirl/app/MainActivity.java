package com.uiterwyk.touchswirl.app;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.uiterwyk.touchswirl.app.R;
import com.uiterwyk.touchswirl.app.model.Simulation;
import com.uiterwyk.touchswirl.app.view.DrawingSurfaceView;

public class MainActivity extends Activity implements SensorEventListener {

    DrawingSurfaceView drawingSurface;
    Simulation simulation;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.drawingSurface = new DrawingSurfaceView(this);
        this.setContentView(this.drawingSurface);
        simulation = Simulation.getInstance();
        //simulation.initialize(1280, 960);
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        simulation.onPause();
        drawingSurface.onPause();
        // TODO: continue turning surface view into just a drawing sub class,
        // activity should connect the model and the surface view

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		if(simulation.isInitialized() == false)
//		{
//			Point windowSize = new Point();
//			this.getWindowManager().getDefaultDisplay().getSize(windowSize);
//			simulation.initialize(windowSize.x-150, windowSize.y-150);
//		}
        simulation.onResume();
        drawingSurface.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
//            case R.id.menu_gravity:
//                simulation.toggleGravity();
//
//                if(simulation.isGravityEnabled())
//                {
//                    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
//
//                }else
//                {
//                    sensorManager.unregisterListener(this);
//                }
//                return true;
//            case  R.id.menu_add_300:
//                simulation.onPause();
//                drawingSurface.onPause();
//                simulation.createShapes(300);
//                Toast.makeText(this, String.valueOf(simulation.getSimPoints().size()) + " total", Toast.LENGTH_SHORT).show();
//                drawingSurface.onResume();
//                simulation.onResume();
//                return true;
//
//            case  R.id.menu_remove_300:
//                simulation.onPause();
//                drawingSurface.onPause();
//                simulation.removeShapes(300);
//                Toast.makeText(this, String.valueOf(simulation.getSimPoints().size())+" total", Toast.LENGTH_SHORT).show();
//                drawingSurface.onResume();
//                simulation.onResume();
//                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int length, index, id;
        int action = event.getActionMasked();

        //touch coordinates are in reference to activity
        //we need to get the location of the drawingSurface
        //and then subtract that x and y from the event location
        int[] coords = new int[2];
        drawingSurface.getLocationInWindow(coords);
        event.offsetLocation(-coords[0], -coords[1]);


        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                index = event.getActionIndex();
                id = event.getPointerId(index);

                simulation.updateTouchPoint(event.getX(index), event.getY(index), id);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_POINTER_UP:
                index = event.getActionIndex();
                id = event.getPointerId(index);
                simulation.removeTouch(id);
                break;

            case MotionEvent.ACTION_MOVE:
                length = event.getPointerCount();
                for (int i = 0; i < length; i++)
                {
                    id = event.getPointerId(i);
                    simulation.updateTouchPoint(event.getX(i), event.getY(i),id);
                }
                break;
        }

        //The following throttles the speed of touch updates to 30fps
        //otherwise this event could get called way to much
        try {
            Thread.sleep(33);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && simulation.isGravityEnabled())
        {
            simulation.updateGravity(-event.values[0],event.values[1]);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
