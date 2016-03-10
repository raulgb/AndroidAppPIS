package edu.ub.pis2016.pis16.strikecom.fragment;

import android.app.Fragment;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Audio;
import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;


public class AndroidGameFragment extends Fragment implements Game, GLSurfaceView.Renderer {

	GLSurfaceView glView;
	GLGraphics glGraphics;


	PowerManager.WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PowerManager powerManager = (PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");

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
	public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
		glGraphics.setGL(gl10);
	}


	@Override
	public void onDrawFrame(GL10 gl10) {
		GL10 gl = glGraphics.getGL();

		gl.glClearColor(MathUtils.random(1f), 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int w, int h) {

	}
}
