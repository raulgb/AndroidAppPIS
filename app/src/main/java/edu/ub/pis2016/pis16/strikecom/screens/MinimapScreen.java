package edu.ub.pis2016.pis16.strikecom.screens;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameMap;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;


public class MinimapScreen extends Screen {

	private GLGraphics glGraphics;
	private SpriteBatch batch;
	private OrthoCamera camera;

	private GameMap gameMap;
	private Vector2 screenCenter = new Vector2();

	Sprite sprite = new Sprite(Assets.SPRITE_ATLAS.getRegion("enemy_large"));

	public MinimapScreen(Game game) {
		super(game);
	}

	public void setGameMap(GameMap map) {
		if (map == null)
			throw new IllegalArgumentException("GameMap can't be null");
		this.gameMap = map;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (gameMap == null)
			return;

		camera.updateOrtho();

		batch.begin(Assets.SPRITE_ATLAS.getTexture());
		// Draw zoomed minimap
		gameMap.drawMiniMap(batch, screenCenter);
		sprite.draw(batch, 0, 0);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		Log.i("MinimapScreen", "Resized: " + width + "x" + height);
	}

	@Override
	public void pause() {
		Log.i("MinimapScreen", "Paused");
	}

	@Override
	public void resume() {
		Log.i("MinimapScreen", "Resumed");
		Texture.reloadManagedTextures();

		glGraphics = game.getGLGraphics();

		camera = new OrthoCamera(glGraphics, glGraphics.getWidth(), glGraphics.getHeight());
		camera.zoom = 1 / 16f;
		camera.setLayer(LAYER_GUI);
		camera.setTag("ortho_camera");
		addGameObject("OrthoCamera", camera);

		//screenCenter.set(glGraphics.getWidth() / 2f, glGraphics.getHeight() / 2f);
		camera.setPosition(screenCenter);
	}

	@Override
	public void dispose() {
		Log.i("MinimapScreen", "Disposed");
	}
}
