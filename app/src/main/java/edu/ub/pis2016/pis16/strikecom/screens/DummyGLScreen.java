package edu.ub.pis2016.pis16.strikecom.screens;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.entity.StrikeBaseTest;

/**
 * Dummy OpenGL screen.
 * <p>
 * Order of calls:
 * - Created
 * - Resumed
 * - Resized
 * <p>
 * Loop:
 * - Update
 * - Presented
 * <p>
 * On back:
 * - Paused
 * - Disposed
 */
public class DummyGLScreen extends Screen {

	private int SW, SH;

	SpriteBatch batch;

	StrikeBaseTest strikeBase;


	public DummyGLScreen(Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		strikeBase = new StrikeBaseTest("sbmk2");
		strikeBase.setPosition(SW / 2f, SH / 2f);
		batch = new SpriteBatch(game.getGLGraphics(), 16);

	}

	@Override
	public void resume() {
		Log.i("DUMMY_SCREEN", "Resumed");
		Texture.reloadManagedTextures();

		GL10 gl = game.getGLGraphics().getGL();
		gl.glClearColor(.25f, .50f, .25f, 1f);
	}

	@Override
	public void resize(int width, int height) {
		Log.i("DUMMY_SCREEN", "Resized: " + width + "x" + height);
		SW = width;
		SH = height;

		GL10 gl = game.getGLGraphics().getGL();

		float viewW = width / 1f;
		float viewH = height / 1f;

		// Setup viewport
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, viewW, 0, viewH, 1, -1);
	}

	@Override
	public void update(float delta) {
		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			if (e.type != Input.TouchEvent.TOUCH_DOWN)
				continue;

			e.y = SH - e.y;

			if (e.x < SW / 3f)
				strikeBase.turnLeft();
			else if (e.x > SW * 2 / 3f)
				strikeBase.turnRight();
			else {
				if (e.y > SH / 2f)
					strikeBase.accelerate();
				else
					strikeBase.brake();
			}
		}

		strikeBase.update(delta);
	}


	@Override
	public void present(float delta) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(Assets.SPRITE_ATLAS.getTexture());
		strikeBase.draw(batch);
		batch.end();
	}

	@Override
	public void pause() {
		Log.i("SCREEN", "Paused");

	}

	@Override
	public void dispose() {
		Log.i("SCREEN", "Disposed");

		Assets.disposeAll();
	}
}
