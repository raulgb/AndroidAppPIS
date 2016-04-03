package edu.ub.pis2016.pis16.strikecom.engine.game;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;

/**
 * Basic game object class, all game objects derive from this class.
 * Contains a HashMap with references to each of its components.
 */
public abstract class GameObject {

	private HashMap<String, Component> components;
	private int layerID = 0;

	public GameObject() {
		components = new HashMap<>();
	}

	/** Sets the layer ordering, lowest values get rendered first. */
	public void setLayer(int layerID) {
		this.layerID = layerID;
	}

	/** Returns the layer ID for ordering in a list, defaults to 0 (Background) */
	public int getLayer() {
		return layerID;
	}

	/** Steps the game simulation. Delta is the time passed since the last frame, in seconds. */
	public abstract void update(float delta);

	/** Draws the GameObject to the {@link SpriteBatch} provided. */
	public abstract void draw(SpriteBatch batch);

	/** Returns the component by that name. */
	public Component getComponent(String name) {
		return components.get(name);
	}

	/** Puts a new component linked to this GameObject. */
	public void putComponent(String name, Component component) {
		components.put(name, component);
	}
}
