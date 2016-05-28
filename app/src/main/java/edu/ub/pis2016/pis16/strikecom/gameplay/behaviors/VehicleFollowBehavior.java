package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.gameplay.Vehicle;

public class VehicleFollowBehavior extends BehaviorComponent {

	Vector2 target = new Vector2();
	Vector2 tmp = new Vector2();

	float minRange = 0;
	float maxRange = Float.MAX_VALUE;
	boolean moveOrder;
	GameObject targetGO = null;

	@Override
	public void update(float delta) {
		if(targetGO != null && targetGO.isValid()){
			moveOrder = true;
			target.set(targetGO.getPosition());
		}

		if (!moveOrder)
			return;

		Vector2 pos = gameObject.getComponent(PhysicsComponent.class).getPosition();
		float rotation = gameObject.getComponent(PhysicsComponent.class).getRotation();
		Vehicle vehicle = (Vehicle) gameObject;

		// Move AI, strikebase follows the move pointer
		float distance = tmp.set(target).dst2(pos);
		if (minRange * minRange < distance && distance < maxRange * maxRange) {

			tmp.set(target).sub(pos);
			float angleDelta = Angle.angleDelta(rotation, tmp.angle());
			float absAngleDelta = Math.abs(angleDelta);

			if (absAngleDelta > 5) {
//				if (Math.abs(angleDelta) > 120) {
//					vehicle.reverse(1f);
//				} else {
				// Reduce power when we're close to target
				float power = MathUtils.min(1, absAngleDelta / 30f);
				if (angleDelta > 0)
					vehicle.turnLeft(power);
				else
					vehicle.turnRight(power);
//				if (absAngleDelta > 90)
//					vehicle.brake();

			} else {
				vehicle.accelerate(1);
			}
		} else {
			vehicle.brake(0.3f);
			if (gameObject.getComponent(PhysicsComponent.class).getVelocity().isZero()) {
				moveOrder = false;
			}
		}
	}

	/** Set the position to go to. if {@code null} is passed, will stop immediately. */
	public void setTarget(Vector2 target) {
		if (target == null)
			this.target.set(gameObject.getPosition());
		this.target.set(target);
		moveOrder = true;
	}

	/** Set the position to go to. if {@code null} is passed, will stop immediately. */
	public void setTarget(GameObject go) {
		this.targetGO = go;
	}

	/** Set the max tracking distance */
	public void setMaxRange(float range) {
		maxRange = range;
	}

	/** Set the distance from the target to stop moving towards it */
	public void setMinRange(float range) {
		minRange = range;
	}
}
