package edu.ub.pis2016.pis16.strikecom.engine.game;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

/**
 * Basic game object class, all game objects derive from this class.
 * Contains a HashMap with references to each of its components.
 */
public class GameObject {

	/** The Screen owning this GameObject */
	protected Screen screen;
	/** A Map of components, using their class as a Key */
	private HashMap<Class, Component> components = new HashMap<>();

	private Array<Component> newComponents = new Array<>();

	/** Component Array for iteration performance purposes */
	private Array<Component> componentArray = new Array<>();

	/** A parent GameObject in a hierarchy */
	private GameObject parent = null;
	/** The layer ID refers to how this component will be ordered in the draw call stack. Lower layers will be drawn first */
	private int layerID = 0;
	/** Tags are used internally for BehaviorComponents to identify certain GameObject as part of the scenery, allied, enemy, etc. */
	private String tag = "";
	/** Groups for factions or whatever. Testing is done A.group & B.group */
	public int group = 0;


	/** Health Related */
	public int hitpoints = 0, maxHitpoints = 0;
	public boolean killable = false;

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

	/** Call this method to schedule this object to be removed from the Screen. */
	public void destroy() {
		screen.removeGameObject(this);
	}

	/** Override this method to change what happens when the object is actually destroyed */
	public void destroyInternal() {
		for (Component c : components.values())
			c.destroy();

		screen = null;
		parent = null;
		components.clear();
		componentArray.clear();
		newComponents.clear();
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
		PhysicsComponent phys;
		if ((phys = getComponent(PhysicsComponent.class)) != null)
			return phys.getPosition();
		throw new IllegalStateException("Can't get the position of a GameObject without PhysicsComponent: " + this.toString());
	}

	public Sprite getSprite() {
		GraphicsComponent graph;
		if ((graph = getComponent(GraphicsComponent.class)) != null)
			return graph.getSprite();
		throw new IllegalStateException("Can't get the Srpite of a GameObject without GraphicsComponent: " + this.toString());

	}

	public String toString() {
		return "GO: " + tag;
	}

	public boolean isValid() {
		return screen != null;
	}
}
