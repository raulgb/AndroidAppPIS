package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public interface ContactListener {

	public class CollisionEvent{
		public Body a;
		public Body b;
		public Vector2 normal;
	}

	public void onCollision(CollisionEvent ce);
}
