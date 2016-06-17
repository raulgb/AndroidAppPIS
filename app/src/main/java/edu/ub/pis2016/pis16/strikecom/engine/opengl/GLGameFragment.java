package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.OptionsActivity;
import edu.ub.pis2016.pis16.strikecom.R;
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
 * Game class definition implemented from a Fragment instead of an Activity
 * <p/>
 * <b>Special method:</b><br>
 * <code>postRunnable(Runnable r)</code>: Posts a new runnable object to run on the OpenGL thread
 */
public abstract class GLGameFragment extends Fragment implements Game, GLSurfaceView.Renderer {

	/** Android GL view object. */
	private GLSurfaceView glView;

	// Interface objects
	private GLGraphics glGraphics;
	private AndroidAudio audio;
	private Input input;
	private FileIO fileIO;

	protected Screen screen;
	protected int SCREEN_W, SCREEN_H;

	int valueMusic;

	GLGame.GLGameState state = GLGame.GLGameState.Initialized;

	final Object stateChanged = new Object();
	long startTime = System.nanoTime();

	private ArrayList<Runnable> runnables = new ArrayList<>(8);

	/**
	 * Method called by the activity when the Fragment is loaded into the layout, returns a View
	 * instance which will be used as the setContentView() parameter.
	 * <p/>
	 * Must return a reference to our GLSurfaceView here.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

/*		OptionsActivity activity =(OptionsActivity) getActivity();

		valueMusic=activity.getPercentMusic();*/
		Bundle args = getArguments();
/*		if(args!=null) {
			valueMusic = args.getInt("percentMusic", 100);
		}else{
			valueMusic=0;
		}*/

		SharedPreferences sharedPreferences = 	PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());;
		valueMusic = sharedPreferences.getInt("percentMusic", 100);

		// Create OpenGL objects
		glView = new GLSurfaceView(getActivity());
		glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glView.setRenderer(this);

		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(getActivity());
		audio = new AndroidAudio(getActivity());
		input = new AndroidInput(getActivity(), glView, 1, 1);

		// Return the GLSurfaceView object, same as setContentView();
		return glView;
	}

	@Override
	public int getValueMusic(){
		return valueMusic;
	}


	@Override
	public void onPause() {
		// Synchronize access to stateChanged, since we're about to
		// change it, and it could be changed mid-operation by the
		// render thread.

		// Upon changing it, we wait for the render thread to acknowledge
		// it by waiting on the object lock.

		synchronized (stateChanged) {
			if (getActivity().isFinishing())
				state = GLGame.GLGameState.Finished;
			else
				state = GLGame.GLGameState.Paused;

			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) { }
			}
		}
		glView.onPause();
		super.onPause();
	}

	public void newInstance(Bundle b) {
		setArguments(b);
	}

	@Override
	public void onResume() {
		super.onResume();
		glView.onResume();

		// TODO Test if this works
		Texture.reloadManagedTextures();
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen cant't be null");

		// Destroy the current instance
		this.screen.pause();
		this.screen.dispose();

		// Init and set the new instance
		screen.resume();
		screen.resize(SCREEN_W, SCREEN_H);
		//screen.update(0);
		this.screen = screen;
	}

	@Override
	public Screen getCurrentScreen() {
		if (screen != null)
			return screen;
		else
			throw new IllegalStateException("This Game instance does not have an associated screen.");
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
		glGraphics.setGL(gl);

		// Acquire single threaded lock to change state
		synchronized (stateChanged) {
			// Start screen will be created here
			if (state == GLGame.GLGameState.Initialized)
				screen = getStartScreen();
			state = GLGame.GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}

	}

	@Override
	public void onDrawFrame(GL10 gl10) {
		GLGame.GLGameState state = null;

		// The reason we synchronize access to the gameFrag's state is that
		// state may be changed inside the UI's onPause() method
		synchronized (stateChanged) {
			state = this.state;
		}



		switch (state) {
			case Running:
				// Run any pending runnables
				if (runnables.size() > 0) {
					for (Runnable r : runnables) {
						r.run();

					}
					runnables.clear();
				}

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
					this.state = GLGame.GLGameState.Idle;
					stateChanged.notifyAll();
				}
				break;

			case Finished:
				// App has been terminated, pause screen to save gameFrag and
				// disposeAll of it.
				screen.pause();
				screen.dispose();
				synchronized (stateChanged) {
					this.state = GLGame.GLGameState.Idle;
					stateChanged.notifyAll();
				}
				break;

			case Initialized:
				break;
			case Idle:
				break;
		}

	}

	public void postRunnable(Runnable runnable) {
		this.runnables.add(runnable);
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int w, int h) {
		SCREEN_W = w;
		SCREEN_H = h;
		screen.resize(w, h);
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		throw new IllegalArgumentException("Using OpenGL ES to draw. No Canvas available.");
	}

	@Override
	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	@Override
	public AndroidAudio getAudio() {
		return audio;
	}
}
