package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.gameplay.Vehicle;

public class VehicleFollowBehavior extends BehaviorComponent {

	Vector2 target = new Vector2();
	Vector2 tmp = new Vector2();

	float minRange = 0;
	float maxRange = Float.MAX_VALUE;
	boolean moveOrder;

	@Override
	public void update(float delta) {
		if (!moveOrder)
			return;

		Vector2 pos = gameObject.getComponent(PhysicsComponent.class).getPosition();
		float rotation = gameObject.getComponent(PhysicsComponent.class).getRotation();
		Vehicle vehicle = (Vehicle) gameObject;

		// Move AI, strikebase follows the move pointer
		// TODO Not working, vehicles never stop??
		float distance = tmp.set(target).dst2(pos);
		if (minRange * minRange < distance && distance < maxRange * maxRange) {

			tmp.set(target).sub(pos);
			float angleDelta = Angle.angleDelta(rotation, tmp.angle());
			if (Math.abs(angleDelta) > 5) {
				if (angleDelta > 0)
					vehicle.turnLeft();
				else
					vehicle.turnRight();
			} else {
				vehicle.accelerate();
			}
		} else {
			vehicle.brake();
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
		// TODO Implement
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
