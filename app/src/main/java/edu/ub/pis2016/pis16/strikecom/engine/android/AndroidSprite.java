package edu.ub.pis2016.pis16.strikecom.engine.android;

import android.graphics.Matrix;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Sprite;

/**
 * Sprite implementation for drawing with the Androdi Canvas API.F
 * Created by German Dempere on 2016-03-01.
 */
public class AndroidSprite extends Sprite {

	protected Pixmap pixmap;
	protected final Matrix projection = new Matrix();
	protected final Matrix transform = new Matrix();

	/** Empty AndroidSprite */
	protected AndroidSprite() {
		super();
	}

	/**
	 * A more advanced pixmap, can be drawn rotated or scaled. Once a pixmap has been passed to the Sprite,
	 * It becomes invalid as it will be mirrored to draw, and then the original is disposed.
	 */
	public AndroidSprite(Pixmap pixmap) {
		super();

		this.pixmap = pixmap;
		this.width = pixmap.getWidth();
		this.height = pixmap.getHeight();

	}

	/**
	 * Draw method, will use supplied graphics to internally generate a transformation
	 * matrix and draw itself on the touch.
	 * <p>
	 * Only use if necessary, otherwise use {@link Pixmap}
	 *
	 * @param g Graphics object, supplied by the Game instance
	 */
	public void draw(Graphics g) {
		this.updateProjectionMatrix();

		g.saveTransformation();
		g.applyTransformation(projection);
		// Draw pixmat offsetted by the OX and OY parameters
		// IE: 0.5,0.5 will center the sprite around the new projection plane
		g.drawPixmap(pixmap, -width * ox, -height * oy);
		g.restoreTransformation();
	}

	/** Must be called before draw to update position, rotation and scale. */
	protected void updateProjectionMatrix() {
		// update transform only if rotation or scale has changed
		if (dirty) {
			transform.setScale(scaleX, scaleY);
			transform.preRotate(angle);
			dirty = false;
		}

		projection.set(transform);
		projection.postTranslate(x, y);

	}

	public void dispose() {
		pixmap.dispose();
	}

}
