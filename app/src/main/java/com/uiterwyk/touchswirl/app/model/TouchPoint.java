package com.uiterwyk.touchswirl.app.model;

import android.graphics.PointF;

public class TouchPoint {
	private int id = 0;
	private PointF point = new PointF();
	private boolean enabled = false;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PointF getPoint() {
		return point;
	}
	public void setPoint(float x, float y) {
		this.point.x = x;
		this.point.y = y;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
