package edu.ub.pis2016.pis16.strikecom.entity;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

public class StrikeBaseTest implements Vehicle {

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

	private float leftThreadVel;
	private float rightThreadVel;

	private float rotation;


	// Sprite information
	private int numDmgHulls = 3;
	private int threadFrames = 4;

	private float threads_leftAnimAccum = 0;
	private float threads_rightAnimAccum = 0;

	private float threads_leftAnimDelay = 0.25f;
	private float threads_rightAnimDelay = 0.25f;

	private int threads_leftFrame = 0;
	private int threads_rightFrame = 0;

	public StrikeBaseTest(String model) {
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
		hull.setScale(8, 8);

		threads_left = new TextureSprite(sbmk1_threads[LEFT][0]);
		threads_left.setScale(8, 8);

		threads_right = new TextureSprite(sbmk1_threads[RIGHT][0]);
		threads_right.setScale(8, 8);
	}

	@Override
	public void update(float delta) {
		threads_leftAnimAccum += delta;
		threads_rightAnimAccum += delta;
		if (threads_leftAnimAccum >= threads_leftAnimDelay) {
			threads_leftAnimAccum -= threads_leftAnimDelay;
			threads_leftFrame = (threads_leftFrame + 1) % threadFrames;
		}
		if (threads_rightAnimAccum >= threads_rightAnimDelay) {
			threads_rightAnimAccum -= threads_rightAnimDelay;
			threads_rightFrame = (threads_rightFrame + 1) % threadFrames;
		}

		threads_leftAnimDelay = 1 - Math.abs(leftThreadVel) * delta;
		threads_rightAnimDelay = 1 - Math.abs(rightThreadVel) * delta;

		// Tank-like controls
		rotation += (-leftThreadVel + rightThreadVel) * delta;

		vel.set(leftThreadVel + rightThreadVel, 0).rotate(rotation);
		pos.add(vel.scl(0.5f * delta));
	}

	@Override
	public void draw(SpriteBatch batch) {
		threads_left.setRegion(sbmk1_threads[LEFT][threads_leftFrame]);
		threads_right.setRegion(sbmk1_threads[RIGHT][threads_leftFrame]);
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
		this.rightThreadVel += 2f;
		this.leftThreadVel -= 1f;
	}

	@Override
	public void turnRight() {
		this.leftThreadVel += 2f;
		this.rightThreadVel -= 1f;
	}

	@Override
	public void accelerate() {
		this.leftThreadVel += 2;
		this.rightThreadVel += 2;
	}

	@Override
	public void brake() {

	}

	public void setPosition(float x, float y) {
		this.pos.set(x, y);
	}
}
