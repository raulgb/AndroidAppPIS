package edu.ub.pis2016.pis16.strikecom.gameplay.factories;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.Explosion;
import edu.ub.pis2016.pis16.strikecom.gameplay.ThreadVehicle;
import edu.ub.pis2016.pis16.strikecom.gameplay.Turret;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.EnemyConfig;

import static edu.ub.pis2016.pis16.strikecom.engine.framework.Screen.LAYER_VEHICLES;
import static edu.ub.pis2016.pis16.strikecom.engine.framework.Screen.LAYER_VEHICLE_TURRET;
import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILE_SIZE;

public class EnemyFactory {

	private static final Vector2 tmp = new Vector2();

	/**
	 * Creates a new random enemy and adds it to the given Screen.
	 *
	 * @param screen The Screen
	 * @return The Enemy GameObject
	 */
	static public GameObject createRandomEnemyTank(final Screen screen) {
		GameObject strikeBase = screen.getGameObject("StrikeBase");
		if (strikeBase == null || !strikeBase.isValid())
			return null;

		// RANDOM enemy config
		EnemyConfig enemyConfig;
		float rand = MathUtils.random();
		if (rand > .95f)
			enemyConfig = new EnemyConfig(EnemyConfig.Model.large);
		else if (rand > .75f)
			enemyConfig = new EnemyConfig(EnemyConfig.Model.medium_2);
		else if (rand > .55f)
			enemyConfig = new EnemyConfig(EnemyConfig.Model.medium_1);
		else if (rand > .25f)
			enemyConfig = new EnemyConfig(EnemyConfig.Model.small_2);
		else
			enemyConfig = new EnemyConfig(EnemyConfig.Model.small_1);

		// ---- TANK
		final ThreadVehicle tank = new ThreadVehicle(enemyConfig);
		tank.killable = true;
		tank.cfg.accel = .75f * TILE_SIZE;

		tank.getPhysics().body.filter = Physics2D.Filter.ENEMY;
		tank.setTag("enemy_tank");
		tank.setLayer(LAYER_VEHICLES);

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
				Assets.sfx_expl_heavy.play(1f);

				// Add Scrap to Player
				FragmentedGameActivity gameActivity =
						(FragmentedGameActivity) ((GLGameFragment) screen.getGame()).getActivity();

				gameActivity.playerState.addScrap(tank.maxHitpoints);
				gameActivity.playerState.increaseCounter();
			}
		});

		// ---- TURRET
		Turret turret = new Turret(tank.cfg.turretType, tank);

		turret.setLayer(LAYER_VEHICLE_TURRET);
		turret.getSprite().setSize(1f * TILE_SIZE);
		TurretBehavior tb = new TurretBehavior();
		turret.putComponent(tb);
		screen.addGameObject(turret);

		tank.faction = GameObject.Faction.RAIDERS;
		turret.faction = GameObject.Faction.RAIDERS;

		return tank;
	}

}



