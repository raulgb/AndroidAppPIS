package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public class DynamicBody extends Body {

	private Vector2 velocity = new Vector2();
	private Vector2 acceleration = new Vector2();

	public DynamicBody(Shape bounds) {
		super(bounds);
	}

	@Override
	public void update(float delta) {

	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration.set(acceleration);
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity.set(velocity);
	}


}
