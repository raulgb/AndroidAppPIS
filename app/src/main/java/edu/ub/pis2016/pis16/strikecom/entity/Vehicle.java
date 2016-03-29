package edu.ub.pis2016.pis16.strikecom.entity;

import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;

public interface Vehicle {

	public void update(float delta);

	public void draw(SpriteBatch batch);

	public void turnLeft();

	public void turnRight();

	public void accelerate();

	public void brake();
}
