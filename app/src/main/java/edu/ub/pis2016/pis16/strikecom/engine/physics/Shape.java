package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public interface Shape {//interface with basic collision methods

	Vector2 getPosition();

	void setPosition(Vector2 pos);

	Vector2 getCenter();

	float getWidth();

	float getHeight();

	/** Returns true if this shape overlaps another shape */
	boolean overlaps(Shape p);

}
