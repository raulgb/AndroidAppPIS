package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import android.util.Log;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;

public class ProjectileBehavior extends BehaviorComponent {

	Vector2 tmp = new Vector2();

	@Override
	public void update(float delta) {
		PhysicsComponent physics = gameObject.getComponent(PhysicsComponent.class);
		// Update Position
		tmp.set(physics.getVelocity());
		physics.getPosition().add(tmp.scl(delta));
		physics.setRotation(tmp.angle());

		// TODO Add collision detection here

		// Delete bullet if it's too far from parent object (Object that shot it)
		tmp.set(gameObject.getParent().getComponent(PhysicsComponent.class).getPosition());
		tmp.sub(physics.getPosition());
		if (tmp.len2() > 200 * 200) {
			((DummyGLScreen) (gameObject.getScreen())).projectilePool.free(gameObject);
			gameObject.getScreen().removeGameObject(gameObject);
		}
	}
}
