package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** Body is a generic type of object used in collision system */
public abstract class Body {

	protected Shape bounds;

	public Vector2 position = new Vector2();

	public Object userData;

	public Body(Shape bounds) {
		this.bounds = bounds;
		bounds.setPosition(this.position);
	}

	public void update(float delta){
		bounds.setPosition(position);
	}

	public Shape getBounds() {
		return bounds;
	}

	public Vector2 getPosition(){
		return this.position;
	}

	public void setPosition(Vector2 v){
		this.position = v;
	}

	public void setPosition(float x, float y){
		this.position = new Vector2(x,y);
	}
}
