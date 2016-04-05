package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * A circle defined by its position and radius;
 */
public class Circle implements Shape {

	private Vector2 tmp = new Vector2();
	private Vector2 position;
	private float radius;

	public Circle(float x, float y, float radius) {
		this.position = new Vector2(x, y);
		this.radius = radius;
	}


	public Vector2 getCenter() {
		return tmp.set(position).sub(radius, radius);
	}

	/** Lower left corner of the circle */
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public float getWidth() {
		return radius * 2;
	}

	@Override
	public float getHeight() {
		return radius * 2;
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
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
