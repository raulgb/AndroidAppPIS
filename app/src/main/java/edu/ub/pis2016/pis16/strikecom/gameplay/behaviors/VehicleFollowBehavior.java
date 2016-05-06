package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.gameplay.Vehicle;

public class VehicleFollowBehavior extends BehaviorComponent {

	Vector2 target = new Vector2();
	Vector2 tmp = new Vector2();
	boolean moveOrder;

	@Override
	public void update(float delta) {
		if (!moveOrder)
			return;

		Vector2 pos = gameObject.getComponent(PhysicsComponent.class).getPosition();
		float rotation = gameObject.getComponent(PhysicsComponent.class).getRotation();
		Vehicle vehicle = (Vehicle) gameObject;

		// Move AI, strikebase follows the move pointer
		if (tmp.set(target).sub(pos).len2() > 10 * 10) {
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

			// If we reach the destination and are stopped, stop following target.
			if (!gameObject.getComponent(PhysicsComponent.class).getVelocity().notZero())
				moveOrder = false;
		}
	}

	/** Set the position to go to. if {@code null} is passed, will stop immediately. */
	public void setTarget(Vector2 target) {
		if (target == null) {
			moveOrder = false;
			return;
		}
		this.target.set(target);
		moveOrder = true;
	}
}
