package edu.ub.pis2016.pis16.strikecom.screens;

import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidSprite;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.android.CanvasOrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.math.WindowedMean;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBase;

public class Test2DScreen extends Screen {

	CanvasOrthoCamera camera;

	Sprite terrain;
	StrikeBase strikeBase;

	Graphics g;
	Matrix uiMatrix;


	public Test2DScreen(Game game) {
		super(game);

		g = game.getGraphics();
		camera = new CanvasOrthoCamera(g.getWidth(), g.getHeight());
		camera.setZoom(16f);

		uiMatrix = new Matrix();
		uiMatrix.setTranslate(0, g.getHeight());
		uiMatrix.preScale(1, -1);

		strikeBase = new StrikeBase(game);

		Pixmap p = g.newPixmap("terrain.jpg");
		p.mirrorY();
		terrain = new AndroidSprite(p);

	}

	WindowedMean fpsMean = new WindowedMean(15);
	float acc = 0;
	int fpsSecondDelay = 2;

	@Override
	public void update(float delta) {

		handleControls();
		strikeBase.update(delta);

		camera.setPosition(strikeBase.pos);
		camera.update();

		fpsMean.addValue(1f / delta);
		acc += delta;
		if (acc > fpsSecondDelay) {
			acc -= fpsSecondDelay;
			Log.i("FPS:", fpsMean.getMean() + "");
		}
	}

	@Override
	public void present(float delta) {
		g.clear(Color.WHITE);

		g.setTransformation(camera.combined);

		terrain.draw(g);

		// 2D axes
		g.drawRect(-10, -5, 5000, 5, Color.RED);
		g.drawRect(-5, -10, 5, 5000, Color.GREEN);

		strikeBase.draw(g);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	Vector2 touch = new Vector2();
	Vector2 dir = new Vector2();

	private void handleControls() {
		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			// Screen space coordinates
			touch.set(e.x, e.y);

			if (e.type == Input.TouchEvent.TOUCH_DOWN || e.type == Input.TouchEvent.TOUCH_DRAGGED) {
				// World space coordinates
				camera.unproject(touch);

				final float strikeBaseSpeed = 32;
				dir.set(touch).sub(strikeBase.pos).nor().scl(strikeBaseSpeed);
				strikeBase.vel.set(dir);
			}
		}

	}
}
