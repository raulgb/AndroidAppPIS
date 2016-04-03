package edu.ub.pis2016.pis16.strikecom.engine.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;

public abstract class Screen implements Disposable {
	protected final Game game;

	private HashMap<String, GameObject> gameObjects;
	private List<GameObject> goOrderedList;

	public Screen(Game game) {
		this.game = game;

		gameObjects = new HashMap<>();
		goOrderedList = new ArrayList<>();
	}

	public abstract void update(float deltaTime);

	public abstract void present(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

	/**********************************/
	/*** GameObject RELATED METHODS ***/
	/**********************************/

	/** Lightweight method to get a named game object. Checks for existence. */
	public GameObject getGameObject(String name) {
		return gameObjects.get(name);
	}

	/**
	 * Method to get GameObjects from the screen. The method automatically casts to the given type.
	 * Don't call this method every frame. Instead, cache the result.
	 *
	 * @throws IllegalArgumentException If object does not exist.
	 */
	public <T extends GameObject> T getGameObject(String name, Class<T> type) {
		GameObject obj = gameObjects.get(name);
		if (!(obj.getClass() == type))
			throw new IllegalArgumentException("GameObject " + name + " is not of type " + type.getName());
		return type.cast(gameObjects.get(name));
	}

	/** Returns a list of GameObjects, ordered by their layerID, in ascending order (Ideal for drawing) */
	public List<GameObject> getGameObjects() {
		return goOrderedList;
	}

	public void putGameObject(String name, GameObject object) {
		gameObjects.put(name, object);
		reorderGameObjectsByLayer();
	}

	public void removeGameObject(GameObject object) {
		String keyToRemove = null;
		for (Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
			if (entry.getValue() == object)
				keyToRemove = entry.getKey();
		}
		if (keyToRemove != null) {
			gameObjects.remove(keyToRemove);
			reorderGameObjectsByLayer();
		}
	}

	public void removeGameObject(String name) {
		gameObjects.remove(name);
		reorderGameObjectsByLayer();
	}

	private void reorderGameObjectsByLayer() {
		goOrderedList.clear();
		goOrderedList.addAll(gameObjects.values());

		// Sort based on layer
		Collections.sort(goOrderedList, new Comparator<GameObject>() {
			@Override
			public int compare(GameObject lhs, GameObject rhs) {
				return Integer.compare(lhs.getLayer(), rhs.getLayer());
			}
		});
	}

}