package edu.ub.pis2016.pis16.strikecom.gameplay.factories;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.gameplay.Explosion;
import edu.ub.pis2016.pis16.strikecom.gameplay.ThreadVehicle;
import edu.ub.pis2016.pis16.strikecom.gameplay.Turret;
import edu.ub.pis2016.pis16.strikecom.gameplay.Vehicle;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILE_SIZE;
import static edu.ub.pis2016.pis16.strikecom.engine.framework.Screen.*;

public class EnemyFactory {

	private static final Vector2 tmp = new Vector2();

	static public GameObject createRandomEnemyTank(final Screen screen) {
		GameObject strikeBase = screen.getGameObject("StrikeBase");
		if (strikeBase == null || !strikeBase.isValid())
			return null;

		// ---- TANK
		final ThreadVehicle tank = new ThreadVehicle();
		tank.killable = true;
		tank.hitpoints = 25;
		tank.maxHitpoints = 25;

		tank.cfg.accel = .75f * TILE_SIZE;

		tank.getPhysics().body.filter = Physics2D.Filter.ENEMY;
		tmp.set(8 * TILE_SIZE, 0).rotate(MathUtils.random(360));
		tank.getPhysics().setPosition(strikeBase.getPosition().add(tmp));

		tank.setTag("enemy_tank");
		tank.setLayer(LAYER_VEHICLES);
		tank.getSprite().setSize(1.2f * TILE_SIZE);

		VehicleFollowBehavior vfb = new VehicleFollowBehavior();
		vfb.setTarget(strikeBase);
		vfb.setMinRange(5 * TILE_SIZE);
		vfb.setMaxRange(16 * TILE_SIZE);
		tank.putComponent(vfb);
		screen.addGameObject(tank);

		tank.addOnDestroyAction(new Runnable() {
			@Override
			public void run() {
				Explosion explosion = new Explosion("explosion_tank");
				explosion.setPosition(tank.getPosition());
				screen.addGameObject(explosion);

				// Add Scrap to Player
				FragmentedGameActivity gameActivity =
						(FragmentedGameActivity) ((GLGameFragment) screen.getGame()).getActivity();

				gameActivity.playerState.addScrap(tank.maxHitpoints);
			}
		});

		// ---- RANDOM TURRET
		Turret turret;
		TurretConfig turretConfig;
		float rand = MathUtils.random();
		if (rand > .75f)
			turretConfig = new TurretConfig(TurretConfig.Type.TURRET_GATLING);
		else if (rand > .50f)
			turretConfig = new TurretConfig(TurretConfig.Type.TURRET_MACHINEGUN);
		else if (rand > .15f)
			turretConfig = new TurretConfig(TurretConfig.Type.TURRET_CANNON);
		else
			turretConfig = new TurretConfig(TurretConfig.Type.TURRET_HOWITZER);

		turret = new Turret(turretConfig, tank);

		turret.setLayer(LAYER_VEHICLE_TURRET);
		turret.getSprite().setSize(1.1f * TILE_SIZE);
		TurretBehavior tb = new TurretBehavior();
		tb.setTargetTag("player");
		turret.putComponent(tb);
		screen.addGameObject(turret);

		return tank;
	}

	/*
	static public Vehicle createEnemyTank() {
		// Create 2 new enemies on death
		final ThreadVehicle enemyTank = new ThreadVehicle();

		enemyTank.addOnDestroyAction(new Runnable() {
			@Override
			public void run() {
				Screen thisScreen = enemyTank.getScreen();
				Explosion explosion = new Explosion("explosion_tank");
				explosion.setPosition(enemyTank.getPosition());
				thisScreen.addGameObject(explosion);

				// Add Scrap to Player
				FragmentedGameActivity gameActivity =
						(FragmentedGameActivity) ((GLGameFragment) thisScreen.getGame()).getActivity();

				gameActivity.playerState.addScrap(enemyTank.maxHitpoints);
			}
		});

		enemyTank.hitpoints = 20;
		enemyTank.maxHitpoints = 20;
		enemyTank.killable = true;
		enemyTank.getPhysics().body.filter = Physics2D.Filter.ENEMY;
		enemyTank.setTag("enemy_tank");

		enemyTank.putComponent(new VehicleFollowBehavior());
		enemyTank.getComponent(VehicleFollowBehavior.class).setMaxRange(16 * TILE_SIZE);
		enemyTank.getComponent(VehicleFollowBehavior.class).setMinRange(4 * TILE_SIZE);

		enemyTank.putComponent(new BehaviorComponent() {

			private GameObject strikeBase;

			@Override
			public void init() {
				strikeBase = gameObject.getScreen().getGameObject("StrikeBase");
			}

			@Override
			public void update(float delta) {
				if (!strikeBase.isValid())
					return;
				VehicleFollowBehavior vfb = gameObject.getComponent(VehicleFollowBehavior.class);
				vfb.setTarget(strikeBase.getPosition());
			}
		});

		enemyTank.cfg.maxSpeed = 8f;
		enemyTank.cfg.accel = 4.5f;
		return enemyTank;
	}

	public static GameObject createEnemyTankTurret(Vehicle parentTank) {
		// ------ TANK TURRET -----
		Turret turret = new Turret("enemy_turret", parentTank, "turret");
		// Configure enemy damage and stuff here
		turret.cfg = new TurretConfig(TurretConfig.Type.TURRET_CANNON);
		turret.putComponent(new TurretBehavior());
		// Bigger cannon
		turret.getComponent(GraphicsComponent.class).getSprite().setSize(1.4f * GameConfig.TILE_SIZE);
		turret.getComponent(TurretBehavior.class).setTargetTag("player");
		turret.setLayer(Screen.LAYER_VEHICLE_TURRET);

		return turret;
	}
	*/

}
