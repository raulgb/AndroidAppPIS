package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public abstract class Shape {//interface with basic collision methods

	public enum Type {
		RECTANGLE,
		CIRCLE
	}

	public Type type;
	public float x, y, width, height;
	public float radius = 0;
	public float rotation = 0;

	/** Axis Aligned Bounding Box. DO NOT ACCESS THIS, USE aabb() */
	protected float[] aabb = new float[4];

	/** Returns true if this shape overlaps another shape */
	abstract boolean overlaps(Shape p);

	/** Calculate an AABB for this shape */
	abstract float[] aabb();

	abstract boolean contains(Vector2 point);
}
