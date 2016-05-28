package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public abstract class Vehicle extends GameObject {

	protected abstract void updatePhysics(float delta);

	public abstract void turnLeft(float power);

	public abstract void turnRight(float power);

	public abstract void accelerate(float power);

	/** @param power Ammount of power to use for braking, 0 is none, 1 is full power */
	public abstract void brake(float power);

	public abstract void reverse(float power);

	public Vector2 getAnchor(String name) {
		return getComponent(PhysicsComponent.class).getAnchor(name);
	}

	protected void putAnchor(String name, Vector2 anchor) {
		getComponent(PhysicsComponent.class).putAnchor(name, anchor);
	}
}
