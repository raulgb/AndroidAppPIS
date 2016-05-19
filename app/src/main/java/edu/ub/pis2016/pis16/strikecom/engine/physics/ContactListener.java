package edu.ub.pis2016.pis16.strikecom.engine.physics;

public interface ContactListener {

	class CollisionEvent {
		public Body a;
		public Body b;
		public float contactX, contactY;
	}

	void onCollision(CollisionEvent ce);

}
