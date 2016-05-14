package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;

/**
 * First test of a working enemy
 * <p/>
 * Created by German Dempere on 03/05/2016.
 */
public class TankVehicle extends Vehicle {

	PhysicsComponent physics;

	private float accel = 0f;
	private float rotAccel = 0f;

	private float speed = 0f;
	private float rotation = 0;
	private float rotSpeed = 0f;

	private float maxSpeed = 10f; /* Tiles per second */
	private float maxRotSpeed = 30f; /* degrees per second */
	private Vector2 tmp = new Vector2();

	Turret turret;
	// Anchors
	private Vector2 turret_0 = new Vector2();

	public TankVehicle() {
		super();

		this.setLayer(Screen.LAYER_VEHICLES);
		physics = new PhysicsComponent(new KinematicBody(new Rectangle(1, 1)));
		putComponent(physics);
		putComponent(new VehicleFollowBehavior());


		// Config turret
		putAnchor("turret_0", turret_0);
		turret = new Turret("enemy_turret", this, "turret_0");
		turret.getComponent(GraphicsComponent.class).getSprite().setSize(1f * GameConfig.TILE_SIZE);
		turret.putComponent(new TurretBehavior());
		turret.getComponent(TurretBehavior.class).setTargetTag("player");

		turret.setTag("enemy_tank_turret");
		turret.setParent(this);
		turret.setLayer(Screen.LAYER_VEHICLE_TURRET);

		// Sprites
		GraphicsComponent graphics = new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("enemy"));
		graphics.getSprite().setSize(1f * GameConfig.TILE_SIZE);
		putComponent(graphics);
	}

	public void update(float delta) {
		if (!screen.hasGameObject(turret))
			screen.addGameObject(turret);

		rotSpeed += rotAccel * delta;
		rotSpeed = MathUtils.min(maxRotSpeed, rotSpeed);
		rotation += rotSpeed * delta;

		speed += accel * delta;
		speed = MathUtils.min(maxSpeed, speed);

		Vector2 position = physics.getPosition();
		tmp.set(speed, 0).rotate(rotation);
		position.add(tmp.scl(delta));

		physics.setVelocity(tmp.set(speed, 0).rotate(rotation));
		physics.setPosition(position);
		physics.setRotation(rotation);

		turret_0.set(physics.getPosition());

		super.update(delta);
	}


	@Override
	public void turnLeft() {
		rotAccel = 2f;
	}

	@Override
	public void turnRight() {
		rotAccel = -2f;
	}

	@Override
	public void accelerate() {
		speed = MathUtils.min(speed + 0.1f, maxSpeed);
	}

	@Override
	public void brake() {
		if (speed > 0)
			accel = -Math.abs(accel) * 0.99f;
		else
			accel = Math.abs(accel) * 0.99f;

		rotAccel *= 0.95f;
	}
}
