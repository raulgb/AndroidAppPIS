package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;

/**
 * First test of a working enemy
 * <p/>
 * Created by German Dempere on 03/05/2016.
 */
public class EnemyTest extends Vehicle {

	PhysicsComponent physics;

	private float x, y, speed, maxSpeed = 10;

	// Anchors
	private Vector2 turret_0 = new Vector2();

	public EnemyTest() {
		super();

		physics = new PhysicsComponent(new KinematicBody(new Rectangle(32, 32)));
		putComponent(new VehicleFollowBehavior());
		putComponent(physics);
		putAnchor("turret_0", turret_0);

	}

	public void update(float delta){



		turret_0.set(physics.getPosition());

	}

	@Override
	public void turnLeft() {

	}

	@Override
	public void turnRight() {

	}

	@Override
	public void accelerate() {

	}

	@Override
	public void brake() {

	}
}
