package edu.ub.pis2016.pis16.strikecom.engine.graphics;

import android.graphics.Rect;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;

/**
 * Class conaitning a AndroidSprite that is part of a larger spritesheet, given as a
 * Pixmap resource.
 * <p>
 * Created by German on 2016-03-01.
 */
public class AtlasSprite {

	/** Region of the pixmap to draw */
	private Rect srcRect;
	/** Resource pixmap */
	private Pixmap pixmap;

	float x, y;

	public AtlasSprite(Pixmap atlas, int x, int y, int w, int h) {
		this.pixmap = atlas;
		srcRect = new Rect();
		srcRect.set(x, y, x + w, y + h);
	}

	public void draw(Graphics g) {
		g.drawPixmap(pixmap, x, y, srcRect.left, srcRect.top, srcRect.width(), srcRect.height());
	}

}
