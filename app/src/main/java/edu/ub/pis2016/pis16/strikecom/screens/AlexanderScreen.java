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
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
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
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBaseTest;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.ProjectileBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;

public class AlexanderScreen extends Screen {
	GLGraphics glGraphics;
	SpriteBatch batch;
	float secondsElapsed;

	Physics2D physics2D;
	GameObject bodySprite;
	GameObject bodySprite2;
	Body testBody;
	Body testBody2;

	GameObject enemy;
	GameObject moveIcon;
	StrikeBaseTest strikeBase;

	TextureSprite grass;

	public Pool<GameObject> projectilePool;

	private Vector2 targetPos = new Vector2();
	private Vector2 tmp = new Vector2();

	private Vector2 camPos = new Vector2();
	public float camZoom = 6f;

	public AlexanderScreen(Game game) {
		super(game);
		Log.i("DUMMY_SCREEN", "Created");

		glGraphics = game.getGLGraphics();
		physics2D = new Physics2D(1024, 1024, Vector2.ZERO);

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
		addGameObject("StrikeBase", strikeBase);

		// Create an  Enemy GameObject
		enemy = new GameObject();
		enemy.setLayer(LAYER_1);
		enemy.setTag("enemy");
		enemy.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("enemy")));
		enemy.putComponent(new PhysicsComponent());
		enemy.getComponent(PhysicsComponent.class).setPosition(64, 0);
		enemy.getComponent(GraphicsComponent.class).getSprite().setScale(0.5f);
		addGameObject("Enemy", enemy);

		moveIcon = new GameObject();
		moveIcon.setLayer(LAYER_BACKGROUND);
		moveIcon.putComponent(new PhysicsComponent());
		moveIcon.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("cursor_move")));
		moveIcon.getComponent(GraphicsComponent.class).getSprite().setScale(0.3f);
		addGameObject("MoveIcon", moveIcon);

		grass = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("grass"));


		// Test Physics2d
		bodySprite = new GameObject();
		bodySprite.putComponent(new PhysicsComponent());
		bodySprite.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("default_enemy")));
		bodySprite.setLayer(LAYER_4);
		addGameObject(bodySprite);

		bodySprite2 = new GameObject();
		bodySprite2.putComponent(new PhysicsComponent());
		bodySprite2.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("default_enemy")));
		bodySprite2.setLayer(LAYER_4);
		addGameObject(bodySprite2);

		testBody = new DynamicBody(new Rectangle(32, 32));
		testBody2 = new DynamicBody(new Rectangle(32, 32));

		testBody.setPosition(0, 64);
		testBody2.setPosition(strikeBase.getComponent(PhysicsComponent.class).getPosition());//lets use if for strikeBase
		physics2D.addContactListener(new ContactListener() {
			@Override
			public void onCollision(CollisionEvent ce) {
				Vector2 tmp=testBody.getPosition();
				testBody.setPosition(tmp.add(64*(float)Math.random()-32,64*(float)Math.random()-32));// random position near old one
			}
		});

		physics2D.addDynamicBody(testBody);
		physics2D.addDynamicBody(testBody2);
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
			e.x = (e.x - glGraphics.getWidth() / 2) / (int) camZoom + (int) camPos.x;
			e.y = (e.y - glGraphics.getHeight() / 2) / (int) camZoom + (int) camPos.y;
			targetPos.set(e.x, e.y);

			strikeBase.getComponent(VehicleFollowBehavior.class).setTarget(targetPos);
			moveIcon.getComponent(PhysicsComponent.class).setPosition(targetPos);
		}
		//Todo curretnly one rectangle is bound to strikeBase, another one changes position randomly if collision is occured
		//also we need to create everything not near the coordinate origin but rather near Physics2d.worldWidth/2, Physics2d.worldHeight/2
		// because if our object gets negative coodrinates - SpatialHashGrid stops working
		testBody2.setPosition(strikeBase.getComponent(PhysicsComponent.class).getPosition());
		// Step physics simulation
		physics2D.update(delta);
		bodySprite.getComponent(PhysicsComponent.class).setPosition(testBody.getPosition());
		bodySprite2.getComponent(PhysicsComponent.class).setPosition(testBody2.getPosition());

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
		return density_id * camZoom / 3.0f;
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
