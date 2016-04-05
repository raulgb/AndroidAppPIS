package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** Body is a generic type of object used in collision system */
public abstract class Body {

	private Shape bounds;

	private Vector2 position = new Vector2();

	public Body(Shape bounds) {
		this.bounds = bounds;
		bounds.setPosition(this.position);
	}

	public abstract void update(float delta);

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 pos) {
		this.position.set(pos);
		bounds.setPosition(pos);
	}

	public Shape getBounds() {
		return bounds;
	}


}
