package edu.ub.pis2016.pis16.strikecom.engine.game;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;

/**
 * Basic game object class, all game objects derive from this class.
 * Contains a HashMap with references to each of its components.
 */
public class GameObject {

	/** The Screen owning this GameObject */
	protected Screen screen;
	/** A Map of components, using their class as a Key */
	private HashMap<Class, Component> components = new HashMap<>();

	/** A parent GameObject in a hierarchy */
	private GameObject parent = null;
	/** The layer ID refers to how this component will be ordered in the draw call stack. Lower layers will be drawn first */
	private int layerID = 0;
	/** Tags are used internally for BehaviorComponents to identify certain GameObject as part of the scenery, allied, enemy, etc. */
	private String tag = "";

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
		for (Component c : components.values())
			if (c instanceof UpdateableComponent)
				((UpdateableComponent) c).update(delta);
	}

	/** Draws the GameObject to the {@link SpriteBatch} provided. */
	public void draw(SpriteBatch batch) {
		for (Component c : components.values())
			if (c instanceof DrawableComponent)
				((DrawableComponent) c).draw(batch);
	}

	/** Returns the component by that name. */
	public <T extends Component> T getComponent(Class<T> type) {
		return type.cast(components.get(type));
	}

	/** Puts a new component linked to this GameObject. */
	public void putComponent(Component component) {
		component.setGameObject(this);
		components.put(component.getClass(), component);
	}

	/** Remove a Component from the GameObject. */
	public void removeComponent(Class type) {
		components.remove(type);
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

	public String toString() {
		return "GO: " + tag;
	}
}
