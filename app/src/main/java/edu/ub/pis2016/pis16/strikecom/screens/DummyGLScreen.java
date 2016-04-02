package edu.ub.pis2016.pis16.strikecom.screens;

import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
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

	private int SW, SH;

	SpriteBatch batch;

	StrikeBaseTest strikeBase;
	ArrayList<Turret> turrets;

	TextureSprite moveIcon;

	public DummyGLScreen(Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		strikeBase = new StrikeBaseTest("sbmk2");
		turrets = new ArrayList<>(4);
		turrets.add(new Turret("turret_mk1", strikeBase, "turret_1"));
		turrets.add(new Turret("turret_mk1", strikeBase, "turret_2"));
		turrets.add(new Turret("turret_mk1", strikeBase, "turret_3"));

		batch = new SpriteBatch(game.getGLGraphics(), 16);

		moveIcon = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("cursor_move"));
		moveIcon.setScale(2, 2);

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

	Vector2 targetPos = new Vector2();
	Vector2 tmp = new Vector2();

	@Override
	public void update(float delta) {
		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			if (e.type != Input.TouchEvent.TOUCH_DOWN)
				continue;
			e.y = SH - e.y;
			targetPos.set(e.x, e.y);
		}

		Vector2 sbPos = strikeBase.getPosition();
		float sbRot = strikeBase.getRotation();

		// Move AI
		if (tmp.set(targetPos).sub(sbPos).len2() > 100 * 100) {
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

		moveIcon.draw(batch, targetPos.x, targetPos.y);

		strikeBase.draw(batch);
		for (Turret t : turrets)
			t.draw(batch);

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
