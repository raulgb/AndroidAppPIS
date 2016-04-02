package edu.ub.pis2016.pis16.strikecom.engine.math;

import android.annotation.SuppressLint;
import android.graphics.Matrix;


/**
 * 2D vector math
 * <p/>
 * Created by German Dempere on 2016-02-29.
 */
@SuppressLint("DefaultLocale")
public class Vector2 {

	public static final Vector2 ZERO = new Vector2(0, 0);

	public float x, y;

	private static final Vector2 tmp = new Vector2();

	public Vector2() {
		x = 0;
		y = 0;
	}

	public Vector2(Vector2 v) {
		x = v.x;
		y = v.y;
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2 set(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	public Vector2 add(Vector2 v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}

	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector2 sub(Vector2 v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}

	public Vector2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Vector2 scl(float scal) {
		this.x *= scal;
		this.y *= scal;
		return this;
	}


	public Vector2 scl(float scalx, float scaly) {
		this.x *= scalx;
		this.y *= scaly;
		return this;
	}

	private final float[] val = new float[16];

	/**
	 * Multiply point by some matrix, usually transformations
	 * NOTE: 1 included as z coordinate.
	 */
	public Vector2 mul(Matrix m) {
		m.getValues(val);
		this.x = val[0] * this.x + val[1] * this.y + val[2] * 1;
		this.y = val[3] * this.x + val[4] * this.y + val[5] * 1;
		return this;
	}

	public float dot(Vector2 v) {
		return x * v.x + y * v.y;
	}

	public Vector2 nor() {
		float len = len();
		this.x /= len;
		this.y /= len;
		return this;
	}

	public float dst(Vector2 v) {
		return tmp.set(v).sub(this).len();
	}

	public float len() {
		return (float)Math.sqrt(x * x + y * y);
	}

	public float angle() {
		return MathUtils.radiansToDegrees * MathUtils.atan2(y, x);
	}

	public Vector2 rotate(float deg) {
		final float ca = MathUtils.cosDeg(deg);
		final float sa = MathUtils.sinDeg(deg);
		set(ca * x - sa * y, sa * x + ca * y);
		return this;
	}

	public float len2() {
		return x * x + y * y;
	}

	public boolean notZero() {
		return Math.abs(x) > 0.001f && Math.abs(y) > 0.001f;
	}

	public Vector2 cpy() {
		return new Vector2(x, y);
	}

	@Override
	public String toString() {
		return String.format("[%.3f, %.3f]", x, y);
	}

	/** Linear interpolation between this vector and the target vector, alpha in the range of 0-1 */
	public Vector2 lerp(Vector2 target, float alpha) {
		final float invAlpha = 1.0f - alpha;
		this.x = (x * invAlpha) + (target.x * alpha);
		this.y = (y * invAlpha) + (target.y * alpha);
		return this;
	}
}
