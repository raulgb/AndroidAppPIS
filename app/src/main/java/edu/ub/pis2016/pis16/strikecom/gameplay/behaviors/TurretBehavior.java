package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;

/**
 * Behavior of a generic turret.
 */
public class TurretBehavior extends BehaviorComponent {

	private String targetTag = null;
	private GameObject target = null;
	private Vector2 tmp = new Vector2();

	public TurretConfig cfg;

	private State state = State.IDLE;
	private float counter = 0;

	private enum State {
		IDLE,
		SEARCHING,
		AIMING,
	}

	public TurretBehavior(TurretConfig cfg) {
		this.cfg = cfg;
	}

	@Override
	public void update(float delta) {
		counter += delta;
		PhysicsComponent turretPhys = gameObject.getComponent(PhysicsComponent.class);
		PhysicsComponent vehiclePhys = gameObject.getParent().getComponent(PhysicsComponent.class);

		switch (state) {
			case IDLE:
				// If we're idling, just look forward
				if (counter < cfg.idle_seconds) {
					tmp.set(8, 0).rotate(vehiclePhys.getRotation()).add(turretPhys.getPosition());
					turretPhys.lookAt(tmp, cfg.lerp_speed * .1f);
				} else {
					counter = 0;
					state = State.SEARCHING;
				}
				break;
			case SEARCHING:
				if (targetTag == null)
					state = State.IDLE;
				else {
					// Look for the closest gameobject in view
					float closestDistance = Float.MAX_VALUE;
					GameObject closestGO = null;

					for (GameObject go : gameObject.getScreen().getGameObjects()) {
						// Target enemies with the target tag, who are not too far, and are killable
						if (go.getTag().contains(targetTag) && !isTooFar(go) && go.killable) {
							float distance = go.getPosition().dst2(turretPhys.getPosition());
							if (distance < closestDistance) {
								closestGO = go;
								closestDistance = distance;
							}
						}
					}

					// If none found, return to idle
					if (closestGO == null)
						state = State.IDLE;
					else {
						target = closestGO;
						state = State.AIMING;
					}
				}
				break;
			case AIMING:
				if (!target.isValid() || isTooFar(target)) {
					target = null;
					state = State.SEARCHING;
				} else {
					// Move the turret towards the target position and check if it's within a 3 degree cone, shoot
					// a projectile towards it
					turretPhys.lookAt(target.getPosition(), cfg.lerp_speed);
					float turretRot = turretPhys.getRotation();
					float targetAngle = tmp.set(target.getPosition()).sub(turretPhys.getPosition()).angle();
					if (Math.abs(Angle.angleDelta(turretRot, targetAngle)) < 3f && counter > cfg.shoot_freq) {
						shoot();
						counter = 0;
					}
				}
				break;
		}

	}


	public void setTargetTag(String tag) {
		this.targetTag = tag;
	}

	private boolean isTooFar(GameObject other) {
		if (!gameObject.hasComponent(PhysicsComponent.class))
			return true;

		tmp.set(gameObject.getPosition()).sub(other.getPosition());
		return tmp.len2() > cfg.range * cfg.range;
	}

	private void shoot() {
		GameObject projectile = ((DummyGLScreen) gameObject.getScreen()).projectilePool.newObject();

		PhysicsComponent turretPhys = gameObject.getComponent(PhysicsComponent.class);
		PhysicsComponent projPhys = projectile.getComponent(PhysicsComponent.class);

		// Set position
		Sprite turretSprite = gameObject.getComponent(GraphicsComponent.class).getSprite();
		tmp.set(turretSprite.getSize() / 2f, 0).rotate(turretPhys.getRotation());
		tmp.add(turretPhys.getPosition());
		projPhys.setPosition(tmp);

		// set velocity and rotation
		projPhys.setVelocity(tmp.set(cfg.proj_speed, 0).rotate(turretPhys.getRotation()));
		projPhys.setRotation(turretPhys.getRotation());

		// set the tag to "player_proj" or "enemy_proj" and same group as parent
		projectile.setTag(gameObject.getParent().getTag() + "_proj");
		projectile.group = gameObject.group;

		// set hitpoints as damage made on impact
		projectile.hitpoints = cfg.proj_damage;

		projectile.setLayer(Screen.LAYER_PROJECTILES);
		gameObject.getScreen().addGameObject(projectile);
	}
}
