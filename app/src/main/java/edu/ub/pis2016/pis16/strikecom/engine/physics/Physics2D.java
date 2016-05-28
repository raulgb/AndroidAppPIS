package edu.ub.pis2016.pis16.strikecom.engine.physics;

import android.util.Log;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.engine.util.Pool;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.ObjectSet;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILE_SIZE;

public class Physics2D {

	private float worldWidth;
	private float worldHeight;

	private Array<Body> dynamicBodies = new Array<>(false, 64);
	private Array<Body> staticBodies = new Array<>(false, 64);
	private Array<Body> allBodies = new Array<>(false, 128);

	private Array<Body> potentials = new Array<>(false, 16);

	private QuadTree quadTree;

	/** Special map for Bodies currenly under contact */
	private ObjectSet<ContactListener.Contact> previousContacts = new ObjectSet<>();
	private ObjectSet<ContactListener.Contact> contacts = new ObjectSet<>();

	private Array<ContactListener> listeners = new Array<>();

	Pool<ContactListener.Contact> contactPool;
	private Vector2 tmp = new Vector2();


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

		quadTree = new QuadTree(0, new Rectangle(worldWidth, worldHeight));

		contactPool = new Pool<>(new Pool.PoolObjectFactory<ContactListener.Contact>() {
			@Override
			public ContactListener.Contact createObject() {
				return new ContactListener.Contact();
			}
		}, 128);
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
		quadTree.clear();

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
		}

		for (Body body : allBodies) {
			body.updateBounds();
			quadTree.insert(body);
		}

		// Store past contacts
		previousContacts.clear();
		previousContacts.addAll(contacts);
		contacts.clear();


		// Contact that will be sent to the first collision
		ContactListener.Contact newContact = contactPool.newObject();
		int tested = 0;

		for (Body bodyA : dynamicBodies) {
			// Get potential matches
			quadTree.retrieve(potentials, bodyA);

			for (Body bodyB : potentials) {
				if (!Filter.test(bodyA.filter, bodyB.filter))
					continue;

				tested++;

				// Check if this collision did already occur this frame
				newContact.a = bodyA;
				newContact.b = bodyB;
				if (contacts.contains(newContact)) {
					continue;
				}

				// Detect Collision
				if (bodyA.bounds.overlaps(bodyB.bounds)) {
					newContact.a = bodyA;
					newContact.b = bodyB;
					tmp.set(bodyA.position).lerp(bodyB.position, .5f);
					newContact.contactX = tmp.x;
					newContact.contactY = tmp.y;

					// Store new contact
					contacts.add(newContact);

					// Pass along to listeners the first time
					if (!previousContacts.contains(newContact))
						for (ContactListener cl : listeners)
							cl.beginContact(newContact);

					// Create a new contact for the next one
					newContact = contactPool.newObject();
				}
			}
		}

		Log.i("Physics2D", String.format("nº: %4d | t: %8d", allBodies.size, tested));

		// Free the last unused contact
		contactPool.free(newContact);

		// End all obsolete contacts
		for (ContactListener.Contact oldContact : previousContacts) {
			if (!contacts.contains(oldContact)) {
				// End contact
				for (ContactListener cl : listeners)
					cl.endContact(oldContact);

				contacts.remove(oldContact);
				contactPool.free(oldContact);
			}
		}
	}

	/** Static bodies */
	protected void addStaticBody(Body b) {
		staticBodies.add(b);
		allBodies.add(b);
	}

	/** Dynamic and Kinematic bodies */
	protected void addDynamicBody(Body b) {
		dynamicBodies.add(b);
		allBodies.add(b);
	}

	public void addContactListener(ContactListener cl) {
		listeners.add(cl);
	}

	public void addBody(Body body) {
		if (body instanceof StaticBody)
			addStaticBody(body);
		else
			addDynamicBody(body);
	}

	public void removeBody(Body body) {
		allBodies.removeValue(body);
		staticBodies.removeValue(body);
		dynamicBodies.removeValue(body);
	}

	public Array<Body> getBodies() {
		return allBodies;
	}

	public Array<Body> getDynamicBodies() {
		return dynamicBodies;
	}

	private Sprite hitbox = new Sprite(Assets.SPRITE_ATLAS.getRegion("hitbox"));
	private Sprite hitbox_round = new Sprite(Assets.SPRITE_ATLAS.getRegion("hitbox_round"));

	public void debugDraw(SpriteBatch batch) {
		for (Body b : allBodies) {
			Shape bounds = b.bounds;

			switch (bounds.type) {
				case RECTANGLE:
					hitbox.setPosition(tmp.set(b.position).scl(TILE_SIZE));
					hitbox.setSize(bounds.width * TILE_SIZE, bounds.height * TILE_SIZE);
					hitbox.setRotation(bounds.rotation);
					hitbox.draw(batch);
					break;
				case CIRCLE:
					hitbox_round.setPosition(tmp.set(b.position).scl(TILE_SIZE));
					hitbox_round.setSize(bounds.radius * 2 * TILE_SIZE);
					hitbox_round.draw(batch);
					break;
			}
		}
	}

	static final int PLAYER_BIT = 1;
	static final int ENEMY_BIT = 1 << 1;
	static final int PLAYER_PROJ_BIT = 1 << 2;
	static final int ENEMY_PROJ_BIT = 1 << 3;
	static final int TERRAIN_BIT = 1 << 4;
	static final int SHOP_BIT = 1 << 5;

	/**
	 * Filters are used to determine whether two bodies collide. To collide two filters,
	 * the Category bits of A must be set on the mask bits of B, and likewise for B.
	 */
	public enum Filter {
		ALL(0x7FFFFFFF, 0x7FFFFFFF),
		PLAYER(PLAYER_BIT, ENEMY_BIT | ENEMY_PROJ_BIT | SHOP_BIT),
		ENEMY(ENEMY_BIT, PLAYER_BIT | PLAYER_PROJ_BIT),
		PLAYER_PROJ(PLAYER_PROJ_BIT, ENEMY_BIT),
		ENEMY_PROJ(ENEMY_PROJ_BIT, PLAYER_BIT),
		SHOP(SHOP_BIT, PLAYER_BIT);

		public final int categoryBits;
		public final int maskBits;

		Filter(int categoryBits, int maskBits) {
			this.categoryBits = categoryBits;
			this.maskBits = maskBits;
		}

		public static boolean isProjectile(Filter f) {
			return f == PLAYER_PROJ || f == ENEMY_PROJ;
		}

		/** Returns true if two Filters indicate a collision */
		public static boolean test(Filter a, Filter b) {
			return (a.maskBits & b.categoryBits) != 0 && (a.categoryBits & b.maskBits) != 0;
		}
	}

}