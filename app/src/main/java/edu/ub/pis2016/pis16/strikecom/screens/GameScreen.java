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
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.HealthBar;
import edu.ub.pis2016.pis16.strikecom.gameplay.Shop;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBase;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.CameraBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.factories.EnemyFactory;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.*;

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
public class GameScreen extends Screen {

	/** Total Seconds the Game has lasted */
	float secondsElapsed;

	private GLGraphics glGraphics;
	private SpriteBatch batch;

	private OrthoCamera camera;

	private Physics2D physics2D;
	private GameMap gameMap;

	private Sprite healthBlackBarSprite;
	private Sprite healthBarSprite;

	StrikeBase strikeBase;

	FragmentedGameActivity activity;

	public GameScreen(final Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		glGraphics = game.getGLGraphics();

		activity = (FragmentedGameActivity) ((StrikeComGLGame) game).getActivity();

		// Create a new OpenGL orthographic projection camera. The constructor also sets up the glViewport()
		camera = new OrthoCamera(glGraphics, glGraphics.getWidth(), glGraphics.getHeight());
		camera.putComponent(new CameraBehavior());
		// Set zoom to fit TILES_ON_SCREEN, rounding to nearest int
		float zoomFactor = glGraphics.getWidth() / (float) (TILES_ON_SCREEN * TILE_SIZE);
		camera.zoom = 1f / (int) zoomFactor;
		camera.setLayer(LAYER_GUI);
		camera.setTag("ortho_camera");
		addGameObject("OrthoCamera", camera);

		// Create highly efficient Sprite drawer, max num of sprites per frame 1024 = 61K sprites/second
		batch = new SpriteBatch(game.getGLGraphics(), 1024);

		// Create Physics2D world definition and linked Tiled Game Map
		physics2D = new Physics2D(MAP_SIZE, MAP_SIZE);

		gameMap = new GameMap(physics2D, TILE_SIZE, MathUtils.random(Long.MAX_VALUE), 16, 2, 0.5f);
		gameMap.setDrawDistance(GameConfig.TILES_ON_SCREEN / 2 + 2);
		gameMap.setLayer(LAYER_TERRAIN);
		addGameObject("GameMap", gameMap);

		healthBarSprite = new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar"));
		healthBlackBarSprite = new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar_bar"));

		createGameObjects();
		commitGameObjectChanges();

		camera.setPosition(strikeBase.getPosition());
		camera.getComponent(CameraBehavior.class).setTracking(strikeBase);
		gameMap.resetDiscovered();

		// Projectile CONTACT LISTENER
		physics2D.addContactListener(new GameContactListener(this));

		// Move order Input
		addInputProcessor(new VehicleTouchController(this, strikeBase));
	}

	/** Delta smoothed over 15 frames for dat buttery smooth 60FPS */
	WindowedMean deltaMean = new WindowedMean(15);
	float fiveSecondsCount = 0;

	@Override
	public void update(float delta) {
		if (gamePaused)
			return;
		// Update all delta counters
		deltaMean.addValue(delta);
		secondsElapsed += delta;
		fiveSecondsCount += delta;

		// Get the delta mean over 15 frames
		delta = deltaMean.getMean();

		super.update(delta);

		// FPS Counter
		if (fiveSecondsCount > 5) {
			if (secondsElapsed < 7)
				gameMap.resetDiscovered();
			fiveSecondsCount -= 5;
			Log.i("FPS", "" + MathUtils.roundPositive(1f / deltaMean.getMean()));
			System.gc();
//			for (GameObject go : getGameObjects())
//				System.out.println(go);
			gameMap.createMiniMap(camera.position, game, this.getGameObjects()); // creates a .png of game map
		}


		// Step physics simulation
		physics2D.update(delta);

		// Update GameObjects
		for (GameObject go : this.getGameObjects())
			go.update(delta);

	}

	private Rectangle clipBounds = new Rectangle(0, 0, 1, 1);

	@Override
	public void present(float delta) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Dank Rotations Dawg
		//camera.rotation = strikeBase.getPhysics().getRotation() - 90;
		camera.updateOrtho();

		batch.begin(Assets.SPRITE_ATLAS.getTexture());

		gameMap.draw(batch, camera.position);

		// Calculate a recangle which defines an area where object are drawn.
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

		// Enable this line to draw hitboxes
		//physics2D.debugDraw(batch);
		batch.end();

		// Draw healthbar
		float HBWidth = glGraphics.getWidth() * 0.8f * ((float) strikeBase.hitpoints / strikeBase.maxHitpoints);
		healthBarSprite.setSize(HBWidth, 48);
		healthBlackBarSprite.setSize(glGraphics.getWidth() * 0.8f, 48);

