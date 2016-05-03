package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public class Rectangle implements Shape {
	private Vector2 tmp = new Vector2();

	/** Lower Left corner of the Rectangle */
	public float x, y;
	public float width;
	public float height;
	/** rotation around the center */
	public float rotation;

	public Rectangle(float x, float y, float width, float height) {
		this.x = x - width / 2f;
		this.y = y - height / 2f;
		this.width = width;
		this.height = height;
		rotation = 0;
	}

	public Rectangle(float w, float h) {
		this(0, 0, w, h);
	}

	public Vector2 getPosition() {
		return tmp.set(x, y);
	}

	/** Move the center of the rectangle */
	@Override
	public void setPosition(Vector2 pos) {
		x = pos.x - width / 2f;
		y = pos.y - height / 2f;
	}

	/** Move the center of the rectangle */
	@Override
	public void setPosition(float x, float y) {
		this.x = x - width / 2f;
		this.y = y - height / 2f;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(float r) {
		// TODO Enable Rectangle rotation
		//rotation = r;
	}

	@Override
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
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
