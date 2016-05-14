package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;

/**
 * Behavior of an allied turret.
 */
public class CustomTurretBehavior extends BehaviorComponent {

	GameObject target = null;

	Vector2 tmp = new Vector2();
	float counter = 0;
	float shootFreq;
	float lerpSpeed;

	public CustomTurretBehavior() {
		this.shootFreq = 0.5f;
		this.lerpSpeed = 0.15f;
	}


	public CustomTurretBehavior(float shootFreq, float lerpSpeed) {
		this.shootFreq = shootFreq;
		this.lerpSpeed = lerpSpeed;
	}

	public void setShootFreq(float shootFreq) {
		this.shootFreq = shootFreq;
	}

	public void setLerpSpeed(float lerpSpeed) {
		this.lerpSpeed = lerpSpeed;
	}

	public void setShootFreq(int shootFreq) {
		this.shootFreq *= (shootFreq/5);
	}

	public void setLerpSpeed(int lerpSpeed) {
		this.lerpSpeed *= (lerpSpeed/5);
	}

	@Override
	public void update(float delta) {
		counter += delta;

		PhysicsComponent physics = gameObject.getComponent(PhysicsComponent.class);

		// If we have no target, try to find our next target
		if (target == null || isTooFar(target))
			for (GameObject go : gameObject.getScreen().getGameObjects())
				if (go.getTag().equals("enemy") && !isTooFar(go)) {
					target = go;
					break;
				}

		// If we have a target, aim at it
		if (target != null) {
			PhysicsComponent targetPhys = target.getComponent(PhysicsComponent.class);
			physics.lookAt(targetPhys.getPosition(), lerpSpeed);

			// Shooting timeout
			if (counter < shootFreq)
				return;
			counter = 0;

			float shootAngle = tmp.set(targetPhys.getPosition()).sub(physics.getPosition()).angle();

			if (Math.abs(Angle.angleDelta(physics.getRotation(), shootAngle)) < 3f) {
				GameObject projectile = ((DummyGLScreen) gameObject.getScreen()).projectilePool.newObject();
				projectile.setParent(gameObject);

				PhysicsComponent projPhys = projectile.getComponent(PhysicsComponent.class);
				// Set position, velocity and rotation;
				projPhys.getPosition().set(physics.getPosition());
				projPhys.getVelocity().set(90f, 0).rotate(physics.getRotation());

				projectile.setTag("player");
				projectile.setLayer(Screen.LAYER_STRIKEBASE);
				gameObject.getScreen().addGameObject(projectile);
			}
		}


	}

	private boolean isTooFar(GameObject o) {
		return false;
	}
}