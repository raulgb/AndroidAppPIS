package edu.ub.pis2016.pis16.strikecom.screens;

import android.content.Context;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.InputProcessor;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameMap;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.math.WindowedMean;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Body;
import edu.ub.pis2016.pis16.strikecom.engine.physics.ContactListener;
import edu.ub.pis2016.pis16.strikecom.engine.physics.DynamicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.physics.StaticBody;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.engine.util.Pool;
import edu.ub.pis2016.pis16.strikecom.gameplay.ThreadVehicle;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBase;
import edu.ub.pis2016.pis16.strikecom.gameplay.Turret;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.ProjectileBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;

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
public class DummyGLScreen extends Screen {

	/** Total Seconds the Game has lasted */
	float secondsElapsed;

	private GLGraphics glGraphics;
	private SpriteBatch batch;

	OrthoCamera camera;

	Physics2D physics2D;
	GameMap gameMap;

	GameObject moveIcon;
	StrikeBase strikeBase;

	public Pool<GameObject> projectilePool;
	private Vector2 targetPos = new Vector2();
	private Vector2 tmp = new Vector2();

	public DummyGLScreen(final Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		glGraphics = game.getGLGraphics();

		// Create camera, set zoom to fit TILES_ON_SCREEN, rounding to nearest int
		camera = new OrthoCamera(glGraphics, glGraphics.getWidth(), glGraphics.getHeight());
		float zoomFactor = glGraphics.getWidth() / (float) (TILES_ON_SCREEN * TILE_SIZE);
		camera.zoom = 1f / (int) zoomFactor;

		physics2D = new Physics2D(MAP_SIZE, MAP_SIZE);
		batch = new SpriteBatch(game.getGLGraphics(), 512);
		gameMap = new GameMap(physics2D, TILE_SIZE, 0L, 16, 2, 0.5f);
		gameMap.setDrawDistance(GameConfig.TILES_ON_SCREEN / 2 + 1);

		projectilePool = new Pool<>(new Pool.PoolObjectFactory<GameObject>() {
			@Override
			public GameObject createObject() {
				// Create a Bullet gameObject
				GameObject projectile = new GameObject();
				projectile.setLayer(Screen.LAYER_PROJECTILES);
				// Init basic preferences
				Body projBody = new DynamicBody(new Rectangle(1 / 16f, 1 / 16f));
				projectile.putComponent(new PhysicsComponent(projBody));
				projectile.putComponent(new ProjectileBehavior());
				projectile.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("bullet")));
				projectile.getComponent(GraphicsComponent.class).getSprite().setScale(0.3f);
				return projectile;
			}
		}, 64);


		createGameObjects();
		commitGameObjectChanges();

		addHealthBar("StrikeBase");

		Texture.reloadManagedTextures();

		// Projectile CONTACT LISTENER
		physics2D.addContactListener(new ContactListener() {
			@Override
			public void onCollision(CollisionEvent ce) {
				GameObject goA = (GameObject) ce.a.userData;
				GameObject goB = (GameObject) ce.b.userData;

				// Skip gameobjects in the same group / same faction
				if ((goA.group & goB.group) != 0)
					return;

				if (goA.getTag().contains("proj") && !goB.getTag().contains("proj"))
					if (!goA.getTag().contains(goB.getTag())) {
						handleProjectileCollision(goA, goB);
					}
				if (goB.getTag().contains("proj") && !goA.getTag().contains("proj"))
					if (!goB.getTag().contains(goA.getTag())) {
						handleProjectileCollision(goB, goA);
					}
			}

			private void handleProjectileCollision(GameObject projectile, GameObject other) {
				//Log.i("Physics2D", "Someone got hit: " + other.hitpoints);
				other.hitpoints = MathUtils.max(1, other.hitpoints - projectile.hitpoints);

				if (other.hitpoints == 1)
					other.destroy();

				projectile.destroy();

			}
		});



