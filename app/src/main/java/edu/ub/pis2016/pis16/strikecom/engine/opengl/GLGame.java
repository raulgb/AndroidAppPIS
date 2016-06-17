package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidAudio;
import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidFileIO;
import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidInput;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Audio;
import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;

/**
 * A Game implementation using OpenGL ES for drawing graphics.
 */
public abstract class GLGame extends Activity implements Game, GLSurfaceView.Renderer {
	public enum GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle,
	}

	GLSurfaceView glView;
	GLGraphics glGraphics;

	AndroidAudio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.nanoTime();
	PowerManager.WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedBundleInstance) {
		super.onCreate(savedBundleInstance);

		// Set immersive mode and hide status/navbar
		this.hideStatusAndNavBar();

		// Create OpenGL objects
		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);

		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}

	@Override
	public void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}

	@Override
	public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
		glGraphics.setGL(gl10);

		// Acquire single threaded lock to change state
		synchronized (stateChanged) {
			// Startscreen will be created here
			if (state == GLGameState.Initialized)
				screen = getStartScreen();
			state = GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int w, int h) {

	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;

		// The reason we syncronize access to the gameFrag's state is that
		// state may be changed inside the UI's onPause() method
		synchronized (stateChanged) {
			state = this.state;
		}

		switch (state) {
			case Running:
				// Game is running normally, calculate delta time
				// since last frame, update screen, and present
				float deltaTime = (System.nanoTime() - startTime) / 1e9f;
				startTime = System.nanoTime();

				screen.update(deltaTime);
				screen.present(deltaTime);
				break;

			case Paused:
				// App has been paused, send message to screen
				// change state to Idle, and release thread lock
				screen.pause();
				synchronized (stateChanged) {
					this.state = GLGameState.Idle;
					stateChanged.notifyAll();
				}
				break;

			case Finished:
				// App has been terminated, pause screen to save gameFrag and
				// disposeAll of it.
				screen.pause();
				screen.dispose();
				synchronized (stateChanged) {
					this.state = GLGameState.Idle;
					stateChanged.notifyAll();
				}
				break;

			case Initialized:
				break;
			case Idle:
				break;
		}
	}

	@Override
	public void onPause() {
		// Syncronize access to stateChanged, since we're about to
		// change it, and it could be changed mid-operation by the
		// render thread.
		// Upon changing it, we wait for the render thread to aknowledge
		// it by waiting on the object lock.

		synchronized (stateChanged) {
			if (isFinishing())
				state = GLGameState.Finished;
			else
				state = GLGameState.Paused;

			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {

				}
			}
		}
		wakeLock.release();
		glView.onPause();
		super.onPause();
	}

	@Override
	public AndroidAudio getAudio() {
		return audio;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen cant't be null");

		// Destroy the current instance
		this.screen.pause();
		this.screen.dispose();

		// Init and set the new instance
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}


	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	public Graphics getGraphics() {
		throw new IllegalStateException("Using GL graphics.");
	}

	private void hideStatusAndNavBar() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
		);

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}
}
