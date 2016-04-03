package edu.ub.pis2016.pis16.strikecom.entity;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;

public abstract class Vehicle extends GameObject {

	private HashMap<String, Vector2> anchors;

	public Vehicle(){
		super();
		anchors = new HashMap<>(8);
	}

	public abstract void turnLeft();

	public abstract void turnRight();

	public abstract void accelerate();

	public abstract void brake();

	public abstract Vector2 getPosition();

	public abstract float getRotation();

	/** Returns a Vector2 anchor for usage with anchored entities */
	public Vector2 getAnchor(String name){
		return anchors.get(name);
	}

	protected Vector2 putAnchor(String name, Vector2 anchor){
		return anchors.put(name, anchor);
	}
}