//		// Zoom in/out Input
//		addInputProcessor(new InputProcessor() {
//			@Override
//			public boolean touchDown(float x, float y, int pointer) {
//				tmp.set(x, glGraphics.getHeight() - y);
//
//				if (tmp.x < glGraphics.getWidth() / 4f) {
//					if (tmp.y > glGraphics.getHeight() / 2f)
//						camera.zoom -= 0.025f;
//					else
//						camera.zoom += 0.025f;
//					return true;
//				}
//				return false;
//			}
//
//			@Override
//			public boolean touchDragged(float x, float y, int pointer) {
//				if (tmp.x < glGraphics.getWidth() / 4f)
//					return true;
//
//				return false;
//			}
//		});

		// Move order Input
		addInputProcessor(new InputProcessor() {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				moveOrder(x, y);
				return false;
			}

			@Override
			public boolean touchDragged(float x, float y, int pointer) {
				moveOrder(x, y);
				return false;
			}

			private void moveOrder(float x, float y) {
				targetPos.set(x, y);
				camera.unproject(targetPos);
				moveIcon.setPosition(targetPos);

				GameObject strikeBase = getGameObject("StrikeBase");
				if (strikeBase != null)
					strikeBase.getComponent(VehicleFollowBehavior.class).setTarget(targetPos);
			}
		});
	}

	WindowedMean fpsMean = new WindowedMean(30);
	float second = 0;

	@Override
	public void update(float delta) {
		if (gamePaused)
			return;

		// Slow-motion
//		delta *= 1 / 2f;

		super.update(delta);

		// FPS Counter
		fpsMean.addValue(delta);
		second += delta;
		if (second > 5) {
			second -= 5;
			Log.i("FPS", "" + MathUtils.roundPositive(1f / fpsMean.getMean()));
		}

		secondsElapsed += delta;

		// Step physics simulation
		physics2D.update(delta);

		// Update GameObjects
		for (GameObject go : this.getGameObjects())
			go.update(delta);

		// Sketchy hack to get seamless rendering using the OpenGL camera
		tmp.set(strikeBase.getPosition());
		tmp.x = (int) (tmp.x * 10) / 10f;
		tmp.y = (int) (tmp.y * 10) / 10f;
		camera.position.set(tmp);
		camera.update();
	}

	@Override
	public void present(float delta) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(Assets.SPRITE_ATLAS.getTexture());

		gameMap.draw(batch, camera.position);

		for (GameObject go : this.getGameObjects())
			go.draw(batch);

		batch.end();
	}

	@Override
	public void resume() {
		Log.i("DUMMY_SCREEN", "Resumed");
		//Texture.reloadManagedTextures();

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
		shop.putComponent(new PhysicsComponent( new StaticBody(new Rectangle(1.5f, 1.5f))));
		shop.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("shop")));
		shop.setLayer(LAYER_BUILDING_BOTTOM);
		shop.setPosition((MAP_SIZE / 2f - 4) * TILE_SIZE, MAP_SIZE / 2f * TILE_SIZE);
		addGameObject("shop_1", shop);

		// ------ STRIKEBASE CONFIG ------------
		strikeBase = new StrikeBase(new StrikeBaseConfig(strikeBaseModel));
		strikeBase.putComponent(new VehicleFollowBehavior());
		strikeBase.getComponent(VehicleFollowBehavior.class).setMinRange(1.5f * TILE_SIZE);
		strikeBase.setTag("player_strikebase");
		strikeBase.setLayer(LAYER_STRIKEBASE);
		strikeBase.setPosition(MAP_SIZE / 2f * TILE_SIZE, MAP_SIZE / 2f * TILE_SIZE);
		strikeBase.group = GROUP_PLAYER;
		strikeBase.hitpoints = 200;
		strikeBase.maxHitpoints = 200;
		strikeBase.killable = true;
		addGameObject("StrikeBase", strikeBase);

		// ------ MOVE ICON CONFIG ------------
		moveIcon = new GameObject();
		moveIcon.setLayer(LAYER_GUI);
		moveIcon.putComponent(new PhysicsComponent());
		moveIcon.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("cursor_move")));
		moveIcon.getComponent(GraphicsComponent.class).getSprite().setScale(0.3f);
		addGameObject("MoveIcon", moveIcon);

		createEnemy();
	}

	/** Create a new tank enemy which spawns somewhere random on the map */
	private void createEnemy() {
		// Create 2 new enemies on death
		ThreadVehicle enemyTank = new ThreadVehicle() {
			@Override
			public void destroy() {
				super.destroy();
				// Add Scrap to Player
				FragmentedGameActivity gameActivity = (FragmentedGameActivity)((GLGameFragment)game).getActivity();

				int scrap = (int) gameActivity.playerState.get("SCRAP");
				gameActivity.playerState.put("SCRAP", scrap + this.maxHitpoints);

				createEnemy();
				createEnemy();
			}
		};
		enemyTank.group = GROUP_RAIDERS;
		enemyTank.hitpoints = 20;
		enemyTank.maxHitpoints = 20;
		enemyTank.killable = true;
		enemyTank.setTag("enemy_tank");
		enemyTank.putComponent(new VehicleFollowBehavior());
		enemyTank.getComponent(VehicleFollowBehavior.class).setMinRange(3 * TILE_SIZE);
		enemyTank.putComponent(new BehaviorComponent() {
			@Override
			public void update(float delta) {
				VehicleFollowBehavior vfb = gameObject.getComponent(VehicleFollowBehavior.class);
				vfb.setTarget(strikeBase.getPosition());
			}
		});
		enemyTank.cfg.maxSpeed = 0.5f * TILE_SIZE;
		enemyTank.cfg.accel = 0.1f * TILE_SIZE;

		String tankIdentifier = addGameObject(enemyTank);
		// ------ TANK TURRET -----
		GameObject turret = new Turret("enemy_turret", enemyTank, "turret");
		turret.getComponent(GraphicsComponent.class).getSprite().setSize(1.2f * GameConfig.TILE_SIZE);
		turret.putComponent(new TurretBehavior(new TurretConfig()));
		turret.getComponent(TurretBehavior.class).setTargetTag("player");
		turret.setLayer(Screen.LAYER_VEHICLE_TURRET);
		addGameObject(turret);

		// Spawn it somewhere random
		tmp.set(MathUtils.random(physics2D.getWorldWidth()) * TILE_SIZE, MathUtils.random(physics2D.getWorldHeight()) * TILE_SIZE);
		enemyTank.getComponent(PhysicsComponent.class).setPosition(tmp);

		// Add a healthbar for the new tank
		addHealthBar(tankIdentifier);
	}

	private void addHealthBar(final String gameObjIdentifier) {
		GameObject healthBar = new GameObject();
		healthBar.putComponent(new PhysicsComponent());
		healthBar.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("healthbar", 0)));
		healthBar.setLayer(LAYER_GUI);
		healthBar.putComponent(new BehaviorComponent() {
			public void init() {
				// Put parent adquisition here for delayed start, otherwise it would crash if we'd add
				// one object after the other
				GameObject owner = getGameObject(gameObjIdentifier);
				gameObject.setParent(owner);
			}

			@Override
			public void update(float delta) {
				GameObject owner = gameObject.getParent();
				gameObject.setPosition(owner.getPosition().add(0, TILE_SIZE));
				gameObject.getComponent(GraphicsComponent.class)
						.getSprite()
						.setScale(3f * ((float) owner.hitpoints / owner.maxHitpoints), 0.25f);
			}
		});
		// Add to Screen
		addGameObject(healthBar);
	}

	private void removeEnemy(GameObject enemy) {

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
