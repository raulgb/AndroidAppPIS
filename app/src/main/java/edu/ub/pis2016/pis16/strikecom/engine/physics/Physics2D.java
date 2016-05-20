package edu.ub.pis2016.pis16.strikecom.engine.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILE_SIZE;

public class Physics2D {

	/** StaticBodies here */
	private Array<Body> staticBodies = new Array<>();
	/** Dynamic + Kinematic */
	private Array<Body> dynamicBodies = new Array<>();

	private Vector2 tmp = new Vector2();

	private float worldWidth;
	private float worldHeight;

	private Array<Body> collidedBodies = new Array<>();
	private SpatialHashGrid spatialHashGrid;

	private Array<ContactListener> listeners = new Array<>();

	private ContactListener.CollisionEvent cEvent = new ContactListener.CollisionEvent();

	/**
	 * creates 2d physics world
	 *
	 * @param worldWidth  width of world in tiles
	 * @param worldHeight height of world in tiles
	 */
	public Physics2D(float worldWidth, float worldHeight) {
		// HashGrid Init
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		float cellSize = 16; // 16*16 tiles per cell
		spatialHashGrid = new SpatialHashGrid(worldWidth, worldHeight, cellSize);
	}

	/** World width in tiles */

	public int getWorldWidth() {
		return MathUtils.roundPositive(worldWidth);
	}

	/** World height in tiles */
	public int getWorldHeight() {
		return MathUtils.roundPositive(worldHeight);
	}

	public void update(float delta) {//update all Bodies of the game
		spatialHashGrid.clearDynamicCells(); //clear previous cells numbers assigned to dynamic objects

		//update velocity and positions of all dynamic bodies
		for (Body body : dynamicBodies) {

			// Only update Dynamic Bodies
			if (body instanceof DynamicBody) {
				DynamicBody dynBody = (DynamicBody) body;
				// Update vel
				dynBody.velocity.add(tmp.set(dynBody.acceleration).scl(delta));
				// Update position
				dynBody.position.add(tmp.set(dynBody.velocity).scl(delta));

				// Friction
				dynBody.friction = MathUtils.clamp(dynBody.friction, 0, 1);
				dynBody.velocity.scl(1f - dynBody.friction * delta);

				// keep in world bounds
				dynBody.position.x = MathUtils.max(0.1f, dynBody.position.x);
				dynBody.position.x = MathUtils.min(worldWidth - 0.1f, dynBody.position.x);
				dynBody.position.y = MathUtils.max(0.1f, dynBody.position.y);
				dynBody.position.y = MathUtils.min(worldHeight - 0.1f, dynBody.position.y);
			}

			this.spatialHashGrid.insertDynamicObject(body); // insert back to  hash grid
		}

		// Handle collisions
		collidedBodies.clear();
		for (Body bodyA : dynamicBodies) {
			if (collidedBodies.contains(bodyA))
				continue;

			Array<Body> potentials = spatialHashGrid.getPotentialColliders(bodyA);

			for (Body bodyB : potentials) {
				if (bodyA == bodyB)
					continue;


				// Detect Collision
				if (bodyA.collide(bodyB)) {
					cEvent.a = bodyA;
					cEvent.b = bodyB;

					// TODO Calculate point of impact
//					cEvent.contactX = 0;
//					cEvent.contactY = 0;

					// Pass along to listeners
					for (ContactListener cl : listeners)
						cl.onCollision(cEvent);

					collidedBodies.add(bodyB);
				}
			}
		}
		//Log.i("Physics2D", "Total tested: " + tested);
	}

	/** Static bodies */
	protected void addStaticBody(Body b) {
		this.staticBodies.add(b);
		this.spatialHashGrid.insertStaticObject(b); //warning
	}

	/** Dynamic and Kinematic bodies */
	protected void addDynamicBody(Body b) {//add dynamic body to physics engine
		this.dynamicBodies.add(b);
	}

	public void addContactListener(ContactListener cl) {
		listeners.add(cl);
	}

	public void addBody(Body body) {
		//Log.d("Physics2D", "Added new body: " + body.userData);

		if (body instanceof StaticBody)
			addStaticBody(body);
		else
			addDynamicBody(body);
	}

	public boolean removeBody(Body body) {
		//Log.d("Physics2D", "Removed body: " + body.userData);

		spatialHashGrid.removeObject(body);
		if (staticBodies.removeValue(body, true))
			return true;
		if (dynamicBodies.removeValue(body, true))
			return true;
		return false;
	}

	public Array<Body> getStaticBodies() {
		return staticBodies;
	}

	public Array<Body> getDynamicBodies() {
		return dynamicBodies;
	}

	private Sprite hitbox = new Sprite(Assets.SPRITE_ATLAS.getRegion("hitbox"));
	private Sprite hitbox_round = new Sprite(Assets.SPRITE_ATLAS.getRegion("hitbox_round"));

	public void debugDraw(SpriteBatch batch) {
		for (Body b : getDynamicBodies()) {
			Shape bounds = b.getBounds();

			if (bounds instanceof Rectangle) {
				hitbox.setPosition(tmp.set(b.position).scl(TILE_SIZE));
				hitbox.setSize(bounds.getWidth() * TILE_SIZE, bounds.getHeight() * TILE_SIZE);
				hitbox.setRotation(bounds.getRotation());
				hitbox.draw(batch);

			} else if (bounds instanceof Circle) {
				hitbox_round.setPosition(tmp.set(b.position).scl(TILE_SIZE));
				hitbox_round.setSize(bounds.getWidth() * TILE_SIZE);
				hitbox_round.draw(batch);
			}

		}
	}
}
/*
* http://box2d.org/manual.pdf
* https://github.com/erincatto/Box2D
*
* */