package edu.ub.pis2016.pis16.strikecom.engine.opengl;


import android.opengl.GLU;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** 2D ortho camera for OpenGL graphics */
public class OrthoCamera extends GameObject {

	private GLGraphics glGraphics;

	public Vector2 position;
	public float zoom = 1;
	public float rotation = 0;
	private float frustumWidth;
	private float frustumHeight;

	public OrthoCamera(GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
		this.glGraphics = glGraphics;
		this.frustumWidth = frustumWidth;
		this.frustumHeight = frustumHeight;

		position = new Vector2(frustumWidth / 2f, frustumHeight / 2f);

		// Setup viewport
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void updateOrtho() {
		GL10 gl = glGraphics.getGL();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrthof(
				position.x - frustumWidth * zoom / 2,
				position.x + frustumWidth * zoom / 2,
				position.y - frustumHeight * zoom / 2,
				position.y + frustumHeight * zoom / 2,
				1, -1
		);

		// Camera rotation around its center
		gl.glTranslatef(position.x, position.y, 0);
		gl.glRotatef(-rotation, 0f, 0f, 1f);
		gl.glTranslatef(-position.x, -position.y, 0);
	}

	public void GUIProjection() {
		GL10 gl = glGraphics.getGL();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, glGraphics.getWidth(), 0, glGraphics.getHeight(), 1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	public Vector2 unproject(Vector2 screen) {
		// Invert y-axis
		screen.y = glGraphics.getHeight() - screen.y;

		screen.x = (screen.x / (float) glGraphics.getWidth()) * frustumWidth * zoom;
		screen.y = (screen.y / (float) glGraphics.getHeight()) * frustumHeight * zoom;

		screen.add(position).sub(frustumWidth * zoom / 2f, frustumHeight * zoom / 2f);

		screen.sub(position);
		screen.rotate(rotation);
		screen.add(position);

		return screen;
	}

}
