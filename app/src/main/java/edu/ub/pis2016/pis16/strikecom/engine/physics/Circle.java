package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * A circle defined by its center position and radius;
 */
public class Circle extends Shape {

	public Circle(float radius) {
		this.type = Type.CIRCLE;
		set(0, 0, radius);
	}

	@Override
	public boolean overlaps(Shape p) {
		switch (p.type) {
			case RECTANGLE:
				return OverlapAlgorithms.overlapCircleRectangle(this, (Rectangle) p);
			case CIRCLE:
				return OverlapAlgorithms.overlapCircles(this, (Circle) p);
		}
		return false;
	}

	@Override
	public float[] aabb() {
		aabb[0] = x - radius;
		aabb[1] = y - radius;
		aabb[2] = x + radius;
		aabb[3] = y + radius;
		return aabb;
	}

	public void set(float x, float y, float rad) {
		this.x = x;
		this.y = y;
		this.radius = rad;
		this.width = rad * 2f;
	}

	@Override
	public boolean contains(Vector2 p) {
		float dx = p.x - x;
		float dy = p.y - y;
		return (dx * dx + dy * dy) < radius * radius;
	}

	@Override
	public String toString() {
		return "C:[" + x + "," + y + "," + radius + "]";
	}

}
