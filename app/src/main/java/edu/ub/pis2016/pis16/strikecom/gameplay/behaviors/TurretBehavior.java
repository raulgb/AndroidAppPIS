package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;

/**
 * Behavior of a generic turret.
 */
public class TurretBehavior extends BehaviorComponent {

	String targetTag = null;
	GameObject target = null;

	Vector2 tmp = new Vector2();
	public float counter = 0;
	public float shootFreq = 0.5f;
	public float lerpSpeed = 0.075f;
	public int attack = 1;

	@Override
	public void update(float delta) {
		counter += delta;
		PhysicsComponent turretPhys = gameObject.getComponent(PhysicsComponent.class);

		if (targetTag == null || target == null) {
			// If no target, look forward
			tmp.set(50, 0).rotate(gameObject.getParent().getComponent(PhysicsComponent.class).getRotation());
			turretPhys.lookAt(tmp, lerpSpeed);
		}

		// If we have no target, try to find our next target
		if (target == null || isTooFar(target)) {
			for (GameObject go : gameObject.getScreen().getGameObjects())
				if (go.getTag() != null && go.getTag().contains(targetTag) && !isTooFar(go)) {
					target = go;
					break;
				}
		}

		// If we have a target, aim at it
		if (target != null) {
			PhysicsComponent targetPhys = target.getComponent(PhysicsComponent.class);
			if(targetPhys == null)
				return;

			turretPhys.lookAt(targetPhys.getPosition(), 0.1f);

			// Shooting timeout
			if (counter < shootFreq)
				return;
			counter = 0;

			float shootAngle = tmp.set(targetPhys.getPosition()).sub(turretPhys.getPosition()).angle();

			if (Math.abs(Angle.angleDelta(turretPhys.getRotation(), shootAngle)) < 3f) {
				GameObject projectile = ((DummyGLScreen) gameObject.getScreen()).projectilePool.newObject();

				PhysicsComponent projPhys = projectile.getComponent(PhysicsComponent.class);

				// Set position
				TextureSprite turretSprite = gameObject.getComponent(GraphicsComponent.class).getSprite();
				tmp.set(turretSprite.getSize() / 2f, 0).rotate(turretPhys.getRotation());
				tmp.add(turretPhys.getPosition());
				projPhys.setPosition(tmp);

				// set velocity and rotation
				projPhys.setVelocity(tmp.set(GameConfig.BULLET_SPEED, 0).rotate(turretPhys.getRotation()));
				projPhys.setRotation(turretPhys.getRotation());

				// set the tag to "playerProj" or "enemyProj"
				projectile.setTag(gameObject.getParent().getTag() + "_proj");

				// set hitpoints as damage made on impact
				projectile.hitpoints = attack;

				projectile.setLayer(Screen.LAYER_PROJECTILES);
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
