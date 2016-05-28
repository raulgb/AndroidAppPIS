package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** Body is a generic type of object used in collision system */
public abstract class Body {

	/** Center of the body */
	public Vector2 position = new Vector2();
	public float rotation = 0f;

	/** The shape of the body, centered around it */
	protected Shape bounds;

	public Object userData;
	public Physics2D.Filter filter = Physics2D.Filter.ALL;
	public boolean colliding = false;

	public Body(Shape bounds) {
		this.bounds = bounds;
	}

	/**
	 * Sets the bounds to match the center of the Body. Also updates aproximate aabb.
	 * <p>MUST BE CALLED BEFORE COLLISION TESTING.
	 */
	public void updateBounds() {
		switch (bounds.type) {
			case RECTANGLE:
				bounds.x = position.x - bounds.width / 2f;
				bounds.y = position.y - bounds.height / 2f;
				bounds.rotation = rotation;
				break;
			case CIRCLE:
				bounds.x = position.x;
				bounds.y = position.y;
				break;
			default:
				bounds.x = position.x;
				bounds.y = position.y;
				bounds.rotation = rotation;
				break;
		}
		bounds.aabb();
	}
}
