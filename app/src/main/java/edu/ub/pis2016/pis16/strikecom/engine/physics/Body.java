package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** Body is a generic type of object used in collision system */
public abstract class Body {

	public Vector2 position = new Vector2();

	/** The shape of the body, centered around it */
	public Shape bounds;
	public Object userData;
	public Physics2D.Filter filter = Physics2D.Filter.ALL;
	public boolean colliding = false;

	public Body(Shape bounds) {
		this.bounds = bounds;
	}

	public Shape getBounds() {
		return bounds;
	}

	public boolean collide(Body other) {
		bounds.setPosition(this.position);
		other.bounds.setPosition(other.position);

		return bounds.overlaps(other.bounds);
	}
}
