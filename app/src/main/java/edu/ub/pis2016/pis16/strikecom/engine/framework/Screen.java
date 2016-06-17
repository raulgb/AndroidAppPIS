package edu.ub.pis2016.pis16.strikecom.engine.framework;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.ObjectMap;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Sort;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;

public abstract class Screen implements Disposable {
	public static final int LAYER_TERRAIN = -1;
	public static final int LAYER_BACKGROUND = 0;

	/** Building Bottoms */
	public static final int LAYER_BUILDING_BOTTOM = 1;

	/** All vehicles except strikebase */
	public static final int LAYER_VEHICLES = 5;
	/** Turrets on top vehicles except strikebase */
	public static final int LAYER_VEHICLE_TURRET = 6;

	/** Strikebase Base Layer */
	public static final int LAYER_STRIKEBASE = 10;
	public static final int LAYER_STRIKEBASE_UPGRADES = 11;
	public static final int LAYER_STRIKEBASE_UPGRADES_TOP = 12;
	public static final int LAYER_STRIKEBASE_TURRETS = 15;
	public static final int LAYER_STRIKEBASE_TURRET_UPGRADES = 16;

	/** Projectiles */
	public static final int LAYER_PROJECTILES = 20;

	public static final int LAYER_EFFECTS = 30;

	/** Building tops */
	public static final int LAYER_BUILDING_TOP = 40;

	/** Terrain overlaping everithing else */
	public static final int LAYER_TALL_TERRAIN = 50;
	public static final int LAYER_GUI = 99;

	/** Strikebase model **/
	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MK2;

	protected final Game game;

	private boolean dirty = false;
	protected boolean gamePaused = false;

	// GameObject Management
	private ObjectMap<String, GameObject> gameObjects = new ObjectMap<>();
	private Array<GameObject> goOrderedList = new Array<>();

	private ObjectMap<String, GameObject> GOsToAdd = new ObjectMap<>();
	private Array<GameObject> GOsToRemove = new Array<>(false, 16);

	/** List of Cascading input processors */
	private Array<InputProcessor> inputProcessors = new Array<>();

	/** Unique ID incremented for each anonymous GameObject added */
	private int uniqueID = 0;


	public Screen(Game game) {
		this.game = game;
	}

	/** Must be called by any subclasses. Commits any changes to the GameObject Map and sends all input to InputProcessors. */
	public void update(float deltaTime) {
		commitGameObjectChanges();

		// Send touch events down the cascade of input processors. If any input processor returns True, it is
		// understood as input successfully processed, and no further passing along is done
		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			switch (e.type) {
				case Input.TouchEvent.TOUCH_UP:
					for (InputProcessor ip : inputProcessors)
						if (ip.touchUp(e.x, e.y, e.pointer))
							break;
					break;
				case Input.TouchEvent.TOUCH_DOWN:
					for (InputProcessor ip : inputProcessors)
						if (ip.touchDown(e.x, e.y, e.pointer))
							break;
					break;
				case Input.TouchEvent.TOUCH_DRAGGED:
					for (InputProcessor ip : inputProcessors)
						if (ip.touchDragged(e.x, e.y, e.pointer))
							break;
					break;
			}
		}
		for (InputProcessor ip : inputProcessors)
			ip.update(deltaTime);
	}

	public abstract void present(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

	public Physics2D getPhysics2D() {
		return null;
	}

	public void pauseGame() {
		gamePaused = true;
	}

	public void resumeGame() {
		gamePaused = false;
	}

	/**
	 * Add input processor to the end of the Input Queue, input processors are processed in order, if any of them
	 * return true, it stops the event from propagating
	 */
	public void addInputProcessor(InputProcessor ip) {
		inputProcessors.add(ip);
	}

	public void removeInputProcessor(InputProcessor ip) {
		inputProcessors.removeValue(ip, true);
	}

	/**********************************/
	/*** GameObject RELATED METHODS ***/
	/**********************************/

	/**
	 * Add a new tagged gameObject. Please note the GameObject will NOT be available until the next frame.
	 * All other methods use this method to actually add a GameObject.
	 */
	public void addGameObject(String name, GameObject object) {
		GOsToAdd.put(name, object);
	}

	/**
	 * <b>WARNING: Use GameObject.destroy() instead.</b>
	 * <p/>
	 * Remove a GameObject by tag. Please note the GameObject will NOT be removed until the next frame.
	 */
	public void removeGameObject(String name) {
		removeGameObject(getGameObject(name));
	}

	/**
	 * <b>WARNING: Use GameObject.destroy() instead.</b>
	 * <p/>
	 * Called by GameObjects when they are initially marked for disposal. Do not use unless you know what
	 * you're doing.
	 * <p/>
	 * This method also removes any children of the destroyed object, recursively.
	 */
	public void removeGameObject(GameObject go) {
		Array.ArrayIterator<GameObject> iter = new Array.ArrayIterator<>(goOrderedList);
		while (iter.hasNext()) {
			GameObject child = iter.next();
			if (child.getParent() == go)
				removeGameObject(child);
		}
		GOsToRemove.add(go);
	}

	/** Commit changes to GameObject Map. Must be called at the begging of update method */
	protected void commitGameObjectChanges() {
		// TODO Raul: Usar PriorityQueue para no reordenar cada vez TODO el array

		// Add all pending GOs, link to this screen
		for (ObjectMap.Entry<String, GameObject> entry : GOsToAdd.entries()) {
			dirty = true;
			entry.value.setScreen(this);
			gameObjects.put(entry.key, entry.value);
		}
		GOsToAdd.clear();

		// Remove all requested GOs, unset the screen
		for (GameObject go : GOsToRemove) {
			dirty = true;
			go.destroyInternal();

			String keyToRemove = null;
			for (ObjectMap.Entry<String, GameObject> e : gameObjects.entries())
				if (e.value == go) {
					keyToRemove = e.key;
					break;
				}

			if (keyToRemove != null)
				gameObjects.remove(keyToRemove);
		}
		GOsToRemove.clear();

		if (dirty) {
			reorderGameObjectsByLayer();
			dirty = false;
		}
	}

	/** Add a new anonymous object. Returns it's automatically generated tag for usage if needed. */
	public String addGameObject(GameObject object) {
		String unique = "go" + uniqueID++;
		addGameObject(unique, object);
		return unique;
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
	public Array<GameObject> getGameObjects() {
		return goOrderedList;
	}


	/** Reorder the working array of GameObjects by layer for correct update and drawing */
	private void reorderGameObjectsByLayer() {
		goOrderedList.clear();
		// Super optimized no-alloc method, copy all values to a given array
		gameObjects.values().toArray(goOrderedList);

		// Sort based on layer
		Sort.instance().sort(goOrderedList, new Comparator<GameObject>() {
			@Override
			public int compare(GameObject lhs, GameObject rhs) {
				return Integer.compare(lhs.getLayer(), rhs.getLayer());
			}
		});
	}

	public boolean existsGameObject(GameObject go) {
		return goOrderedList.contains(go, true);
	}

	public boolean existsGameObject(String name) {
		return gameObjects.containsKey(name);
	}

	public Game getGame() {
		return game;
	}
}