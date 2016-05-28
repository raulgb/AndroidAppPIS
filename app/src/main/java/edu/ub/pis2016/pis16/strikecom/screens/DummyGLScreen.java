package edu.ub.pis2016.pis16.strikecom.screens;

import android.content.Context;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.controller.GameContactListener;
import edu.ub.pis2016.pis16.strikecom.controller.VehicleTouchController;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameMap;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.math.WindowedMean;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.physics.StaticBody;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBase;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.CameraBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.factories.EnemyFactory;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.MAP_SIZE;
import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILES_ON_SCREEN;
import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILE_SIZE;

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

	/** Total Seconds the Game has lasted */
	float secondsElapsed;

	private GLGraphics glGraphics;
	private SpriteBatch batch;

	OrthoCamera camera;

	Physics2D physics2D;
	GameMap gameMap;
	Sprite healthBarSprite;
	StrikeBase strikeBase;

	FragmentedGameActivity activity;

	public static int FRAME = 0;


	private Vector2 targetPos = new Vector2();
	private Vector2 tmp = new Vector2();

	public DummyGLScreen(final Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		glGraphics = game.getGLGraphics();

		activity = (FragmentedGameActivity) ((StrikeComGLGame) game).getActivity();

		// Create camera, set zoom to fit TILES_ON_SCREEN, rounding to nearest int
		camera = new OrthoCamera(glGraphics, glGraphics.getWidth(), glGraphics.getHeight());
		camera.putComponent(new CameraBehavior());

		float zoomFactor = glGraphics.getWidth() / (float) (TILES_ON_SCREEN * TILE_SIZE);
		camera.zoom = 1f / (int) zoomFactor;
		camera.setLayer(LAYER_GUI);
		addGameObject("OrthoCamera", camera);

		physics2D = new Physics2D(MAP_SIZE, MAP_SIZE);
		batch = new SpriteBatch(game.getGLGraphics(), 1024);
		gameMap = new GameMap(physics2D, TILE_SIZE, 0L, 16, 2, 0.5f);
		gameMap.setDrawDistance(GameConfig.TILES_ON_SCREEN / 2 + 1);

		healthBarSprite = new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar", 0));

		createGameObjects();
		commitGameObjectChanges();

		camera.getComponent(CameraBehavior.class).setTracking(strikeBase);

		// Projectile CONTACT LISTENER
		physics2D.addContactListener(new GameContactListener(this));

		// Move order Input
		addInputProcessor(new VehicleTouchController(this, strikeBase));
	}

	WindowedMean fpsMean = new WindowedMean(30);
	float secondsCounter = 0;

	@Override
	public void update(float delta) {
		if (gamePaused)
			return;

//		delta = 0.016f; // for debugging
		delta = MathUtils.min(1, delta);
		FRAME += 1;

		super.update(delta);

		// FPS Counter
		fpsMean.addValue(delta);
		secondsCounter += delta;
		if (secondsCounter > 5) {
			secondsCounter -= 5;
			Log.i("FPS", "" + MathUtils.roundPositive(1f / fpsMean.getMean()));
//			System.gc();
//			for (GameObject go : getGameObjects())
//				System.out.println(go);
		}

		secondsElapsed += delta;

		// Step physics simulation
		physics2D.update(delta);

		// Update GameObjects
		for (GameObject go : this.getGameObjects())
			go.update(delta);

		gameMap.update(delta);

	}

	private Rectangle clipBounds = new Rectangle(0, 0, 1, 1);

	@Override
	public void present(float delta) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(Assets.SPRITE_ATLAS.getTexture());

		gameMap.draw(batch, camera.position);

		// Calculate a triangle which defines an area where object are drawn.
		// Any object outside this rectangle is not drawn
		float clipBreath = TILES_ON_SCREEN * TILE_SIZE * 1.2f;
		clipBounds.set(
				camera.position.x - clipBreath / 2f,
				camera.position.y - clipBreath / 2f,
				clipBreath, clipBreath
		);

		for (GameObject go : this.getGameObjects())
			if (!go.hasComponent(PhysicsComponent.class) || clipBounds.contains(go.getPosition()))
				go.draw(batch);

		//physics2D.debugDraw(batch);

		batch.end();

		// Draw healthbar
		camera.GUIProjection();
		batch.begin(Assets.SPRITE_ATLAS.getTexture());
		float HBWidth = glGraphics.getWidth() * 0.8f * ((float) strikeBase.hitpoints / strikeBase.maxHitpoints);
		healthBarSprite.setSize(HBWidth, 48);
		healthBarSprite.draw(batch, glGraphics.getWidth() / 2f, glGraphics.getHeight() - 48);
		batch.end();

