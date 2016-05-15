package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import android.util.Log;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;

public class ProjectileBehavior extends BehaviorComponent {

	static Vector2 tmp = new Vector2();

	@Override
	public void update(float delta) {
		PhysicsComponent physics = gameObject.getComponent(PhysicsComponent.class);
		if (physics == null) return;

		// ALT: Delete if it's outside the player's range
		tmp.set(gameObject.getScreen().getGameObject("StrikeBase").getComponent(PhysicsComponent.class).getPosition());

		// Destroy bullet if too far
		tmp.sub(physics.getPosition());
		if (tmp.len2() > 6 * 6 * GameConfig.TILE_SIZE * GameConfig.TILE_SIZE)
			gameObject.destroy();

	}
}
