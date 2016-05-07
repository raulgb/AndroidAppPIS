package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;

/**
 * Behavior of a generic turret.
 */
public class TurretBehavior extends BehaviorComponent {

	String targetTag = null;
	GameObject target = null;

	Vector2 tmp = new Vector2();
	float counter = 0;
	float shootFreq = 0.5f;

	@Override
	public void update(float delta) {
		counter += delta;
		PhysicsComponent turretPhys = gameObject.getComponent(PhysicsComponent.class);

		if (targetTag == null || target == null) {
			// If no target, look forward
			tmp.set(50, 0).rotate(gameObject.getParent().getComponent(PhysicsComponent.class).getRotation());
			turretPhys.lookAt(tmp, 0.05f);
		}

		// If we have no target, try to find our next target
		if (target == null || isTooFar(target)) {
			for (GameObject go : gameObject.getScreen().getGameObjects())
				if (go.getTag().contains(targetTag) && !isTooFar(go)) {
					target = go;
					break;
				}
		}

		// If we have a target, aim at it
		if (target != null) {
			PhysicsComponent targetPhys = target.getComponent(PhysicsComponent.class);
			turretPhys.lookAt(targetPhys.getPosition(), 0.1f);

			// Shooting timeout
			if (counter < shootFreq)
				return;
			counter = 0;

			float shootAngle = tmp.set(targetPhys.getPosition()).sub(turretPhys.getPosition()).angle();

			if (Math.abs(Angle.angleDelta(turretPhys.getRotation(), shootAngle)) < 3f) {
				GameObject projectile = ((DummyGLScreen) gameObject.getScreen()).projectilePool.newObject();

				projectile.setParent(gameObject);
				PhysicsComponent projPhys = projectile.getComponent(PhysicsComponent.class);

				// Set position, velocity and rotation;
				projPhys.setPosition(turretPhys.getPosition());
				projPhys.setVelocity(tmp.set(90f, 0).rotate(turretPhys.getRotation()));
				projPhys.setRotation(turretPhys.getRotation());

				// set the tag to "playerProj" or "enemyProj"
				projectile.setTag(gameObject.getParent().getTag() + "_proj");

				projectile.setLayer(Screen.LAYER_1);
				gameObject.getScreen().addGameObject(projectile);
			}
		}
	}

	public void setTargetTag(String tag) {
		this.targetTag = tag;
	}

	private boolean isTooFar(GameObject o) {
		return false;
	}
}
