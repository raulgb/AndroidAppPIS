package edu.ub.pis2016.pis16.strikecom.engine.game;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.ObjectMap;

/**
 * Basic game object class, all game objects derive from this class.
 * Contains a HashMap with references to each of its components.
 */
public class GameObject {

	/** The Screen owning this GameObject */
	protected Screen screen;
	/** A Map of components, using their class as a Key */
	private ObjectMap<Class, Component> components = new ObjectMap<>(6, 0.8f);

	private Array<Component> newComponents = new Array<>();

	/** Component Array for iteration performance purposes */
	private Array<Component> componentArray = new Array<>();

	private Array<Runnable> destroyRunnables = new Array<>(false, 2);

	/** A parent GameObject in a hierarchy */
	private GameObject parent = null;
	/** The layer ID refers to how this component will be ordered in the draw call stack. Lower layers will be drawn first */
	private int layerID = 0;
	/** Tags are used internally for BehaviorComponents to identify certain GameObject as part of the scenery, allied, enemy, etc. */
	private String tag = "";

	/** Health Related */
	public int hitpoints = 0, maxHitpoints = 0;
	public boolean killable = false;

	/** Factions */
	public Faction faction = Faction.NEUTRAL;

	private static final int NEUTRAL_BIT = 0x01;
	private static final int PLAYER_BIT = 0x01 << 1;
	private static final int RAIDERS_BIT = 0x01 << 2;
	private static final int SHOP_BIT = 0x01 << 3;

	public enum Faction {
		HOSTILE(0, 0),
		NEUTRAL(NEUTRAL_BIT, 0),
		PLAYER(PLAYER_BIT, SHOP_BIT),
		RAIDERS(RAIDERS_BIT, 0),
		SHOP(SHOP_BIT, PLAYER_BIT);

		protected int self;
		protected int allies;

		Faction(int self, int allies) {
			this.self = self;
			this.allies = allies | self | NEUTRAL_BIT;
		}

		/** Returns true if both factions agreed to be allies */
		public boolean isAlly(Faction other) {
			return (this.self & other.allies) > 0 && (this.allies & other.self) > 0;
		}

		/** Returns true if either faction is not an ally of the other. */
		public boolean isEnemy(Faction other) {
			return (this.self & other.allies) == 0 || (this.allies & other.self) == 0;
		}
	}


	/**
	 * Sets the layer ordering, lowest values get rendered first.
	 * <p/>
	 * <b>WARNING:</b> Set the layer BEFORE adding to a Screen. To change layer if already inside, remove from
	 * Screen, change layer, and add back.
	 */
	public void setLayer(int layerID) {
		this.layerID = layerID;
	}

	/** Returns the layer ID for ordering in a list, defaults to 0 (Background) */
	public int getLayer() {
		return layerID;
	}

	/** Steps the game simulation. Delta is the time passed since the last frame, in seconds. */
	public void update(float delta) {
		if (newComponents.size > 0) {
			for (Component c : newComponents)
				c.init();
			newComponents.clear();
		}

		for (Component c : componentArray)
			if (c instanceof UpdateableComponent)
				((UpdateableComponent) c).update(delta);
	}

	/** Draws the GameObject to the {@link SpriteBatch} provided. */
	public void draw(SpriteBatch batch) {
		for (Component c : componentArray)
			if (c instanceof DrawableComponent)
				((DrawableComponent) c).draw(batch);
	}

	/** Returns the component by that name. */
	public <T extends Component> T getComponent(Class<T> type) {
		return type.cast(components.get(type));
	}

	/** Puts a new component linked to this GameObject. */
	public void putComponent(Component component) {
		component.gameObject = this;
		newComponents.add(component);
		components.put(component.getClass(), component);
		componentArray.add(component);
	}

	/** Remove a Component from the GameObject. */
	public void removeComponent(Class type) {
		Component comp = components.get(type);
		if (comp == null)
			return;
		comp.destroy();
		Component c = components.remove(type);
		componentArray.removeValue(c, true);
	}

	public boolean hasComponent(Class type) {
		return components.containsKey(type);
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setScreen(Screen s) {
		this.screen = s;
	}

	public Screen getScreen() {
		return screen;
	}

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public void addOnDestroyAction(Runnable r) {
		destroyRunnables.add(r);
	}

	/**
	 * Call this method to schedule this object to be removed from the Screen. This will also run any actions
	 * defined to be run on object destroy.
	 */
	final public void destroy() {
		if (!isValid())
			throw new IllegalStateException("Can't destroy an Invalid GameObject: " + toString());

		for (Runnable r : destroyRunnables)
			r.run();
		screen.removeGameObject(this);
	}

	public void destroyInternal() {
		if (!isValid())
			return;

		for (Component c : componentArray)
			c.destroy();

		destroyRunnables.clear();
		components.clear();
		componentArray.clear();
		newComponents.clear();

		screen = null;
		parent = null;
		destroyRunnables = null;
		components = null;
		componentArray = null;
		newComponents = null;

	}

	// --- ACCELERATOR METHODS --- //

	/** Depends on the object having a PhysicsComponent */
	public void setPosition(float x, float y) {
		PhysicsComponent phys;
		if ((phys = getComponent(PhysicsComponent.class)) != null)
			phys.setPosition(x, y);
	}

	/** Depends on the object having a PhysicsComponent */
	public void setPosition(Vector2 pos) {
		PhysicsComponent phys;
		if ((phys = getComponent(PhysicsComponent.class)) != null)
			phys.setPosition(pos);
	}

	public Vector2 getPosition() {
		if (hasComponent(PhysicsComponent.class))
			return getComponent(PhysicsComponent.class).getPosition();
		throw new IllegalStateException("Can't get the position of a GameObject without PhysicsComponent: " + this.toString());
	}

	public Sprite getSprite() {
		if (hasComponent(GraphicsComponent.class))
			return getComponent(GraphicsComponent.class).getSprite();
		throw new IllegalStateException("Can't get the Sprite of a GameObject without GraphicsComponent: " + this.toString());

	}


	public PhysicsComponent getPhysics() {
		if (hasComponent(PhysicsComponent.class))
			return getComponent(PhysicsComponent.class);
		throw new IllegalStateException("GameObject has no PhysicsComponent: " + this.toString());

	}

	public String toString() {
		return "GO: " + tag;
	}

	public boolean isValid() {
		return screen != null;
	}

	public void takeHit(float dmg) {
		if (dmg > this.maxHitpoints) {
			maxHitpoints = 0;
			this.destroy();
		} else {
			this.hitpoints -= dmg;
		}
	}

}
