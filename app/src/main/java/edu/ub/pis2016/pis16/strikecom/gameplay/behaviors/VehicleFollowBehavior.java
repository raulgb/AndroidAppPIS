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
	GameObject targetGO = null;

	@Override
	public void update(float delta) {
		if (targetGO != null) {
			target.set(targetGO.getPosition());
		}

		// get only what we need to know wether to move
		Vehicle vehicle = (Vehicle) gameObject;
		PhysicsComponent phys = gameObject.getPhysics();
		Vector2 pos = phys.getPosition();
		float distance = tmp.set(target).dst2(pos);

		// Range check
		if (minRange * minRange < distance && distance < maxRange * maxRange) {

			float rotation = phys.getRotation();

			tmp.set(target).sub(pos);
			float angleDelta = Angle.angleDelta(rotation, tmp.angle());
			float absAngleDelta = Math.abs(angleDelta);

			if (absAngleDelta > 15) {
//				if (Math.abs(angleDelta) > 120) {
//					vehicle.reverse(1f);
//				} else {
				// Reduce power when we're close to target
				float power = MathUtils.min(1, absAngleDelta / 30f);
				if (absAngleDelta > 90)
					vehicle.reverse(1);
				else {
					if (angleDelta > 0)
						vehicle.turnLeft(power);
					else if (angleDelta < 0)
						vehicle.turnRight(power);
				}


			} else {
				vehicle.accelerate(1);
			}
		} else {
			vehicle.brake(0.3f);
		}

	}


	/** Set the position to go to. if {@code null} is passed, will stop immediately. */
	public void setTarget(Vector2 target) {
		if (target == null)
			this.target.set(gameObject.getPosition());
		this.target.set(target);
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
