package edu.ub.pis2016.pis16.strikecom.engine.game;

import java.util.HashMap;

/**
 * Basic game object class, all game objects derive from this class.
 * Contains a HashMap with references to each of its components.
 */
public abstract class GameObject {

	private HashMap<String, Component> components;

	public GameObject() {
		components = new HashMap<>();
	}

	public abstract void update(float delta);

	public Component getComponent(String name) {
		return components.get(name);
	}

	public void putComponent(String name, Component component) {
		components.put(name, component);
	}
}
