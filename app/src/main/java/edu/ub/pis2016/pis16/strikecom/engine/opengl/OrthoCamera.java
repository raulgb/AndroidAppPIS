package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import android.graphics.Matrix;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Utility class to manage a viewport camera, can be moved around, rotate around center, and
 * zoom in/out.
 * <p>
 * Also known as the hackiest, most patched up class ever.
 * <p>
 * Created by Akira on 2016-03-01.
 */
public class OrthoCamera {

	public Vector2 pos;
	private float w, h;
	private float rotation;
	public float zoom;

	private Matrix transform;
	private Matrix view;
	private static float[] vals = new float[9];
	private static Vector2 tmp = new Vector2();

	/** Combined view and transform matrices. */
	public Matrix combined;
	private Matrix invProjectionView;

	public OrthoCamera(float viewWidth, float viewHeight) {
		this.w = viewWidth;
		this.h = viewHeight;
		this.zoom = 1;
		this.rotation = 0;

		// Create matrices
		transform = new Matrix();
		view = new Matrix();
		combined = new Matrix();
		invProjectionView = new Matrix();

		pos = new Vector2();
	}

	private Vector2 center = new Vector2();

	/** Sets the center of this camera to look at coordinates x,y */
	public void setPosition(float x, float y) {
		pos.x = x;
		pos.y = y;
	}

	public void setPosition(Vector2 v) {
		pos.x = v.x;
		pos.y = v.y;
	}

	public void rotate(float deg) {
		this.rotation += deg;
	}

	public void rotateTo(float deg) {
		this.rotation = deg;
	}

	public void translate(float dx, float dy) {
		pos.add(dx, dy);
	}

	public void translate(Vector2 dv) {
		this.pos.add(dv);
	}

	public void setZoom(float z) {
		this.zoom = z;
		//this.w /= zoom;
		//this.h /= zoom;
	}

	public void unproject(Vector2 screenCoords) {
		screenCoords.mul(invProjectionView);

		/* DEBUG
		combined.getValues(vals);
		Log.i("Combined", Arrays.toString(vals));
		invProjectionView.getValues(vals);
		Log.i("Inverse", Arrays.toString(vals));
		*/
	}

	/** Calculates matrix with all current transforms. */
	public void update() {
		// Camera starts up at the top left corner of the touch, with the identity matrix.
		// Set camera to Y axis up, move to bottom corner and reflect along x axis
		// (flip projection horizontally)
		view.setTranslate(0, this.h);
		view.preScale(1, -1);

		// Rotate and scale first, origin is the center of the viewport
		// Then move the transformed camera viewport to the position offset
		transform.setRotate(-rotation, w / 2f, h / 2f);
		transform.preScale(zoom, zoom, w / 2f, h / 2f);
		transform.preTranslate(-pos.x + w / 2f, -pos.y + h / 2f);

		// Order is VIEW * TRANSFORM
		combined.set(view);
		combined.preConcat(transform);

		/* DEBUG */
//		Log.i("CAM POSI", pos.toString());
//		Log.i("VIEW MAT", view.toString());
//		Log.i("TRANSF M", transform.toString());
//		Log.i("COMBINED", transform.toString());

		// Invert combined for touch -> world projections
		combined.invert(invProjectionView);
	}


	public Vector2 project(Vector2 v) {
		return v.mul(combined);
	}

	public Vector2 project(float x, float y) {
		return tmp.set(x, y).mul(combined);
	}
}
