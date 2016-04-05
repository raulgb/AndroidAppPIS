package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public class Rectangle implements Shape {
	private Vector2 tmp = new Vector2();

	private Vector2 pos = new Vector2(); //lower left corner of rectangle
	private float width;
	private float height;

	//generic getters and setters
	public Vector2 getPosition() {
		return tmp.set(pos);
	}

	public void setPosition(Vector2 pos) {
		this.pos = pos;
	}

	@Override
	public Vector2 getCenter() {
		return tmp.set(pos).add(width / 2f, height / 2f);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * generic constructor
	 *
	 * @param x      x-position of low left corner
	 * @param y      y-position of low left corner
	 * @param width  rectangle width
	 * @param height rectangle height
	 */
	public Rectangle(float width, float height) {
		this.pos = new Vector2(0, 0);
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean overlaps(Shape p) {
		// if p is rectangle
		if (p instanceof Rectangle) {
			return OverlapAlgorithms.overlapRectangles(this, (Rectangle) p);
		}
		//if p is circle
		else if (p instanceof Circle) {
			return OverlapAlgorithms.overlapCircleRectangle((Circle) p, this);
		}

		return false;
	}
}
