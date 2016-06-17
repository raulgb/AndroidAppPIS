package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** A rectangle defined by it's lower-left position and radius */
public class Rectangle extends Shape {

	private static final float COS45 = MathUtils.cosDeg(45);
	private static final float SIN45 = MathUtils.sinDeg(45);

	/** Construct a new rectangle, with the lower left corner as origin x,y */
	public Rectangle(float x, float y, float width, float height) {
		type = Type.RECTANGLE;
		set(x, y, width, height);
	}

	public Rectangle(float w, float h) {
		this(0, 0, w, h);
	}


	@Override
	public boolean overlaps(Shape p) {
		switch (p.type) {
			case RECTANGLE:
				return OverlapAlgorithms.overlapRectangles(this, (Rectangle) p);
			case CIRCLE:
				return OverlapAlgorithms.overlapCircleRectangle((Circle) p, this);
		}
		return false;
	}

	@Override
	float[] aabb() {
		// Obtain the type of shape, either left-right, top-down or slanted 45 deg
		int index = (int) ((rotation - 22.5f) / 45f);
		index = index < 0 ? 7 : index;

		// left-right
		if (index == 0 || index == 4) {
			aabb[0] = x;
			aabb[1] = y;
			aabb[2] = width;
			aabb[3] = height;
			return aabb;
		}

		// top-down
		if (index == 2 || index == 6) {
			aabb[0] = x;
			aabb[1] = y;
			aabb[2] = height;
			aabb[3] = width;
			return aabb;
		}

		// Slanted (45º deg)
		float cx = x + width;
		float cy = y + height;
		float w = COS45 * height + COS45 * width;
		float h = SIN45 * height + SIN45 * width;
		aabb[0] = cx - w / 2f;
		aabb[1] = cy - h / 2f;
		aabb[2] = w;
		aabb[3] = h;
		return aabb;

	}

	public void set(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public void set(float[] aabb) {
		this.x = aabb[0];
		this.y = aabb[1];
		this.width = aabb[2];
		this.height = aabb[3];
	}

	public boolean contains(Vector2 p) {
		// both x and y have to be inside the rectangle limits
		return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
	}

	@Override
	public String toString() {
		return "R:[" + x + "," + y + "," + width + "," + height + "]";
	}
}