		camera.GUIProjection();
		batch.begin(Assets.SPRITE_ATLAS.getTexture());
		healthBarSprite.draw(batch, glGraphics.getWidth() / 2f, glGraphics.getHeight() - 48);
		healthBlackBarSprite.draw(batch, glGraphics.getWidth() / 2f, glGraphics.getHeight() - 48);
		batch.end();

	}

	@Override
	public void resume() {
		Log.i("GameScreen", "Resumed");

		Texture.reloadManagedTextures();
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClearColor(.25f, .75f, .25f, 1f);
	}

	@Override
	public void resize(int width, int height) {
		Log.i("GameScreen", "Resized: " + width + "x" + height);
		// TODO resize camera here
	}

	@Override
	public void pause() {
		Log.i("GameScreen", "Paused");
	}

	@Override
	public void dispose() {
		Log.i("GameScreen", "Disposed");
		Assets.disposeAll();
	}

	@Override
	public Physics2D getPhysics2D() {
		return physics2D;
	}

	private void createGameObjects() {
		// ------ SHOP -----------------
		int shopCount = MathUtils.max(1, MAP_SIZE / SHOPS_FACTOR);

		for (int i = 0; i < shopCount; i++) {
			// Vault one out of 10 shops
			boolean isVault = MathUtils.random() > 0.9f;
			String shopName = isVault ? "vault_" + i : "shop_" + i;

			Shop shop = new Shop(false);
			shop.setTag(shopName);
			shop.setLayer(LAYER_BACKGROUND);
			shop.setPosition(
					MathUtils.randomTriangular(0, 1) * MAP_SIZE * TILE_SIZE,
					MathUtils.randomTriangular(0, 1) * MAP_SIZE * TILE_SIZE
			);
			addGameObject(shopName, shop);
			activity.shopMap.put(shopName, shop);
		}
		// After generating all shops, generate their inventories
		activity.generateInventories();


		// ------ STRIKEBASE CONFIG ------------
		strikeBase = new StrikeBase(new StrikeBaseConfig(strikeBaseModel));
		strikeBase.addOnDestroyAction(new Runnable() {
			@Override
			public void run() {
				activity.showGameOverDialog();
			}
		});

		strikeBase.setTag("player_strikebase");
		strikeBase.setLayer(LAYER_STRIKEBASE);
		strikeBase.setPosition(MAP_SIZE / 2f * TILE_SIZE, MAP_SIZE / 2f * TILE_SIZE);
		strikeBase.getPhysics().body.filter = Physics2D.Filter.PLAYER;
		// Vehicle Follow Behavior
		strikeBase.putComponent(new VehicleFollowBehavior());
		strikeBase.getComponent(VehicleFollowBehavior.class).setMinRange(1.5f * TILE_SIZE);
		strikeBase.getComponent(VehicleFollowBehavior.class).setTarget(strikeBase.getPosition());

		strikeBase.faction = GameObject.Faction.PLAYER;
		addGameObject("StrikeBase", strikeBase);
		commitGameObjectChanges();

		camera.setPosition(strikeBase.getPosition());

		for (int i = 0; i < 96; i++)
			createRandomEnemy();

	}

	/** Create a new tank enemy which spawns somewhere random on the map */
	private void createRandomEnemy() {
		if (!strikeBase.isValid())
			return;

		GameObject enemy = EnemyFactory.createRandomEnemyTank(this);

		// Random pos
		enemy.getPhysics().setPosition(
				MathUtils.random(physics2D.getWorldWidth() * TILE_SIZE),
				MathUtils.random(physics2D.getWorldHeight() * TILE_SIZE)
		);
		enemy.getPhysics().setRotation(MathUtils.random(360));
		addGameObject(new HealthBar(enemy));
	}

	private void createRandomStalker(float dist) {
		if (!strikeBase.isValid())
			return;

		GameObject enemy = EnemyFactory.createRandomEnemyTank(this);
		Vector2 position = strikeBase.getPosition();

		// Random pos
		enemy.getPhysics().setPosition(
				position.x + MathUtils.random(-dist * TILE_SIZE, dist * TILE_SIZE),
				position.y + MathUtils.random(-dist * TILE_SIZE, dist * TILE_SIZE)
		);
		enemy.getPhysics().setRotation(MathUtils.random(360));
		addGameObject(new HealthBar(enemy));

	}

	// Enemies swarm you when you run out of fuel.
	public void outOfFuel() {
		for (int i = 0; i < 16; i++)
			createRandomStalker(12f);
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
