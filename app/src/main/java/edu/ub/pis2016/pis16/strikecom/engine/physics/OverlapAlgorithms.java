package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;

/**
 * all collision algorithms will be stored here
 */
public class OverlapAlgorithms {
	/**
	 * generic circle-circle collision detection
	 *
	 * @param c1 first circle
	 * @param c2 second circle
	 * @return true if they are collided
	 */
	public static boolean overlapCircles(Circle c1, Circle c2) {
		float dx = c1.x - c2.x;
		float dy = c1.y - c2.y;
		float rs = c1.radius + c2.radius;
		return (dx * dx + dy * dy <= rs * rs);
	}

	/**
	 * generic rectangle-rectangle  Axis-Aligned Bounding Box collision detection
	 *
	 * @param r1 first rectangle
	 * @param r2 second rectangle
	 * @return true if they are collided
	 */
	public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {

		if (MathUtils.isEqual(r1.rotation, 0, 0.1f) && MathUtils.isEqual(r2.rotation, 0, 0.1f))
			return (r1.x < r2.x + r2.width) && (r1.x + r1.width > r2.x) && (r1.y < r2.y + r2.height) && (r1.y + r1.height > r2.y);

		// TODO Alexander: Implement collision detection for rotated rectangles
		// Use coarse Axis-Aligned Bounding Box detecting first and then if that succeeds try more precise detection
		// See: Separating Axis Theorem http://www.gamedev.net/page/resources/_/technical/game-programming/2d-rotated-rectangle-collision-r2604
		return false;

	}

	/**
	 * generic circlre-rectangle collision detection
	 *
	 * @param c circle
	 * @param r rectangle
	 * @return true if they are collided
	 */
	public static boolean overlapCircleRectangle(Circle c, Rectangle r) {
		float closestX = c.x;
		float closestY = c.y;

		if (c.x < r.x)
			closestX = r.x;
		else if (c.x > r.x + r.width)
			closestX = r.x + r.width;

		if (c.y < r.y)
			closestY = r.y;
		else if (c.y > r.y + r.height)
			closestY = r.y + r.height;

		closestX = c.x - closestX;
		closestY = c.y - closestY;
		return (closestX * closestX + closestY * closestY <= c.radius * c.radius);
	}

	float min(float a, float b) {
		return a < b ? a : b;
	}

	float max(float a, float b) {
		return a > b ? a : b;
	}
}
