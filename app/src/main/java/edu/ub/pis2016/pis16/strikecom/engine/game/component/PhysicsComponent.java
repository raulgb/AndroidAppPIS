package edu.ub.pis2016.pis16.strikecom.engine.game.component;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.game.Component;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * A Component in charge of keeping the velocity, position and acceleration of a GameObject, as well as managing the
 * Anchoring system.
 *
 * @author German
 */
public class PhysicsComponent extends Component {

	private static Vector2 tmp = new Vector2();
	private Vector2 position = new Vector2();
	private Vector2 velocity = new Vector2();
	private Vector2 acceleration = new Vector2();
	private float rotation = 0;

	private HashMap<String, Vector2> anchors;

	public PhysicsComponent() {
		anchors = new HashMap<>();
	}

	/** Returns a Vector2 anchor for usage with anchored entities */
	public Vector2 getAnchor(String name) {
		return anchors.get(name);
	}

	protected Vector2 putAnchor(String name, Vector2 anchor) {
		return anchors.put(name, anchor);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity.set(velocity);
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration.set(acceleration);
	}

	public void setRotation(float r) {
		this.rotation = r;
	}

	public float getRotation() {
		return rotation;
	}

	/** Rotates the component to face the given target. Lerp speed refers to how fast the rotation zeroes-in. I.e. a
	 * lerp speed of 1 is instant rotation*/
	public void lookAt(Vector2 target, float lerpSpeed) {
		float angleToTarget = tmp.set(target).sub(position).angle();
		float angleDelta = Angle.angleDelta(rotation, angleToTarget);

		rotation += angleDelta * lerpSpeed;
	}

}
