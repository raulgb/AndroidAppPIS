package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.VehicleConfig;

/**
 * Template for threaded vehicles. Can be used as-is. Has one single anchor in the midle of the
 * vehicle named "turret" for usage with turrets.
 * <p/>
 * Created by Herman Dempere on 03/05/2016.
 */
public class ThreadVehicle extends Vehicle {

	PhysicsComponent physics;

	// Current accel
	private float leftThreadAccel = 0;
	private float rightThreadAccel = 0;

	// Current Dampening
	private float leftThreadDampening = 0;
	private float rightThreadDampening = 0;

	// Current Speed
	private float leftThreadVel;
	private float rightThreadVel;

	private Vector2 pivot = new Vector2();
	private Vector2 leftThread = new Vector2();
	private Vector2 rightThread = new Vector2();

	public VehicleConfig cfg = new VehicleConfig();
	private Vector2 tmp = new Vector2();

	// Anchors
	private Vector2 turretAnchor = new Vector2();

	public ThreadVehicle() {
		this.setLayer(Screen.LAYER_VEHICLES);

		physics = new PhysicsComponent(new KinematicBody(new Rectangle(1, 1)));
		putComponent(physics);
		GraphicsComponent graphics = new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("enemy"));
		graphics.getSprite().setSize(1f * GameConfig.TILE_SIZE);
		putComponent(graphics);

		// Config turret
		putAnchor("turret", turretAnchor);
	}

	public void update(float delta) {
		updatePhysics(delta);
		turretAnchor.set(physics.getPosition());

		super.update(delta);
	}


	@Override
	protected void updatePhysics(float delta) {
		// Tank-like controls VERSION 2
		// Update speeds
		leftThreadVel = MathUtils.clamp(leftThreadVel + leftThreadAccel * delta, -cfg.maxSpeed, cfg.maxSpeed);
		rightThreadVel = MathUtils.clamp(rightThreadVel + rightThreadAccel * delta, -cfg.maxSpeed, cfg.maxSpeed);

		rightThreadVel *= 1 - rightThreadDampening * delta;
		leftThreadVel *= 1 - leftThreadDampening * delta;

		final float width = 0.8f * getComponent(GraphicsComponent.class).getSprite().getSize();
		float rotSpeed = (-leftThreadVel + rightThreadVel) / width;

		Vector2 pos = physics.getPosition();
		float rotation = physics.getRotation();

		leftThread.set(0, width / 2f).rotate(rotation).add(pos);
		rightThread.set(0, -width / 2f).rotate(rotation).add(pos);

		// Pivot around either threads or center
		if (rightThreadVel > leftThreadVel)
			pivot.set(leftThread);
		else if (leftThreadVel > rightThreadVel)
			pivot.set(rightThread);
		else
			pivot.set(pos);

		Vector2 threadToCenter = new Vector2(pos).sub(pivot);
		threadToCenter.rotate(rotSpeed * delta);
		pos.set(pivot).add(threadToCenter);

		// Average thread velocity and rotate to get a velocity vector
		tmp.set(leftThreadVel + rightThreadVel, 0).scl(0.5f).rotate(rotation);
		pos.add(tmp.scl(delta));

		rotation = (rotation + rotSpeed) % 360;
		if (rotation < 0)
			rotation = 360 + rotation;

		// Commit Physics changes
		physics.setVelocity(tmp.set((leftThreadVel + rightThreadVel) / 2f, 0).rotate(rotation));
		physics.setRotation(rotation);
		physics.setPosition(pos);
	}

	@Override
	public void turnLeft() {
		this.rightThreadAccel = cfg.accel * 0.75f;
		this.leftThreadAccel = -cfg.accel * 0.25f;

		this.leftThreadDampening = 0f;
		this.rightThreadDampening = 0f;
	}

	@Override
	public void turnRight() {
		this.leftThreadAccel = cfg.accel * 0.75f;
		this.rightThreadAccel = -cfg.accel * 0.25f;

		this.leftThreadDampening = 0f;
		this.rightThreadDampening = 0f;
	}

	@Override
	public void accelerate() {
		this.leftThreadAccel = cfg.accel;
		this.rightThreadAccel = cfg.accel;

		this.leftThreadDampening = 0f;
		this.rightThreadDampening = 0f;
	}

	@Override
	public void brake() {
		this.leftThreadAccel = 0;
		this.rightThreadAccel = 0;

		this.leftThreadDampening = 0.95f;
		this.rightThreadDampening = 0.95f;
	}

	public void reverse() {

	}
}
