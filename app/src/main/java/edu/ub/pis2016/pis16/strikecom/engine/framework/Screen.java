package edu.ub.pis2016.pis16.strikecom.engine.framework;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;

public abstract class Screen implements Disposable {
	protected final Game game;

	private HashMap<String, GameObject> gameObjects;

	public Screen(Game game) {
		this.game = game;
		this.gameObjects = new HashMap<>();
	}

	/**
	 * Method to get GameObjects from the screen. The method automatically casts to the given type.
	 * Don't call this method every frame. Instead, cache the result.
	 */
	public <T extends GameObject> T getGameObject(String name, Class<T> type) {
		GameObject obj = gameObjects.get(name);
		if (!(obj.getClass() == type))
			throw new IllegalArgumentException("GameObject " + name + " is not of type " + type.getName());
		return type.cast(gameObjects.get(name));
	}

	public void putGameObject(String name, GameObject object) {
		gameObjects.put(name, object);
	}

	public abstract void update(float deltaTime);

	public abstract void present(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();
}