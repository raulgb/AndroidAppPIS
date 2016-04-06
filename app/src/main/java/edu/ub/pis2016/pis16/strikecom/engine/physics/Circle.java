package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * A circle defined by its position and radius;
 */
public class Circle implements Shape {
	private Vector2 tmp = new Vector2();

	public float x, y;
	public float radius;

	public Circle(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public Vector2 getCenter() {
		return tmp.set(x, y);
	}

	public Vector2 getPosition() {
		return tmp.set(x, y).sub(radius, radius);
	}

	@Override
	public float getWidth() {
		return radius * 2;
	}

	@Override
	public float getHeight() {
		return radius * 2;
	}

	public void setPosition(Vector2 p) {
		x = p.x + radius;
		y = p.y + radius;
	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x + radius;
		this.y = y + radius;
	}

	@Override
	public boolean overlaps(Shape p) {
		//if p is circle
		if (p instanceof Circle) {
			return OverlapAlgorithms.overlapCircles(this, (Circle) p);
		}
		//if p is rectangle
		else if (p instanceof Rectangle) {
			return OverlapAlgorithms.overlapCircleRectangle(this, (Rectangle) p);
		}

		return false;
	}
}
