package edu.ub.pis2016.pis16.strikecom.screens;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
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
	TextureSprite grass;
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

		putGameObject("StrikeBase", strikeBase);

		batch = new SpriteBatch(game.getGLGraphics(), 512);

		moveIcon = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("cursor_move"));
		moveIcon.setScale(0.5f);

		grass = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("grass"));
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

		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}


	@Override
	public void update(float delta) {
		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			if (e.type == Input.TouchEvent.TOUCH_UP)
				continue;

			// Manually unproject screen to world:
			// Invert y axis
			// substract half-width and half-height
			// scale by the zoom (zoom factor x8)
			// add camera offset
			e.y = glGraphics.getHeight() - e.y;
			e.x = (e.x - glGraphics.getWidth() / 2) / 8 + (int)camPos.x;
			e.y = (e.y - glGraphics.getHeight() / 2) / 8 + (int)camPos.y;
			targetPos.set(e.x, e.y);
		}

		Vector2 sbPos = strikeBase.getPosition();
		float sbRot = strikeBase.getRotation();

		// Move AI, strikebase follows the move pointer
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

		camPos.set(strikeBase.getPosition());
		updateCamera(glGraphics.getWidth(), glGraphics.getHeight(), 1 / getZoomConstant());


		// Update GameObjects
		for(GameObject go : getGameObjects()) {
			if(go instanceof Turret)
				((Turret)go).aimAt(targetPos);

			go.update(delta);
		}

	}

	private float getZoomConstant() {
		Context ctx = ((StrikeComGLGame)game).getActivity().getApplicationContext();

		/*	DENSITY IDs:
				xxxhdpi - 4.0
				xxhdpi - 3.0
				xhdpi - 2.0
				hdpi - 1.5
				tvdpi - 1.33
				mdpi - 1.0
				ldpi - 0.75
		 */
		float density_id = ctx.getResources().getDisplayMetrics().density;

		//Based on the fact that xxhdpi has a constant of 8
		return density_id * 8.0f / 3.0f;
	}


	@Override
	public void present(float delta) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(Assets.SPRITE_ATLAS.getTexture());

		// Draw terrain
		for (int y = -8; y < 8; y++)
			for (int x = -8; x < 8; x++)
				grass.draw(batch, 16 + x * 31.99f, 16 + y * 31.99f);

		moveIcon.draw(batch, targetPos.x, targetPos.y);

		for(GameObject go : this.getGameObjects())
			go.draw(batch);

		// Debug drawing
//		Vector2 lThread = strikeBase.getAnchor("left_thread");
//		Vector2 rThread = strikeBase.getAnchor("right_thread");
//		dot.draw(batch, lThread.x, lThread.y);
//		dot.draw(batch, rThread.x, rThread.y);
//		Vector2 pivot = strikeBase.getAnchor("pivot");
//		dot.draw(batch, pivot.x, pivot.y);

		batch.end();
	}

	/** Manually set the orthographic camera to the camPos vector */
	private void updateCamera(float w, float h, float zoom) {
		// TODO Make this a separate OrthographicCamera class and add rotation
		float frustumWidth = w;
		float frustumHeight = h;

		GL10 gl = game.getGLGraphics().getGL();
		gl.glLoadIdentity();
		gl.glOrthof(
				camPos.x - frustumWidth * zoom / 2,
				camPos.x + frustumWidth * zoom / 2,
				camPos.y - frustumHeight * zoom / 2,
				camPos.y + frustumHeight * zoom / 2,
				1, -1
		);
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
