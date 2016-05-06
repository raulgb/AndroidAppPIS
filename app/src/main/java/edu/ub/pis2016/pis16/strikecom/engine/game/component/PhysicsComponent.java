package edu.ub.pis2016.pis16.strikecom.engine.game.component;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.game.Component;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Body;
import edu.ub.pis2016.pis16.strikecom.engine.physics.DynamicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Shape;
import edu.ub.pis2016.pis16.strikecom.engine.physics.StaticBody;

/**
 * A Component in charge of keeping the velocity, position and acceleration of a GameObject, as well as managing the
 * Anchoring system.
 *
 * @author German
 */
public class PhysicsComponent extends Component {
	public Body body = null;

	private static Vector2 tmp = new Vector2();
	private Vector2 position = new Vector2();
	private Vector2 velocity = new Vector2();
	private Vector2 acceleration = new Vector2();
	private float rotation = 0;

	private HashMap<String, Vector2> anchors = new HashMap<>();

	/** Basic Physics, no collisions, no updating every frame. Use for GUI etc. */
	public PhysicsComponent() {
		position.set(0, 0);
	}

	/** Full Physics behavior, collisions, updated automatically etc */
	public PhysicsComponent(Body body) {
		this.body = body;
	}

	protected void init() {
		if (body != null) {
			body.userData = this.gameObject;
			gameObject.getScreen().getPhysics2D().addBody(this.body);
		}
	}

	protected void destroy() {
		if (body != null) {
			body.userData = null;
			gameObject.getScreen().getPhysics2D().removeBody(this.body);
		}
	}

	/** Returns a Vector2 anchor for usage with anchored entities */
	public Vector2 getAnchor(String name) {
		return anchors.get(name);
	}

	public void putAnchor(String name, Vector2 anchor) {
		anchors.put(name, anchor);
	}

	public Vector2 getPosition() {
		if (body != null)
			return position.set(body.position);
		else return position;
	}

	public void setPosition(Vector2 pos) {
		if (body != null)
			body.position.set(pos);
		else position.set(pos);
	}

	public void setPosition(float x, float y) {
		if (body != null)
			body.position.set(x, y);
		else position.set(x, y);
	}

	public Vector2 getVelocity() {
		if (body instanceof DynamicBody)
			return velocity.set(((DynamicBody) body).velocity);
		if (body instanceof KinematicBody)
			return velocity.set(((KinematicBody) body).velocity);
		return tmp.set(0, 0);
	}

	public void setVelocity(Vector2 velocity) {
		if (body instanceof DynamicBody)
			((DynamicBody) body).velocity.set(velocity);
		if (body instanceof KinematicBody)
			((KinematicBody) body).velocity.set(velocity);
	}

	public void setVelocity(float x, float y) {
		if (body instanceof DynamicBody)
			((DynamicBody) body).velocity.set(x, y);
		if (body instanceof KinematicBody)
			((KinematicBody) body).velocity.set(x, y);
	}

	public Vector2 getAcceleration() {
		if (body instanceof DynamicBody)
			return acceleration.set(((DynamicBody) body).acceleration);
		return tmp.set(0, 0);
	}

	public void setAcceleration(Vector2 accel) {
		if (body instanceof DynamicBody)
			((DynamicBody) body).acceleration.set(accel);
	}

	public void setRotation(float r) {
		if (body != null)
			body.getBounds().setRotation(r);
		else rotation = r;
	}

	public float getRotation() {
		if (body != null)
			return body.getBounds().getRotation();
		else return rotation;
	}

	/**
	 * Rotates the component to face the given target. Lerp speed refers to how fast the rotation zeroes-in. I.e. a
	 * lerp speed of 1 is instant rotation
	 */
	public void lookAt(Vector2 target, float lerpSpeed) {
		float angleToTarget = tmp.set(target).sub(position).angle();
		float angleDelta = Angle.angleDelta(rotation, angleToTarget);

		rotation += angleDelta * lerpSpeed;
	}

}
