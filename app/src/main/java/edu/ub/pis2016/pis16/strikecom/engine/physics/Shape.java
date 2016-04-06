package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public interface Shape {//interface with basic collision methods

	/** Lower Left corner of the Shape, whatever it is. */
	Vector2 getPosition();

	/** Set the lower-left corner of the shape */
	void setPosition(Vector2 pos);

	/** Set the lower-left corner of the shape */
	void setPosition(float x, float y);

	/** Center of the shape */
	Vector2 getCenter();

	float getWidth();

	float getHeight();

	/** Returns true if this shape overlaps another shape */
	boolean overlaps(Shape p);

}
