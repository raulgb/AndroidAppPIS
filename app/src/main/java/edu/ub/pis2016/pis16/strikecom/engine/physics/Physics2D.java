package edu.ub.pis2016.pis16.strikecom.engine.physics;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.util.Pool;

public class Physics2D {

	private ArrayList<Body> staticBodies; // all   staticBodies here
	private ArrayList<Body> dynamicBodies; // Dynamic + Kinematic
	private Vector2 gravity; // not sure if needed

	private Vector2 tmp = new Vector2();

	private float worldWidth;
	private float worldHeight;
	private float scale; //lets store here  grafix/physics scaling rate here for now

	private Set<Body> collidedBodies = new HashSet<>();
	private SpatialHashGrid spatialHashGrid; // collision detection optimisation

	private Pool<ContactListener.CollisionEvent> cePool;
	private ArrayList<ContactListener> listeners;

	public float getWorldWidth() {
		return worldWidth;
	}

	public float getWorldHeight() {
		return worldHeight;
	}

	public float getScale() {
		return scale;
	}

	/**
	 * creates 2d physics world
	 *
	 * @param worldWidth  width of world
	 * @param worldHeight height of world
	 * @param gravity     gravity vector, not sure if needed
	 */
	public Physics2D(float worldWidth, float worldHeight, Vector2 gravity) {
		this.staticBodies = new ArrayList<>(); //initialize list of staticBodies
		this.dynamicBodies = new ArrayList<>(); //initialize list of dynamicBodies
		this.gravity = gravity;

		// HashGrid Init
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		float cellSize = worldHeight / 8f;
		spatialHashGrid = new SpatialHashGrid(worldWidth, worldHeight, cellSize);

		// Listener and EventPool init
		listeners = new ArrayList<>();
		cePool = new Pool<>(new Pool.PoolObjectFactory<ContactListener.CollisionEvent>() {
			@Override
			public ContactListener.CollisionEvent createObject() {
				return new ContactListener.CollisionEvent();
			}
		}, 128);

	}

	public void update(float delta) {//update all Bodies of the game
		spatialHashGrid.clearDynamicCells(); //clear previous cells numbers assigned to dynamic objects

		//update velocity and positions of all dynamic bodies
		for (Body body : dynamicBodies) {

			// Only update Dynamic Bodies
			if (body instanceof DynamicBody) {
				DynamicBody dynBody = (DynamicBody) body;
				// Update vel
				tmp.set(dynBody.getAcceleration());
				dynBody.setVelocity(dynBody.getVelocity().add(tmp.scl(delta)));
				// Update position
				tmp.set(dynBody.getVelocity());
				body.setPosition(body.getPosition().add(tmp.scl(delta)));
			}

			this.spatialHashGrid.insertDynamicObject(body); // insert back to  hash grid
		}

		// Handle collisions
		collidedBodies.clear();
		for (Body bodyA : dynamicBodies) {
			List<Body> potentials = spatialHashGrid.getPotentialColliders(bodyA);
			for (Body bodyB : potentials) {

				// Skip already collided
				if (collidedBodies.contains(bodyB))
					continue;

				// Detect Collision
				// todo: every object still checks collision with itself, probably there is a better way to fix this than simple if
				if (bodyA!=bodyB && bodyA.getBounds().overlaps(bodyB.getBounds())) {
					// Create collision event
					ContactListener.CollisionEvent cEvent = cePool.newObject();
					cEvent.a = bodyA;
					cEvent.b = bodyB;
					// set contact to middle-point between centers
					tmp.set(bodyA.getBounds().getCenter()).lerp(bodyB.getBounds().getCenter(), 0.5f);

					cEvent.contactX = tmp.x;
					cEvent.contactY = tmp.y;

					// Pass along to listeners
					for (ContactListener cl : listeners)
						cl.onCollision(cEvent);

					collidedBodies.add(bodyB);
				}
			}
		}

	}

	/** Static bodies */
	public void addStaticBody(Body b) {//add static body to physics engine
		this.staticBodies.add(b);
		this.spatialHashGrid.insertStaticObject(b); //warning

	}

	/** Dynamic and Kinematic bodies */
	public void addDynamicBody(Body b) {//add dynamic body to physics engine
		this.dynamicBodies.add(b);
	}

	public void addContactListener(ContactListener cl) {
		listeners.add(cl);
	}
}
/*
* http://box2d.org/manual.pdf
* https://github.com/erincatto/Box2D
*
* */