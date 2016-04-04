package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Created by Alexander Bevzenko on 15/03/16.
 */
public class Body {// body is generic type of object used in collision system
	private Vector2 position;

	public Vector2 getPosition() {
		return position;
	}

	/**
	 *  generic constructor
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Body (float x, float y){
		this.position= new Vector2(x,y);
	}
}
