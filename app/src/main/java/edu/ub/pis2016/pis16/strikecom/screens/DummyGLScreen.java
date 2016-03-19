package edu.ub.pis2016.pis16.strikecom.screens;

import android.opengl.GLU;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;

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

	private int SW, SH;

	Texture atlas;
	TextureRegion strikeBaseMK2;
	SpriteBatch batch;

	Vector2 pos = new Vector2();

	public DummyGLScreen(Game game) {
		super(game);
		Log.i("SCREEN", "Created");
	}

	@Override
	public void resume() {
		Log.i("SCREEN", "Resumed");

		batch = new SpriteBatch(game.getGLGraphics(), 16);
		atlas = new Texture(game, "strikebase/strikebase_atlas.png");
		strikeBaseMK2 = new TextureRegion(atlas, 32 * 2, 0, 32, 32);
	}

	@Override
	public void resize(int width, int height) {
		Log.i("SCREEN", "Resized: " + width + "x" + height);
		SW = width;
		SH = height;

		GL10 gl = game.getGLGraphics().getGL();
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);

		float viewW = width / 8f;
		float viewH = height / 8f;

		// Setup viewport
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, viewW, 0, viewH, 1, -1);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glScalef(8, 8, 1);

		// Enable blend and texturing
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	@Override
	public void update(float deltaTime) {
		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			e.y = SH - e.y;
			e.x /= 8;
			e.y /= 8;


			Log.i("Touch", e.x + " " + e.y);
			pos.set(e.x, e.y);
		}

	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(atlas);
		batch.drawSprite(pos.x, pos.y, strikeBaseMK2);
		batch.end();

	}

	@Override
	public void pause() {
		Log.i("SCREEN", "Paused");

	}


	@Override
	public void dispose() {
		Log.i("SCREEN", "Disposed");

	}
}
