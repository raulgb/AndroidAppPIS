package edu.ub.pis2016.pis16.strikecom.fragment;

import android.app.Fragment;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Audio;
import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;


public class GLGameFragment extends Fragment implements Game, GLSurfaceView.Renderer {

	GLSurfaceView glView;
	GLGraphics glGraphics;


	PowerManager.WakeLock wakeLock;


	int VERTEX_SIZE = (2 + 4) * 4;
	FloatBuffer vertices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PowerManager powerManager = (PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");

		getActivity().getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


		// Test triangle
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3 * VERTEX_SIZE);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertices = byteBuffer.asFloatBuffer();
		vertices.put(new float[]{
				0, 0, 1, 0, 0, 1,
				1, 0, 0, 1, 0, 1,
				0.5f, 1f, 0, 0, 1, 1
		});
		vertices.flip();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		glView = new GLSurfaceView(getActivity());
		glView.setRenderer(this);
		glGraphics = new GLGraphics(glView);

		return glView;
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		if (glView != null) { glView.onResume(); }
	}

	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		if (glView != null) { glView.onResume(); }
	}

	@Override
	public Input getInput() {
		return null;
	}

	@Override
	public FileIO getFileIO() {
		return null;
	}

	@Override
	public Graphics getGraphics() {
		return null;
	}

	@Override
	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	@Override
	public Audio getAudio() {
		return null;
	}

	@Override
	public void setScreen(Screen screen) {

	}

	@Override
	public Screen getCurrentScreen() {
		return null;
	}

	@Override
	public Screen getStartScreen() {
		return null;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
		glGraphics.setGL(gl);
		Log.i("SCREEN SIZE", String.format("%d x %d", glGraphics.getWidth(), glGraphics.getHeight()));


		gl.glClearColor(0.1f, 0, 0, 1);

		// Setup OpenGL for colored vertices
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
		vertices.position(2);
		gl.glColorPointer(4, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
	}


	@Override
	public void onDrawFrame(GL10 gl10) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Setup viewport
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		float ratio = (float)glGraphics.getWidth() / (float)glGraphics.getHeight();
		gl.glOrthof(0, 1, 0, 1, 1, -1);

		// Draw
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int w, int h) {
		//Log.i("SCREEN SIZE", String.format("%d x %d", w, h));
	}
}
