package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** Body is a generic type of object used in collision system */
public abstract class Body {

	public Vector2 position = new Vector2();
	public Object userData;

	/** The shape of the body, centered around it */
	protected Shape bounds;

	public Body(Shape bounds) {
		this.bounds = bounds;
		bounds.setPosition(this.position);
	}

	public Shape getBounds() {
		return bounds;
	}

	public boolean collide(Body body){
		bounds.setPosition(this.position);
		body.bounds.setPosition(body.position);

		return bounds.overlaps(body.bounds);
	}
}
