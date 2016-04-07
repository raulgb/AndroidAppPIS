package edu.ub.pis2016.pis16.strikecom.engine.opengl;


import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/** 2D ortho camera for OpenGL graphics */
public class OrthoCamera {

	private GLGraphics glGraphics;

	public Vector2 position;
	public float zoom;
	public float frustumWidth;
	public float frustumHeight;

	public OrthoCamera(GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
		this.glGraphics = glGraphics;
		this.frustumWidth = frustumWidth;
		this.frustumHeight = frustumHeight;

		position = new Vector2(frustumWidth / 2f, frustumHeight / 2f);
		zoom = 1;

		// Setup viewport
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	public void update() {
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
	}

	public void GUIProjection() {
		GL10 gl = glGraphics.getGL();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, glGraphics.getWidth(), 0, glGraphics.getHeight(), 1, -1);
	}

	public Vector2 unproject(Vector2 screen) {
		// Invert y-axis
		screen.y = glGraphics.getHeight() - screen.y;
		screen.x = (screen.x / (float) glGraphics.getWidth()) * frustumWidth * zoom;
		screen.y = (screen.y / (float) glGraphics.getHeight()) * frustumHeight * zoom;

		return screen.add(position).sub(frustumWidth * zoom / 2f, frustumHeight * zoom / 2f);
	}

}
