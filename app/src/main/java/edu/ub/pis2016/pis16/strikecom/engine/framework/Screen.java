package edu.ub.pis2016.pis16.strikecom.engine.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;

public abstract class Screen implements Disposable {
	public static final int LAYER_TERRAIN = -1;
	public static final int LAYER_BACKGROUND = 0;
	public static final int LAYER_1 = 1;
	public static final int LAYER_2 = 2;
	public static final int LAYER_3 = 3;
	public static final int LAYER_4 = 4;


	protected final Game game;

	private HashMap<String, GameObject> gameObjects;
	private List<GameObject> goOrderedList;
	private int uniqueID = 0;

	public Screen(Game game) {
		this.game = game;

		gameObjects = new HashMap<>();
		goOrderedList = new ArrayList<>();
	}

	public void update(float deltaTime){
		commitGameObjectChanges();
	}

	public abstract void present(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

	/**********************************/
	/*** GameObject RELATED METHODS ***/
	/**********************************/

	/**
	 * Add a new tagged gameObject. Please note the GameObject will NOT be available until the next frame.
	 * All other methods use this method to actually add a GameObject.
	 */
	public void putGameObject(String name, GameObject object) {
		addedGOs.put(name, object);
	}

	/**
	 * Remove a GameObject by tag. Please note the GameObject will NOT be available until the next frame.
	 * All other methods use this method to actually remove a GameObject.
	 */
	public void removeGameObject(String name) {
		removedGOs.add(name);
	}

	private boolean dirty = false;
	private HashMap<String, GameObject> addedGOs = new HashMap<>();
	private ArrayList<String> removedGOs = new ArrayList<>();

	/** Commit changes to GameObject Map. Must be called at the begging of update method */
	protected void commitGameObjectChanges() {
		// Add all pending GOs, link to this screen
		for (Map.Entry<String, GameObject> entry : addedGOs.entrySet()) {
			dirty = true;
			entry.getValue().setScreen(this);
			gameObjects.put(entry.getKey(), entry.getValue());
		}
		addedGOs.clear();

		// Remove all requested GOs, unset the screen
		for (String name : removedGOs) {
			dirty = true;
			gameObjects.remove(name)/*.setScreen(null)*/;
		}
		removedGOs.clear();

		if (dirty) {
			reorderGameObjectsByLayer();
			dirty = false;
		}
	}

	/** Add a new untagged gameObject */
	public void putGameObject(GameObject object) {
		putGameObject("go" + (uniqueID++), object);
	}

	/** Lightweight method to get a named game object. Does not check for existence. */
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

	public void removeGameObject(GameObject object) {
		String keyToRemove = null;
		for (Map.Entry<String, GameObject> entry : gameObjects.entrySet())
			if (entry.getValue() == object)
				keyToRemove = entry.getKey();

		if (keyToRemove != null)
			removeGameObject(keyToRemove);
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