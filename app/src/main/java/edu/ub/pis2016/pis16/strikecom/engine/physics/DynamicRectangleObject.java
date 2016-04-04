package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Created by Alexander Bevzenko
 */
public class DynamicRectangleObject extends RectangleObject {
	private Vector2 accel;
	private Vector2 velocity;

	public Vector2 getAccel() {
		return accel;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public void setAccel(Vector2 accel) {
		this.accel = accel;
	}

	/**
	 *
	 * @param x x coordinate of center
	 * @param y y coordinate of center
	 * @param width rectangle bound width
	 * @param height rectangle bound height
	 *               velocity and acceleration are set to 0
	 */
	public DynamicRectangleObject(float x,float y,float width,float height){
		super( x, y, width, height);
		this.accel= new Vector2();
		this.velocity=new Vector2();
	}
}
