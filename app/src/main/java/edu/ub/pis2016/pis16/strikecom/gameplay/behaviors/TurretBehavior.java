package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.gameplay.Turret;

/**
 * Behavior of an allied turret.
 */
public class TurretBehavior extends BehaviorComponent {

	GameObject target = null;
	GameObject owner = null;

	@Override
	public void update(float delta) {
		if (owner == null) {
			Turret turret = ((Turret) gameObject);
			owner = turret.getOwner();
		}

		PhysicsComponent physics = gameObject.getComponent(PhysicsComponent.class);

		// If we have no target, try to find our next target
		if (target == null || isTooFar(target))
			for (GameObject go : gameObject.getScreen().getGameObjects())
				if (go.getTag().equals("enemy") && !isTooFar(go))
					target = go;

		// If we have a target, aim at it
		if (target != null)
			physics.lookAt(target.getComponent(PhysicsComponent.class).getPosition(), 0.1f);


	}

	private boolean isTooFar(GameObject o) {
		return false;
	}
}
