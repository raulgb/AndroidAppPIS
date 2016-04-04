package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Created by Akira on 2016-03-15.
 */
public class Circle implements Shape {
	private Vector2 center;
	private float radius;

	public Vector2 getCenter() {
		return center;
	}

	public void setCenter(Vector2 center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Circle(float x, float y, float radi){
		this.center= new Vector2(x,y);
		this.radius=radi;
	}
	@Override
	public boolean overlaps(Shape p) {
		//if p is circle
		if (p.getClass().getName().equals("Circle")){
			return OverlapAlgorithms.overlapCircles(this, (Circle)p);
		}
		//if p is rectangle
		else if (p.getClass().getName().equals("Rectangle")){
			return OverlapAlgorithms.overlapCircleRectangle(this,(Rectangle)p);
		}

		return false;
	}
}
