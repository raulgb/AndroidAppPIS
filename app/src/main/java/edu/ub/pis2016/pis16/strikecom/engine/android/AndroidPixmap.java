package edu.ub.pis2016.pis16.strikecom.engine.android;


import android.graphics.Bitmap;
import android.graphics.Matrix;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics.PixmapFormat;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;

public class AndroidPixmap implements Pixmap {
	Bitmap bitmap;
	Graphics.PixmapFormat format;

	public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	@Override
	public Object getObject() {
		return bitmap;
	}

	@Override
	public void mirrorX() {
		Matrix m = new Matrix();
		m.setScale(-1, 1);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
		bitmap.recycle();
		bitmap = newBitmap;
	}

	@Override
	public void mirrorY() {
		Matrix m = new Matrix();
		m.setScale(1, -1);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
		bitmap.recycle();
		bitmap = newBitmap;
	}

	public PixmapFormat getFormat() {
		return format;
	}

	public void dispose() {
		if (!bitmap.isRecycled())
			bitmap.recycle();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}
}