package edu.ub.pis2016.pis16.strikecom.engine.framework.graphics;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Disposable;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public abstract class Sprite implements Disposable {

	/** Position of the sprite */
	protected float x, y;
	/** Origin offset */
	protected float ox, oy;
	/** Size of the sprite */
	protected float width, height;
	/** Scale factor */
	protected float scaleX, scaleY;
	/** Rotation, in degrees */
	protected float angle;
	/** True if has been rotated or scaled, false if transform has been acknowledged */
	protected boolean dirty;

	/** Init default Sprite parameters. */
	public Sprite() {
		this.x = y = 0;
		this.ox = oy = 0;
		this.width = height = 0;
		this.angle = 0;
		this.scaleX = scaleY = 1;
		this.dirty = true;
	}

	public abstract void draw(Graphics g);

	/**
	 * Sets the origin of the sprite relative to its width and height
	 * Example: 0.5, 0.5 would be origin at the center.
	 *
	 * @param ox range [0, 1]
	 * @param oy range [0, 1]
	 */
	public void setOrigin(float ox, float oy) {
		this.ox = ox;
		this.oy = oy;
	}

	public float getCenterX() {
		return x - width * ox;
	}

	public float getCenterY() {
		return y - height * oy;
	}

	public void translate(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	public void translateTo(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void translateTo(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void scale(float sx, float sy) {
		if (sx == 0 || sy == 0)
			return;
		this.scaleX *= sx;
		this.scaleY *= sy;
		dirty = true;
	}

	public void scaleTo(float sx, float sy) {
		if (sx == 0 || sy == 0)
			return;
		this.scaleX = sx;
		this.scaleY = sy;
		dirty = true;
	}

	public void rotate(float deg) {
		this.angle += deg;
		this.angle = angle % 360;
		dirty = true;
	}

	public void rotateTo(float deg) {
		this.angle = deg % 360;
		dirty = true;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getRotation() {
		return angle;
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	/** Returns scaled width */
	public float getWidth() {
		return width;
	}

	/** Returns scaled height */
	public float getHeight() {
		return height;
	}

	public float getAngle() {
		return angle;
	}

	private final Vector2 tmp = new Vector2();

	public Vector2 getPosition() {
		return tmp.set(x, y);
	}
}
