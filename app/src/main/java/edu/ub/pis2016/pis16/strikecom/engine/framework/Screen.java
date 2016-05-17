package edu.ub.pis2016.pis16.strikecom.engine.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
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
	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MKII;

	protected final Game game;

	private List<InputProcessor> inputProcessors;
	private HashMap<String, GameObject> gameObjects;
	private List<GameObject> goOrderedList;
	private int uniqueID = 0;

	// GameObject Management
	private boolean dirty = false;
	protected boolean gamePaused = false;
	private HashMap<String, GameObject> GOsToAdd = new HashMap<>();
	private ArrayList<GameObject> GOsToRemove = new ArrayList<>();

	public Screen(Game game) {
		this.game = game;

		gameObjects = new HashMap<>();
		goOrderedList = new ArrayList<>();
		inputProcessors = new ArrayList<>();
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
		inputProcessors.add(inputProcessors.size(), ip);
	}

	public void removeInputProcessor(InputProcessor ip) {
		inputProcessors.remove(ip);
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
		for (GameObject child : goOrderedList)
			if (child.getParent() == go)
				removeGameObject(child);
		GOsToRemove.add(go);
	}

	/** Commit changes to GameObject Map. Must be called at the begging of update method */
	protected void commitGameObjectChanges() {
		// TODO Raul: Usar PriorityQueue para no reordenar cada vez TODO el array

		// Add all pending GOs, link to this screen
		for (Map.Entry<String, GameObject> entry : GOsToAdd.entrySet()) {
			dirty = true;
			entry.getValue().setScreen(this);
			gameObjects.put(entry.getKey(), entry.getValue());
		}
		GOsToAdd.clear();

		// Remove all requested GOs, unset the screen
		for (GameObject go : GOsToRemove) {
			dirty = true;
			go.destroyInternal();

			String keyToRemove = null;
			for (Map.Entry<String, GameObject> e : gameObjects.entrySet())
				if (e.getValue() == go) {
					keyToRemove = e.getKey();
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
	public List<GameObject> getGameObjects() {
		return goOrderedList;
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

	public boolean hasGameObject(GameObject go) {
		return (goOrderedList.contains(go));
	}
}