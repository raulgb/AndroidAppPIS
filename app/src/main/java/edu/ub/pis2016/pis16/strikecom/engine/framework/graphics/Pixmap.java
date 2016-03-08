package edu.ub.pis2016.pis16.strikecom.engine.framework.graphics;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Disposable;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;

public interface Pixmap extends Disposable {
	public int getWidth();

	public int getHeight();

	/**
	 * Returns the underlying memory object this Pixmap represents.
	 * For AndroidPixmap: returns Bitmap.
	 */
	public Object getObject();

	public void mirrorX();

	public void mirrorY();

	public Graphics.PixmapFormat getFormat();

	public void dispose();
}