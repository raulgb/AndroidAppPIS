package edu.ub.pis2016.pis16.strikecom.screens;

import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.entity.StrikeBaseTest;
import edu.ub.pis2016.pis16.strikecom.entity.Turret;

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
	GLGraphics glGraphics;
	SpriteBatch batch;

	StrikeBaseTest strikeBase;
	ArrayList<Turret> turrets;

	TextureSprite grid;
	TextureSprite moveIcon;
	TextureSprite dot;

	private Vector2 targetPos = new Vector2();
	private Vector2 tmp = new Vector2();
	private Vector2 camPos = new Vector2();

	public DummyGLScreen(Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		glGraphics = game.getGLGraphics();

		strikeBase = new StrikeBaseTest("sbmk2");

		turrets = new ArrayList<>(4);
		turrets.add(new Turret("turret_mk1", strikeBase, "turret_1"));
		turrets.add(new Turret("turret_mk1", strikeBase, "turret_2"));
		turrets.add(new Turret("turret_mk1", strikeBase, "turret_3"));

		batch = new SpriteBatch(game.getGLGraphics(), 128);

		moveIcon = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("cursor_move"));
		moveIcon.setScale(0.25f);

		grid = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("grid"));
		dot = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dot"));
		dot.setScale(0.5f);

		Log.i("Sprite", moveIcon.getRegion().toString());
	}

	@Override
	public void resume() {
		Log.i("DUMMY_SCREEN", "Resumed");
		Texture.reloadManagedTextures();

		GL10 gl = game.getGLGraphics().getGL();
		gl.glClearColor(.25f, .75f, .25f, 1f);
	}

	@Override
	public void resize(int width, int height) {
		Log.i("DUMMY_SCREEN", "Resized: " + width + "x" + height);

		// Crude 2D Camera
		GL10 gl = game.getGLGraphics().getGL();
		GLGraphics glGraphics = game.getGLGraphics();

		float frustumWidth = width;
		float frustumHeight = height;
		float zoom = 1 / 8f;

		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(
				camPos.x - frustumWidth * zoom / 2,
				camPos.x + frustumWidth * zoom / 2,
				camPos.y - frustumHeight * zoom / 2,
				camPos.y + frustumHeight * zoom / 2,
				1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}


	@Override
	public void update(float delta) {
		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			if (e.type != Input.TouchEvent.TOUCH_DOWN)
				continue;
			e.y = glGraphics.getHeight() - e.y;
			e.x = (e.x - glGraphics.getWidth() / 2) / 8;
			e.y = (e.y - glGraphics.getHeight() / 2) / 8;
			targetPos.set(e.x, e.y);
		}

		Vector2 sbPos = strikeBase.getPosition();
		float sbRot = strikeBase.getRotation();

		// Move AI
		if (tmp.set(targetPos).sub(sbPos).len2() > 5 * 5) {
			float angleDelta = Angle.angleDelta(sbRot, tmp.angle());
			if (Math.abs(angleDelta) > 5) {
				if (angleDelta > 0)
					strikeBase.turnLeft();
				else
					strikeBase.turnRight();
			} else {
				strikeBase.accelerate();
			}
		} else {
			strikeBase.brake();
		}


		strikeBase.update(delta);
		for (Turret t : turrets) {
			t.aimAt(targetPos);
			t.update(delta);
		}

	}


	@Override
	public void present(float delta) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(Assets.SPRITE_ATLAS.getTexture());

		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++)
				grid.draw(batch, x * 32, y * 32);

		moveIcon.draw(batch, targetPos.x, targetPos.y);
		strikeBase.draw(batch);
		for (Turret t : turrets)
			t.draw(batch);

//		Vector2 lThread = strikeBase.getAnchor("left_thread");
//		Vector2 rThread = strikeBase.getAnchor("right_thread");
//		dot.draw(batch, lThread.x, lThread.y);
//		dot.draw(batch, rThread.x, rThread.y);

		batch.end();
	}

	@Override
	public void pause() {
		Log.i("DUMMY_SCREEN", "Paused");
	}

	@Override
	public void dispose() {
		Log.i("DUMMY_SCREEN", "Disposed");
		Assets.disposeAll();
	}
}
