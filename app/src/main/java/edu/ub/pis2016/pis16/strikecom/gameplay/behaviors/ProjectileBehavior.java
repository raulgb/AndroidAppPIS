package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;

/** Behavior to remove bullets after a certain time has passed */
public class ProjectileBehavior extends BehaviorComponent {
	/** Projectile lifetime in seconds */
	public float life = 10;

	@Override
	public void update(float delta) {
		life -= delta;
		if (life <= 0)
			destroy();
	}
}
