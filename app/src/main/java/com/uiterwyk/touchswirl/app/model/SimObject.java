package com.uiterwyk.touchswirl.app.model;

import java.util.ArrayList;

import android.graphics.Paint;
import android.graphics.Rect;


public interface SimObject {

	public abstract void update();
	public abstract void setPosition(int x, int y);
	public abstract double getX();
	public abstract double getY();
	public abstract void setVelocity(double dx, double dy);
	public abstract double getDX();
	public abstract double getDY();
	public abstract double getAge();
	public abstract boolean isAlive();
	public abstract void setAlive(boolean state);
	public abstract void checkWalls(Rect rect, float bounce);
	public abstract void setColor(int color);
	public abstract int getColor();
	public abstract Paint getPaint();
	public abstract void setPaint(Paint paint);
	public abstract void doGravity(ArrayList<SimObject> simPoints);
}
