package edu.ub.pis2016.pis16.strikecom.entity;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Animation;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

public class StrikeBaseTest extends Vehicle {

	private final int LEFT = 0, RIGHT = 1;

	private TextureSprite hull;
	private TextureSprite threads_left;
	private TextureSprite threads_right;

	private TextureRegion[] sbmk1_hull;
	private TextureRegion[][] sbmk1_threads;

	// Physics
	private float health = 1000;
	private Vector2 pos;
	private Vector2 vel;
	private Vector2 tmp;

	private float maxSpeed = 250;
	private float maxTurnSpeed = maxSpeed / 6f;
	private float accel = 2;
	private float leftThreadVel;
	private float rightThreadVel;

	private float rotation;

	private Vector2 turret_1;
	private Vector2 turret_2;
	private Vector2 turret_3;
	private Vector2 turret_4;

	// Sprite information
	private int numDmgHulls = 3;
	private int threadFrames = 4;

	private Animation[] threadAnim;

	public StrikeBaseTest(String model) {
		super();
		pos = new Vector2();
		vel = new Vector2();
		tmp = new Vector2();

		sbmk1_hull = new TextureRegion[numDmgHulls];
		for (int i = 0; i < numDmgHulls; i++)
			sbmk1_hull[i] = Assets.SPRITE_ATLAS.getRegion(model + "_hull", i);

		sbmk1_threads = new TextureRegion[2][threadFrames];
		for (int i = 0; i < threadFrames; i++) {
			sbmk1_threads[LEFT][i] = Assets.SPRITE_ATLAS.getRegion(model + "_threads_left", i);
			sbmk1_threads[RIGHT][i] = Assets.SPRITE_ATLAS.getRegion(model + "_threads_right", i);
		}

		// Create sprites
		hull = new TextureSprite(sbmk1_hull[0]);
		hull.setScale(10, 10);

		threads_left = new TextureSprite(sbmk1_threads[LEFT][0]);
		threads_left.setScale(10, 10);

		threads_right = new TextureSprite(sbmk1_threads[RIGHT][0]);
		threads_right.setScale(10, 10);

		// Animations
		threadAnim = new Animation[2];
		threadAnim[0] = new Animation(threadFrames);
		threadAnim[0].setFrameSpeed(0);
		threadAnim[1] = new Animation(threadFrames);
		threadAnim[1].setFrameSpeed(0);

		// Create and put anchors
		turret_1 = new Vector2();
		turret_2 = new Vector2();
		turret_3 = new Vector2();
		this.putAnchor("turret_1", turret_1);
		this.putAnchor("turret_2", turret_2);
		this.putAnchor("turret_3", turret_3);
	}

	@Override
	public void update(float delta) {
		threadAnim[0].setFrameSpeed(leftThreadVel * 0.1f);
		threadAnim[1].setFrameSpeed(rightThreadVel * 0.1f);

		for (Animation a : threadAnim)
			a.update(delta);

		// Tank-like controls
		rotation += (-leftThreadVel + rightThreadVel) * delta;
		vel.set(leftThreadVel + rightThreadVel, 0).rotate(rotation);
		pos.add(vel.scl(0.5f * delta));

		// TODO Make this more universal, range 0-1 and depending on actual size (game units)
		turret_1.set(8, -8).scl(hull.getScale()).rotate(rotation).add(pos);
		turret_2.set(-8, 8).scl(hull.getScale()).rotate(rotation).add(pos);
		turret_3.set(-8, -8).scl(hull.getScale()).rotate(rotation).add(pos);

	}

	@Override
	public void draw(SpriteBatch batch) {
		threads_left.setRegion(sbmk1_threads[LEFT][threadAnim[0].frame()]);
		threads_right.setRegion(sbmk1_threads[RIGHT][threadAnim[1].frame()]);
		hull.setRegion(sbmk1_hull[0]);

		threads_left.setRotation(rotation);
		threads_right.setRotation(rotation);
		hull.setRotation(rotation);

		threads_left.draw(batch, pos.x, pos.y);
		threads_right.draw(batch, pos.x, pos.y);
		hull.draw(batch, pos.x, pos.y);
	}

	@Override
	public void turnLeft() {
		this.rightThreadVel = Math.min(rightThreadVel + accel, maxTurnSpeed);
		this.leftThreadVel = Math.min(Math.max(leftThreadVel - accel / 2f, -maxTurnSpeed/2f), maxTurnSpeed);
	}

	@Override
	public void turnRight() {
		this.leftThreadVel = Math.min(leftThreadVel + accel, maxTurnSpeed);
		this.rightThreadVel = Math.min(Math.max(rightThreadVel - accel / 2f, -maxTurnSpeed/2f), maxTurnSpeed);
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

	public void setPosition(float x, float y) {
		this.pos.set(x, y);
	}

	public Vector2 getPosition() {
		return pos;
	}

	public float getRotation() {
		return rotation;
	}
}
