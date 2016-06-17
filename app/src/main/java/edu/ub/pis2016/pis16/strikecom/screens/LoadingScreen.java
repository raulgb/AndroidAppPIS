package edu.ub.pis2016.pis16.strikecom.screens;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

public class LoadingScreen extends Screen {

	Texture texture;
	TextureRegion iconLoading;
	SpriteBatch batch;

	private int SW, SH;

	public LoadingScreen(StrikeComGLGame game) {
		super(game);

		texture = new Texture(game, "loading.jpg");
		iconLoading = new TextureRegion(texture, 0, 0, 512, 512);
		batch = new SpriteBatch(game.getGLGraphics(), 10);

		Assets.loadAssets(game);

	}

	@Override
	public void resume() {
		Texture.reloadManagedTextures();
	}

	@Override
	public void update(float deltaTime) {
		if (Assets.isReady()) {
			Log.i("LoadingScreen", "Switching screens");

			// PUT YOUR TESTING SCREEN HERE
			game.setScreen(new GameScreen(game));
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(texture);
		batch.drawSprite(SW / 2f, SH / 2f, iconLoading);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		Log.i("LOADING_SCREEN", "Resized: " + width + "x" + height);
		SW = width;
		SH = height;

		GL10 gl = game.getGLGraphics().getGL();
		gl.glClearColor(0.113f, 0.113f, 0.113f, 1f);

		float viewW = width;
		float viewH = height;

		// Setup viewport
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, viewW, 0, viewH, 1, -1);
	}

	@Override
	public void pause() {

	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
