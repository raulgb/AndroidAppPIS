package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public class DynamicBody extends Body {

	public Vector2 velocity = new Vector2();
	public Vector2 acceleration = new Vector2();

	/** 1: no friction, 0: full friction */
	public float friction = 1f;

	public DynamicBody(Shape bounds) {
		super(bounds);
	}

}
