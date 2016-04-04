package edu.ub.pis2016.pis16.strikecom.screens;

import android.content.Context;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.engine.util.Pool;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBaseTest;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.ProjectileBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;

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
	float secondsElapsed;

	GameObject enemy;
	GameObject moveIcon;
	StrikeBaseTest strikeBase;

	TextureSprite grass;

	public Pool<GameObject> projectilePool;

	private Vector2 targetPos = new Vector2();
	private Vector2 tmp = new Vector2();
	private Vector2 camPos = new Vector2();

	public DummyGLScreen(Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		glGraphics = game.getGLGraphics();
		batch = new SpriteBatch(game.getGLGraphics(), 512);

		projectilePool = new Pool<>(new Pool.PoolObjectFactory<GameObject>() {
			@Override
			public GameObject createObject() {
				// Create a Bullet gameObject
				GameObject projectile = new GameObject();
				projectile.setLayer(Screen.LAYER_2);
				projectile.putComponent(new PhysicsComponent());
				projectile.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("bullet")));
				projectile.getComponent(GraphicsComponent.class).getSprite().setScale(0.3f);
				projectile.putComponent(new ProjectileBehavior());
				return projectile;
			}
		}, 64);

		strikeBase = new StrikeBaseTest(new StrikeBaseConfig(StrikeBaseConfig.Model.MKII));
		strikeBase.putComponent(new VehicleFollowBehavior());
		strikeBase.setTag("player");
		strikeBase.setLayer(LAYER_1);
		putGameObject("StrikeBase", strikeBase);

		// Create an  Enemy GameObject
		enemy = new GameObject();
		enemy.setLayer(LAYER_1);
		enemy.setTag("enemy");
		enemy.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("enemy")));
		enemy.putComponent(new PhysicsComponent());
		enemy.getComponent(PhysicsComponent.class).setPosition(64, 0);
		enemy.getComponent(GraphicsComponent.class).getSprite().setScale(0.5f);
		putGameObject("Enemy", enemy);

		moveIcon = new GameObject();
		moveIcon.setLayer(LAYER_BACKGROUND);
		moveIcon.putComponent(new PhysicsComponent());
		moveIcon.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("cursor_move")));
		moveIcon.getComponent(GraphicsComponent.class).getSprite().setScale(0.3f);
		putGameObject("MoveIcon", moveIcon);

		grass = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("grass"));
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
		super.update(delta);

		secondsElapsed += delta;

		for (Input.TouchEvent e : game.getInput().getTouchEvents()) {
			if (e.type == Input.TouchEvent.TOUCH_UP)
				continue;

			// Manually unproject screen to world:
			// Invert y axis
			// substract half-width and half-height
			// scale by the zoom (zoom factor x8)
			// add camera offset
			e.y = glGraphics.getHeight() - e.y;
			e.x = (e.x - glGraphics.getWidth() / 2) / 8 + (int) camPos.x;
			e.y = (e.y - glGraphics.getHeight() / 2) / 8 + (int) camPos.y;
			targetPos.set(e.x, e.y);

			strikeBase.getComponent(VehicleFollowBehavior.class).setTarget(targetPos);
			moveIcon.getComponent(PhysicsComponent.class).setPosition(targetPos);
		}

		// Move enemy around
		Vector2 oldPos = getGameObject("Enemy").getComponent(PhysicsComponent.class).getPosition();
		tmp.set(MathUtils.cosDeg(secondsElapsed * 10) * 64, MathUtils.sinDeg(secondsElapsed * 10) * 64);
		float angle = tmp.sub(oldPos).angle();

		getGameObject("Enemy").getComponent(PhysicsComponent.class).setPosition(tmp.add(oldPos));
		getGameObject("Enemy").getComponent(PhysicsComponent.class).setRotation(angle);

		// Update GameObjects
		for (GameObject go : this.getGameObjects())
			go.update(delta);

		// Move camera to strikebase
		camPos.set(strikeBase.getComponent(PhysicsComponent.class).getPosition());
		camPos.add(strikeBase.getComponent(PhysicsComponent.class).getVelocity());
		updateCamera(glGraphics.getWidth(), glGraphics.getHeight(), 1 / getZoomConstant());

		commitGameObjectChanges();
	}

	private float getZoomConstant() {
		Context ctx = ((StrikeComGLGame) game).getActivity().getApplicationContext();

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

		for (GameObject go : this.getGameObjects())
			go.draw(batch);

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
