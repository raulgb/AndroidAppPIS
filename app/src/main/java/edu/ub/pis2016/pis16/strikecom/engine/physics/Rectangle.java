package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Created by Akira on 2016-03-15.
 */
public class Rectangle implements Shape {
	private Vector2 llpos; //lower left corner of rectangle
	private float width;
	private float height;

    //generic getters and setters
	public Vector2 getLlpos() {
		return llpos;
	}

	public void setLlpos(Vector2 llpos) {
		this.llpos = llpos;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * generic constructor
	 * @param x x-position of low left corner
	 * @param y y-position of low left corner
	 * @param width rectangle width
	 * @param height rectangle height
	 */
	public Rectangle (float x, float y, float width, float height){
		this.llpos= new Vector2(x,y);
		this.width=width;
		this.height=height;
	}
	@Override
	public boolean overlaps(Shape p) {
		// if p is rectangle
		if(p instanceof Rectangle){
			return OverlapAlgorithms.overlapRectangles(this, (Rectangle)p);
		}
		//if p is circle
		else if (p instanceof Circle){
			return OverlapAlgorithms.overlapCircleRectangle((Circle)p,this);
		}

		return false;
	}
}
