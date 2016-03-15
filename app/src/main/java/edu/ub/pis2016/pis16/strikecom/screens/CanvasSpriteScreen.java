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
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.math.WindowedMean;
import edu.ub.pis2016.pis16.strikecom.entity.StrikeBase;

public class CanvasSpriteScreen extends Screen {

	CanvasOrthoCamera camera;

	Sprite terrain;
	StrikeBase strikeBase;

	Sprite[] sprites;
	Graphics g;

	Matrix uiMatrix;

	public CanvasSpriteScreen(Game game) {
		super(game);

		this.g = game.getGraphics();
		uiMatrix = new Matrix();
		uiMatrix.setTranslate(0, g.getHeight());
		uiMatrix.preScale(1, -1);

		camera = new CanvasOrthoCamera(1920, 1080);
		camera.setZoom(10);
		camera.rotateTo(0f);

		strikeBase = new StrikeBase(game);

		Pixmap terr = g.newPixmap("terrain.jpg");
		terr.mirrorY();
		terrain = new AndroidSprite(terr);
		terrain.translate(-100, -100);
		//terrain.scaleTo(0.5f, 0.5f);

//		int nSprites = 25;
//		sprites = new Sprite[nSprites];
//		for (int i = 0; i < nSprites; i++) {
//			sprites[i] = new AndroidSprite(pixmap);
//			sprites[i].setOrigin(0.5f, 0.5f);
//			sprites[i].translateTo(MathUtils.random(0f, 1920f), MathUtils.random(0, 1080));
//		}
	}

	public Vector2 touch = new Vector2();
	public Vector2 orig = new Vector2();
	public Vector2 drag = new Vector2();
	public Vector2 delta = new Vector2();

	float ease, sum;

	Vector2 rotation = new Vector2();
	WindowedMean angleMean = new WindowedMean(10);

	@Override
	public void update(float delta) {
		sum += delta * 60;
		ease = (MathUtils.cosDeg(sum) + 1) * 0.5f;

		// Get gyroscope rotation, 0º is up, positive is clockwise
		rotation.set(game.getInput().getAccelY(), game.getInput().getAccelX());
		angleMean.addValue(90 - rotation.angle());


		// Add rotation to the strikebase angle
		Vector2 accel = new Vector2(0.5f, 0).rotate(-angleMean.getMean() * 0.85f);
		accel.rotate(strikeBase.getAngle());
		strikeBase.vel.add(accel);
		strikeBase.vel.nor().scl(14);

		// Rotate camera inverse of real rotation -> keep world aligned with eyes
		camera.rotateTo(-angleMean.getMean());

		//camera.setZoom(10 + ease * 2f);

		strikeBase.update(delta);

		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {

			if (e.x < g.getWidth() * 5 / 16f)
				// sidebar.touch(e.x, e.y))
				return;

			//else
			// controller.touch(e.x, e.y);

			camera.unproject(touch.set(e.x, e.y));

			switch (e.type) {
				case Input.TouchEvent.TOUCH_DOWN:
					orig.set(touch);

					Vector2 movDelta = new Vector2(touch.x, touch.y).sub(strikeBase.pos);
					movDelta.nor().scl(16);
					strikeBase.vel.set(movDelta);

					break;
				case Input.TouchEvent.TOUCH_DRAGGED:
					drag.set(touch);
					this.delta.set(orig).sub(drag);
					camera.translate(this.delta.scl(0.5f));
					break;
			}
		}


		camera.setPosition(strikeBase.pos);
		//camera.translate(-26, 0);
		camera.update();


		fpsMean.addValue(1f / delta);
		acc += delta;
		if (acc > fpsSecondDelay) {
			acc -= fpsSecondDelay;
			Log.i("FPS:", fpsMean.getMean() + "");
		}
	}

	float acc = 0;
	int fpsSecondDelay = 2;
	WindowedMean fpsMean = new WindowedMean(20);

	@Override
	public void present(float delta) {
		g.clear(0x00FF8F3B1B);

		g.setTransformation(camera.combined);

		// Draw axes
//		g.drawRect(-50, -50, 100, 100, Color.BLUE);
//		g.drawRect(-100, -5, 200, 10, Color.RED);
//		g.drawRect(0, -5, 200, 10, Color.RED);
//		g.drawRect(-5, -100, 10, 200, Color.GREEN);
//		g.drawRect(-5, 0, 10, 200, Color.GREEN);

		terrain.draw(g);
		strikeBase.draw(g);

		g.setTransformation(uiMatrix);

		g.drawRect(0, 0, 5f / 16f * g.getWidth(), g.getHeight(), Color.GRAY);

		float minimapDim = g.getWidth() * 5 / 32f;
		g.drawRect(0, g.getHeight() - minimapDim, minimapDim, minimapDim, Color.WHITE);


		g.drawRect(40, 40, 500, 24 * 4f, Color.RED);
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
//		for (Sprite s : sprites)
//			s.dispose();
	}
}
