package edu.ub.pis2016.pis16.strikecom.engine.physics;

/**
 * Created by Alexander Bevzenko
 */
public class RectangleObject extends Body{
	private Rectangle bounds;

	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 *
	 * @param x x coordinate of center
	 * @param y y coordinate of center
	 * @param width rectangle bound width
	 * @param height rectangle bound height
	 */
	public RectangleObject(float x, float y, float width, float height){
		super(x,y);
		this.bounds= new Rectangle(x-width/2,y-height/2,width, height);
	}
}