//		camera.rotation = strikeBase.getPhysics().getRotation() - 90;
		camera.updateOrtho();
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
		// TODO resize camera here
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

	@Override
	public Physics2D getPhysics2D() {
		return physics2D;
	}

	private void createGameObjects() {
		// ------ SHOP -----------------
		GameObject shop = new GameObject();
		shop.putComponent(new PhysicsComponent(new StaticBody(new Rectangle(1.5f, 1.5f))));
		shop.getPhysics().body.filter = Physics2D.Filter.SHOP;
		shop.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("shop")));
		shop.setTag("shop_1");
		shop.setLayer(LAYER_BACKGROUND);
		shop.setPosition((MAP_SIZE / 2f - 4) * TILE_SIZE, MAP_SIZE / 2f * TILE_SIZE);
		addGameObject("shop_1", shop);

		// ------ STRIKEBASE CONFIG ------------
		strikeBase = new StrikeBase(new StrikeBaseConfig(strikeBaseModel));
		strikeBase.addOnDestroyAction(new Runnable() {
			@Override
			public void run() {
				activity.showGameOverDialog();
			}
		});

		strikeBase.putComponent(new VehicleFollowBehavior());
		strikeBase.getComponent(VehicleFollowBehavior.class).setMinRange(1.5f * TILE_SIZE);
		strikeBase.setTag("player_strikebase");
		strikeBase.setLayer(LAYER_STRIKEBASE);
		strikeBase.setPosition(MAP_SIZE / 2f * TILE_SIZE, MAP_SIZE / 2f * TILE_SIZE);
		strikeBase.getPhysics().body.filter = Physics2D.Filter.PLAYER;
		strikeBase.hitpoints = 500;
		strikeBase.maxHitpoints = 500;
		strikeBase.killable = true;
		camera.setPosition(strikeBase.getPosition());
		addGameObject("StrikeBase", strikeBase);

		commitGameObjectChanges();

		createEnemy();
		createEnemy();

	}

	/** Create a new tank enemy which spawns somewhere random on the map */
	private void createEnemy() {
		if (!strikeBase.isValid())
			return;

		GameObject enemy = EnemyFactory.createRandomEnemyTank(this);
		if (enemy != null)
			enemy.addOnDestroyAction(new Runnable() {
				@Override
				public void run() {
					createEnemy();
					if (Math.random() > 0.5f)
						createEnemy();
				}
			});

		addHealthBar(enemy);
	}

	private void addHealthBar(final GameObject owner) {
		final GameObject healthBar = new GameObject();
		healthBar.putComponent(new PhysicsComponent());
		healthBar.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("healthbar", 0)));
		healthBar.setLayer(LAYER_GUI);

		healthBar.putComponent(new BehaviorComponent() {
			public void init() {
				// Put parent adquisition here for delayed start, otherwise it would crash if we'd add
				// one object after the other
				gameObject.setParent(owner);
			}

			@Override
			public void update(float delta) {
				GameObject owner = gameObject.getParent();
				if (!owner.hasComponent(GraphicsComponent.class))
					return;

				Sprite selfSprite = gameObject.getComponent(GraphicsComponent.class).getSprite();
				Sprite ownerSprite = owner.getComponent(GraphicsComponent.class).getSprite();
				gameObject.setPosition(owner.getPosition().add(0, ownerSprite.getSize() * 0.6f));

				float healthPerc = 100 * (float) owner.hitpoints / owner.maxHitpoints;

				if (healthPerc > 75)
					selfSprite.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 0));
				else if (healthPerc > 50)
					selfSprite.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 1));
				else if (healthPerc > 25)
					selfSprite.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 2));
				else
					selfSprite.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 3));

				healthPerc /= 100;

				// Set the healthbar width to the parent's width * perc health
				selfSprite.setSize(healthPerc * ownerSprite.getSize(), 0.15f * TILE_SIZE);
			}
		});
		// Add to Screen
		addGameObject(healthBar);
	}

	@Deprecated
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
		return density_id * 6f / 3.0f;
	}

}
