package edu.ub.pis2016.pis16.strikecom.screens;

import android.content.Context;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.InputProcessor;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Sprite;
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
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Body;
import edu.ub.pis2016.pis16.strikecom.engine.physics.ContactListener;
import edu.ub.pis2016.pis16.strikecom.engine.physics.DynamicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.engine.util.Pool;
import edu.ub.pis2016.pis16.strikecom.gameplay.EnemyTest;
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

	float secondsElapsed;

	private GLGraphics glGraphics;
	private SpriteBatch batch;

	OrthoCamera camera;

	/** Size of tiles in pixels */
	private final int TILE_SIZE = 16;
	/** Size of map in tiles */
	private final int MAP_SIZE = 16;

	Physics2D physics2D;
	GameMap gameMap;

	GameObject moveIcon;
	StrikeBaseTest strikeBase;


	public Pool<GameObject> projectilePool;

	private Vector2 targetPos = new Vector2();
	private Vector2 tmp = new Vector2();

	public DummyGLScreen(final Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		glGraphics = game.getGLGraphics();
		camera = new OrthoCamera(glGraphics, glGraphics.getWidth(), glGraphics.getHeight());
		camera.zoom = 1 / getZoomConstant();
		//camera.zoom = 1 / 8f;

		physics2D = new Physics2D(MAP_SIZE * TILE_SIZE, MAP_SIZE * TILE_SIZE);
		batch = new SpriteBatch(game.getGLGraphics(), 2048);
		gameMap = new GameMap(physics2D, 16, 0L, 16, 3, 0.5f);

		projectilePool = new Pool<>(new Pool.PoolObjectFactory<GameObject>() {
			@Override
			public GameObject createObject() {
				// Create a Bullet gameObject
				GameObject projectile = new GameObject();
				projectile.setLayer(Screen.LAYER_3);
				// Init basic preferences
				Body projBody = new DynamicBody(new Rectangle(1, 1));
				projectile.putComponent(new PhysicsComponent(projBody));
				projectile.putComponent(new ProjectileBehavior());
				projectile.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("bullet")));
				projectile.getComponent(GraphicsComponent.class).getSprite().setScale(0.3f);
				return projectile;
			}
		}, 64);

		// ------ STRIKEBASE CONFIG ------------
		strikeBase = new StrikeBaseTest(new StrikeBaseConfig(StrikeBaseConfig.Model.MKII));
		strikeBase.putComponent(new VehicleFollowBehavior());
		strikeBase.setTag("playerStrikeBase");
		strikeBase.setLayer(LAYER_1);
		strikeBase.setPosition(128, 128);
		strikeBase.hitpoints = 20;
		strikeBase.maxHitpoints = 20;
		addGameObject("StrikeBase", strikeBase);

		// HealthBar
		GameObject healthBar = new GameObject();
		healthBar.putComponent(new PhysicsComponent());
		healthBar.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("healthbar", 0)));
		healthBar.setLayer(LAYER_GUI);
		healthBar.putComponent(new BehaviorComponent() {
			@Override
			public void update(float delta) {
				gameObject.setPosition(strikeBase.getPosition().add(0, 24));
				gameObject.getComponent(GraphicsComponent.class).getSprite().setScale(2f * ((float) strikeBase.hitpoints / strikeBase.maxHitpoints), 0.20f);
			}
		});
		addGameObject("HealthBar", healthBar);

		// ------ MOVE ICON CONFIG ------------
		moveIcon = new GameObject();
		moveIcon.setLayer(LAYER_GUI);
		moveIcon.putComponent(new PhysicsComponent());
		moveIcon.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("cursor_move")));
		moveIcon.getComponent(GraphicsComponent.class).getSprite().setScale(0.3f);
		addGameObject("MoveIcon", moveIcon);

		// ------ ENEMY TEST CONFIG ------------
		EnemyTest enemy = new EnemyTest();
		enemy.setTag("enemyTank");
		enemy.getComponent(PhysicsComponent.class).setPosition(64, 86);
		enemy.putComponent(new BehaviorComponent() {
			@Override
			public void update(float delta) {
				if (strikeBase.getPosition().dst2(gameObject.getPosition()) > 32 * 32)
					gameObject.getComponent(VehicleFollowBehavior.class).setTarget(strikeBase.getPosition());
				else
					gameObject.getComponent(VehicleFollowBehavior.class).setTarget(null);
			}
		});
		addGameObject("EnemyTest", enemy);

		Texture.reloadManagedTextures();

		// PHYSICS CONTACT LISTENER
		physics2D.addContactListener(new ContactListener() {
			@Override
			public void onCollision(CollisionEvent ce) {
				GameObject goA = (GameObject) ce.a.userData;
				GameObject goB = (GameObject) ce.b.userData;
				if (goA.getTag().contains("player"))
					handlePlayerCollision((StrikeBaseTest) goA, goB);
				if (goB.getTag().contains("player"))
					handlePlayerCollision((StrikeBaseTest) goB, goA);
			}

			private void handlePlayerCollision(StrikeBaseTest player, GameObject other) {
				if (other.getTag().contains("proj")) {
					Log.i("Physics2D", "Player got hit: " + strikeBase.hitpoints);
					strikeBase.hitpoints = MathUtils.max(1, strikeBase.hitpoints - 1);
					TextureSprite hBar = getGameObject("HealthBar").getComponent(GraphicsComponent.class).getSprite();
					float hp = 100 * (float) strikeBase.hitpoints / strikeBase.maxHitpoints;

					if (100 >= hp && hp > 75)
						hBar.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 0));
					if (75 >= hp && hp > 50)
						hBar.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 1));
					if (50 >= hp && hp > 25)
						hBar.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 2));
					if (25 >= hp && hp > 0)
						hBar.setRegion(Assets.SPRITE_ATLAS.getRegion("healthbar", 3));

					other.destroy();
				}
			}
		});

		addInputProcessor(new InputProcessor() {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				tmp.set(x, glGraphics.getHeight() - y);

				if (tmp.x < glGraphics.getWidth() / 4f) {
					if (tmp.y > glGraphics.getHeight() / 2f)
						camera.zoom -= 0.025f;
					else
						camera.zoom += 0.025f;
					return true;
				}
				return false;
			}

			@Override
			public boolean touchDragged(float x, float y, int pointer) {
				if (tmp.x < glGraphics.getWidth() / 4f)
					return true;

				return false;
			}
		});

		addInputProcessor(new InputProcessor() {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				moveOrder(x, y);
				return true;
			}

			@Override
			public boolean touchDragged(float x, float y, int pointer) {
				moveOrder(x, y);
				return true;
			}

			private void moveOrder(float x, float y) {
				targetPos.set(x, y);
				camera.unproject(targetPos);
				strikeBase.getComponent(VehicleFollowBehavior.class).setTarget(targetPos);
				moveIcon.getComponent(PhysicsComponent.class).setPosition(targetPos);
			}
		});
	}

	WindowedMean fpsMean = new WindowedMean(5);
	float second = 0;

	@Override
	public void update(float delta) {
		if (gamePaused)
			delta = 0f;
		super.update(delta);

		// FPS Counter
		fpsMean.addValue(delta);
		second += delta;
		if (second > 5) {
			second -= 5;
			Log.i("FPS", "" + 1 / fpsMean.getMean());
		}

		secondsElapsed += delta;

		// Step physics simulation
		physics2D.update(delta);

		// Update GameObjects
		for (GameObject go : this.getGameObjects())
			go.update(delta);

		// Move camera to strikebase
		camera.position.set(strikeBase.getPosition());
		//camera.position.add(strikeBase.getComponent(PhysicsComponent.class).getVelocity().scl(0.75f));
		camera.update();
	}

	@Override
	public void present(float delta) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(Assets.SPRITE_ATLAS.getTexture());

		gameMap.draw(batch, strikeBase.getComponent(PhysicsComponent.class).getPosition());

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
