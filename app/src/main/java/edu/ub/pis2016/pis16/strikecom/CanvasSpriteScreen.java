package edu.ub.pis2016.pis16.strikecom;

import android.graphics.Color;
import android.util.Log;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.graphics.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.graphics.SpriteGrid;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.math.WindowedMean;
import edu.ub.pis2016.pis16.strikecom.entity.StrikeBase;

public class CanvasSpriteScreen extends Screen {

	OrthoCamera camera;
	StrikeBase strikeBase;
	Sprite[] sprites;
	SpriteGrid grid;
	Graphics g;

	public CanvasSpriteScreen(Game game) {
		super(game);

		this.g = game.getGraphics();

		camera = new OrthoCamera(1920, 1080);
//		camera.setPosition(1920 / 2, 1080 / 2);
		camera.setZoom(16);
		camera.rotateTo(0f);

		strikeBase = new StrikeBase(game);

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

	@Override
	public void update(float delta) {
		sum += delta * 60;
		ease = (MathUtils.cosDeg(sum) + 1) * 0.5f;

		strikeBase.update(delta);

		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			camera.unproject(touch.set(e.x, e.y));
			switch (e.type) {
				case Input.TouchEvent.TOUCH_DOWN:
					orig.set(touch);

					Vector2 movDelta = new Vector2(touch.x, touch.y).sub(strikeBase.pos);
					movDelta.nor().scl(8);
					strikeBase.vel.set(movDelta);

					break;
				case Input.TouchEvent.TOUCH_DRAGGED:
					drag.set(touch);
					this.delta.set(orig).sub(drag);
					camera.translate(this.delta.scl(0.5f));
					break;
			}
		}


		camera.pos.set(strikeBase.pos);
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
		g.clear(Color.CYAN);

		g.setTransformation(camera.combined);

		// Draw axes
//		g.drawRect(-50, -50, 100, 100, Color.BLUE);
		g.drawRect(-100, -5, 200, 10, Color.RED);
		g.drawRect(0, -5, 200, 10, Color.RED);
		g.drawRect(-5, -100, 10, 200, Color.GREEN);
		g.drawRect(-5, 0, 10, 200, Color.GREEN);


		strikeBase.draw(g);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		for (Sprite s : sprites)
			s.dispose();
	}
}
