package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Body;
import edu.ub.pis2016.pis16.strikecom.engine.physics.DynamicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Animation;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;

public class StrikeBaseTest extends Vehicle {

	private final int LEFT = 0, RIGHT = 1;

	private TextureSprite hull;
	private TextureSprite leftThreads;
	private TextureSprite rightThreads;

	private TextureRegion[] sbmk1_hull;
	private TextureRegion[][] sbmk1_threads;

	private PhysicsComponent physics;
	private Vector2 tmp = new Vector2();

	// Physics
	private float health = 1000;

	private float maxSpeed = 25;
	private float maxTurnSpeed = maxSpeed / 1f;
	private float maxReverseSpeed = maxSpeed / 2f;
	private float accel = 2;
	private float leftThreadVel;
	private float rightThreadVel;

	// Anchors
	private Vector2 turret_0 = new Vector2();
	private Vector2 turret_1 = new Vector2();
	private Vector2 turret_2 = new Vector2();
	private Vector2 turret_3 = new Vector2();

	private Vector2 pivot = new Vector2();
	private Vector2 leftThread = new Vector2();
	private Vector2 rightThread = new Vector2();

	private Animation[] threadAnim;

	public StrikeBaseTest(StrikeBaseConfig cfg) {
		super();

		//  Create Physics component
		Rectangle rect = new Rectangle(32,32);
		Body b = new DynamicBody(rect);
		physics = new PhysicsComponent(b);
		putComponent(physics);

		// TODO Read config parameters for each modelName from a file or something
		String model = cfg.modelName;

		sbmk1_hull = new TextureRegion[cfg.animHullFrames];
		for (int i = 0; i < cfg.animHullFrames; i++)
			sbmk1_hull[i] = Assets.SPRITE_ATLAS.getRegion(model + "_hull", i);

		sbmk1_threads = new TextureRegion[2][cfg.animThreadFrames];
		for (int i = 0; i < cfg.animThreadFrames; i++) {
			sbmk1_threads[LEFT][i] = Assets.SPRITE_ATLAS.getRegion(model + "_threads_left", i);
			sbmk1_threads[RIGHT][i] = Assets.SPRITE_ATLAS.getRegion(model + "_threads_right", i);
		}

		// Create sprites
		hull = new TextureSprite(sbmk1_hull[0]);
		leftThreads = new TextureSprite(sbmk1_threads[LEFT][0]);
		rightThreads = new TextureSprite(sbmk1_threads[RIGHT][0]);

		// Animations
		threadAnim = new Animation[2];
		threadAnim[0] = new Animation(cfg.animThreadFrames);
		threadAnim[0].setFrameSpeed(0);
		threadAnim[1] = new Animation(cfg.animThreadFrames);
		threadAnim[1].setFrameSpeed(0);

		// Create and put anchors
		this.putAnchor("turret_0", turret_0);
		this.putAnchor("turret_1", turret_1);
		this.putAnchor("turret_2", turret_2);
		this.putAnchor("turret_3", turret_3);

		this.putAnchor("pivot", pivot);
		this.putAnchor("left_thread", leftThread);
		this.putAnchor("right_thread", rightThread);
	}

	@Override
	public void update(float delta) {
		threadAnim[0].setFrameSpeed(leftThreadVel);
		threadAnim[1].setFrameSpeed(rightThreadVel);

		for (Animation a : threadAnim)
			a.update(delta);

		// Tank-like controls VERSION 1
//		rotation += (-leftThreadVel + rightThreadVel) / (32) * delta;
//		vel.set(leftThreadVel + rightThreadVel, 0).rotate(rotation);
//		pos.add(vel.scl(0.5f * delta));

		// Tank-like controls VERSION 2
		final float width = 28;
		float rotDelta = (-leftThreadVel + rightThreadVel) / width;
		Vector2 pos = physics.getPosition();
		float rotation = physics.getRotation();

		leftThread.set(0, width / 2f).rotate(physics.getRotation()).add(pos);
		rightThread.set(0, -width / 2f).rotate(physics.getRotation()).add(pos);

		if (rightThreadVel > leftThreadVel)
			pivot.set(leftThread);
		else if (leftThreadVel > rightThreadVel)
			pivot.set(rightThread);
		else
			pivot.set(physics.getPosition());

		Vector2 threadToCenter = new Vector2(physics.getPosition()).sub(pivot);
		threadToCenter.rotate(rotDelta * delta);
		pos.set(pivot).add(threadToCenter);

		tmp.set(leftThreadVel + rightThreadVel, 0).scl(0.5f).rotate(rotation);
		pos.add(tmp.scl(delta));

		rotation = (rotation + rotDelta) % 360;
		if (rotation < 0)
			rotation = 360 + rotation;

		// Commit rotation changes
		physics.setRotation(rotation);

		// TODO Make this more universal, range 0-1 and depending on actual size (game units)
		turret_0.set(-8, 8).scl(hull.getScale()).rotate(rotation).add(pos);
		turret_1.set(8, 8).scl(hull.getScale()).rotate(rotation).add(pos);
		turret_2.set(-8, -8).scl(hull.getScale()).rotate(rotation).add(pos);
		turret_3.set(8, -8).scl(hull.getScale()).rotate(rotation).add(pos);

		// Commit rotation changes
		physics.setRotation(rotation);
		physics.setVelocity(5,0);

		super.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		Vector2 pos = physics.getPosition();
		float rotation = physics.getRotation();

		leftThreads.setRegion(sbmk1_threads[LEFT][threadAnim[0].frame()]);
		rightThreads.setRegion(sbmk1_threads[RIGHT][threadAnim[1].frame()]);
		hull.setRegion(sbmk1_hull[0]);

		leftThreads.setRotation(rotation);
		rightThreads.setRotation(rotation);
		hull.setRotation(rotation);

		leftThreads.draw(batch, pos.x, pos.y);
		rightThreads.draw(batch, pos.x, pos.y);
		hull.draw(batch, pos.x, pos.y);
	}

	/**
	 * Turn the strikebase counter-clock wise, accelerating the right thread forward
	 */
	@Override
	public void turnLeft() {
		this.rightThreadVel = Math.min(rightThreadVel + accel, maxTurnSpeed);
		this.leftThreadVel = Math.min(Math.max(leftThreadVel - accel / 2f, -maxReverseSpeed), maxTurnSpeed);
	}

	@Override
	public void turnRight() {
		this.leftThreadVel = Math.min(leftThreadVel + accel, maxTurnSpeed);
		this.rightThreadVel = Math.min(Math.max(rightThreadVel - accel / 2f, -maxReverseSpeed), maxTurnSpeed);
	}

	@Override
	public void accelerate() {
		this.leftThreadVel = Math.min(leftThreadVel + accel, maxSpeed);
		this.rightThreadVel = Math.min(rightThreadVel + accel, maxSpeed);
	}

	@Override
	public void brake() {
		this.leftThreadVel = Math.max(leftThreadVel - accel, 0);
		this.rightThreadVel = Math.max(rightThreadVel - accel, 0);
	}

}
