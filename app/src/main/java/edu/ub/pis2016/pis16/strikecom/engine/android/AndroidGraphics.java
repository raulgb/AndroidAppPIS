package edu.ub.pis2016.pis16.strikecom.engine.android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;


public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
		paint.setDither(false);
		paint.setAntiAlias(false);
		paint.setFilterBitmap(false);
	}

	public Pixmap newPixmap(String fileName) {
		Config config = Config.ARGB_8888;

		Options options = new Options();
		options.inPreferredConfig = config;
		InputStream in = null;
		Bitmap bitmap = null;
		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);

			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '"
						+ fileName + "'");
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return new AndroidPixmap(bitmap, PixmapFormat.ARGB8888);
	}

	@Override
	public void setTransformation(Matrix matrix) {
		this.canvas.setMatrix(matrix);
	}

	@Override
	public void applyTransformation(Matrix matrix) {
		canvas.concat(matrix);
	}

	@Override
	public void saveTransformation() {
		this.canvas.save();
	}

	@Override
	public void restoreTransformation() {
		this.canvas.restore();
	}

	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));
	}

	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}

	public void drawLine(float x, float y, float x2, float y2, int color) {
		paint.setColor(color);
		paint.setStrokeWidth(5f);
		canvas.drawLine(x, y, x2, y2, paint);
		paint.setStrokeWidth(1f);
	}

	public void drawRect(float x, float y, float width, float height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}

	public void drawPixmap(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;
		dstRect.left = (int)(x + 0.5f);
		dstRect.top = (int)(y + 0.5);
		dstRect.right = dstRect.left + srcWidth - 1;
		dstRect.bottom = dstRect.top + srcHeight - 1;
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, srcRect, dstRect, null);
	}

	public void drawPixmap(Pixmap pixmap, float x, float y) {
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
	}

	public int getWidth() {
		return frameBuffer.getWidth();
	}

	public int getHeight() {
		return frameBuffer.getHeight();
	}

}