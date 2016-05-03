package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * A circle defined by its position and radius;
 */
public class Circle implements Shape {

	Vector2 tmp = new Vector2();

	/** Center coordinates of the Circle */
	public float x, y;
	public float radius, rotation;

	public Circle(float radius) {
		this.radius = radius;
	}

	public Vector2 getPosition() {
		return tmp.set(x, y);
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
		x = p.x;
		y = p.y;
	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(float r) {
		rotation = r % 360;
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
