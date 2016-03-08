package edu.ub.pis2016.pis16.strikecom;

import android.graphics.Color;
import android.util.Log;

import java.util.Random;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidSprite;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.graphics.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.math.WindowedMean;


public class CanvasRotationScreen extends Screen {

	Graphics g;

	OrthoCamera orthoCam;
	AndroidSprite sprite;
	Random r;

	WindowedMean fpsMean;
	float acc;

	public CanvasRotationScreen(Game game) {
		super(game);
		r = new Random();

		// Mean FPS over 5 seconds
		fpsMean = new WindowedMean(60 * 2);
		g = game.getGraphics();

		orthoCam = new OrthoCamera(1920, 1080);
		orthoCam.setZoom(1f);
		orthoCam.setPosition(1920 / 2f, 1080 / 2f);

		sprite = new AndroidSprite(g.newPixmap("tools.png"));
		sprite.translateTo(g.getWidth() / 2f, g.getHeight() / 2f);
		sprite.setOrigin(0.5f, 0.5f);
		sprite.scaleTo(2, 2);

	}


	float angle = 0;
	Vector2 rotation = new Vector2();
	WindowedMean angleMean = new WindowedMean(1);
	Vector2 screenTouch = new Vector2();
	Vector2 touch1 = new Vector2();
	Vector2 touch2 = new Vector2();

	@Override
	public void update(float deltaTime) {
		// rotation is set so it points UPWARDS
		rotation.set(-game.getInput().getAccelY(), game.getInput().getAccelX());
		angleMean.addValue(-(rotation.angle() - 90));
		float ad = angleMean.getLatest() - angle;
		angle = angle + 0.15f * ad;

		// Log.i("Y", angle + "º");
		// Camera rotations, origin at center of touch
		orthoCam.rotateTo(angle);

		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			screenTouch.set(e.x, e.y);
			orthoCam.unproject(screenTouch);

			switch (e.type) {
				case Input.TouchEvent.TOUCH_DOWN | Input.TouchEvent.TOUCH_DRAGGED:
					switch (e.pointer) {
						case 0:
							touch1.set(screenTouch);
							break;
						case 1:
							touch2.set(screenTouch);
							break;
						default:
							break;
					}
			}
		}

		if (touch1.notZero() && touch2.notZero()) {
			Vector2 dir = new Vector2().set(touch2).sub(touch1);
			//Vector2 dir2 = new Vector2().set(dir);
			float len = dir.len() / 256f;
			float angle = dir.angle();

			sprite.translateTo(touch1.add(dir.scl(0.5f)));
			sprite.rotateTo(-angle);
			sprite.scaleTo(len, len);

			touch1.set(0, 0);
			touch2.set(0, 0);
		}


		// Store new projection matrix and reset camera
		orthoCam.update();
	}

	@Override
	public void present(float deltaTime) {
		g.clear(Color.CYAN);
		g.setTransformation(orthoCam.combined);

		g.drawRect(50, 50, g.getWidth() - 100, g.getHeight() - 100, Color.DKGRAY);
		sprite.draw(g);

//		g.drawLine(-5000, 0, 5000, 0, Color.RED);
//		g.drawLine(0, -5000, 0, 5000, Color.GREEN);
//		g.drawRect(0 ,-10, 150,20, Color.RED);
//		g.drawRect(-10, 0, 20,150, Color.GREEN);
//		g.drawRect(-10, -10, 20,20, Color.BLUE);


		fpsMean.addValue(1f / deltaTime);
		acc += deltaTime;
		if (acc > 2) {
			acc -= 5;
			Log.i("FPS:", fpsMean.getMean() + "");
		}
	}

	@Override
	public void pause() {
		Log.i("Screen", "Paused");

	}

	@Override
	public void resume() {
		Log.i("Screen", "Resumed");

	}

	@Override
	public void dispose() {
		Log.i("Screen", "Disposed");

	}
}
