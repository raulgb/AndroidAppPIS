package edu.ub.pis2016.pis16.strikecom.screens;

import android.util.Log;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Body;
import edu.ub.pis2016.pis16.strikecom.engine.physics.ContactListener;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;

/**
 * Dummy OpenGL screen.
 * <p/>
 * Order of calls:
 * - Created
 * - Resumed
 * - Resized
 * <p/>
 * Loop:
 * - Update
 * - Presented
 * <p/>
 * On back:
 * - Paused
 * - Disposed
 */
public class DummyGLScreen extends Screen {


	public DummyGLScreen(Game game) {
		super(game);
		Log.i("SCREEN", "Created");




	}

	@Override
	public void resize(int width, int height) {
		Log.i("SCREEN", "Resized: " + width + "x" + height);

		GL10 gl = game.getGLGraphics().getGL();
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = game.getGLGraphics().getGL();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);


	}

	@Override
	public void pause() {
		Log.i("SCREEN", "Paused");

	}

	@Override
	public void resume() {
		Log.i("SCREEN", "Resumed");

	}

	@Override
	public void dispose() {
		Log.i("SCREEN", "Disposed");

	}
}
