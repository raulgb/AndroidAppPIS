package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public interface Shape {//interface with basic collision methods

	/** Natural position of the plauer, LL corner for rects, center for circles  */
	Vector2 getPosition();

	/** Set the center position of the shape */
	void setPosition(Vector2 pos);

	/** Set the center position of the shape */
	void setPosition(float x, float y);

	float getRotation();

	void setRotation(float r);

	float getWidth();

	float getHeight();

	/** Returns true if this shape overlaps another shape */
	boolean overlaps(Shape p);

	boolean contains(Vector2 point);
}
