package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Encapsulates a region inside a more convenient container. Keeps track of drawing position, rotation
 * and Scale.
 */
public class Sprite {

	private TextureRegion region;

	/** In degrees */
	private float rotation,
			scaleX,
			scaleY,
			width,
			height;
	private Vector2 position;


	public Sprite(TextureRegion region) {
		this.region = region;

		this.width = region.width;
		this.height = region.height;
		this.scaleX = 1;
		this.scaleY = 1;

		this.position = new Vector2();
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
		this.width = region.width;
		this.height = region.height;
	}

	public void draw(SpriteBatch batch, float x, float y) {
		this.position.set(x, y);
		draw(batch);
	}

	public void draw(SpriteBatch batch) {
		// If not rotated, use default 0 deg method to save performance, otherwise use specialized drawing
		if (MathUtils.isEqual(rotation, 0, 0.01f))
			batch.drawSprite(position.x, position.y, width * scaleX, height * scaleY, region);
		else
			batch.drawSprite(position.x, position.y, width * scaleX, height * scaleY, rotation, region);

	}

	public void translate(float dx, float dy) {
		position.add(dx, dy);
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	/** Get the pixel width */
	public float getSize() {
		return region.width * scaleX;
	}

	/** Set the pixel width and height */
	public void setSize(float wh) {
		setSize(wh, wh);
	}

	/** Set the pixel width and height */
	public void setSize(float w, float h) {
		float scalex = w / region.width;
		float scaley = h / region.height;
		setScale(scalex, scaley);
	}

	public void setScale(float s) {
		this.scaleX = this.scaleY = s;
	}

	public void setScale(float sx, float sy) {
		this.scaleX = sx;
		this.scaleY = sy;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public void setRotation(float angleDeg) {
		this.rotation = angleDeg;
	}

	public void setPosition(Vector2 pos) {
		this.position.set(pos);
	}


	public TextureRegion getRegion() {
		return region;
	}

	/** Returns the X scale, useful if using same X and Y scale */
	public float getScale() {
		return scaleX;
	}


}
