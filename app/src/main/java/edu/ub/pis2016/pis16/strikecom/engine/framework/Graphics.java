package edu.ub.pis2016.pis16.strikecom.engine.framework;


import android.graphics.Matrix;

import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;

/** Interface for the graphics component in charge of drawing to the touch. */
public interface Graphics {
	public static enum PixmapFormat {
		ARGB8888, ARGB4444, RGB565
	}

	public Pixmap newPixmap(String fileName);

	public void setTransformation(Matrix matrix);

	public void applyTransformation(Matrix matrix);

	public void saveTransformation();

	public void restoreTransformation();

	public void clear(int color);

	public void drawPixel(int x, int y, int color);

	public void drawLine(float x, float y, float x2, float y2, int color);

	public void drawRect(float x, float y, float width, float height, int color);

	public void drawPixmap(Pixmap pixmap, float x, float y, int srcX, int srcY,
						   int srcWidth, int srcHeight);

	public void drawPixmap(Pixmap pixmap, float x, float y);

	public int getWidth();

	public int getHeight();

}