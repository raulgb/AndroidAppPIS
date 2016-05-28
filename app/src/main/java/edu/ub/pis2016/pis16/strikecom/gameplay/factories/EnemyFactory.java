package edu.ub.pis2016.pis16.strikecom.gameplay.factories;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.gameplay.Explosion;
import edu.ub.pis2016.pis16.strikecom.gameplay.ThreadVehicle;
import edu.ub.pis2016.pis16.strikecom.gameplay.Turret;
import edu.ub.pis2016.pis16.strikecom.gameplay.Vehicle;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.*;

public class EnemyFactory {

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
		enemyTank.getComponent(VehicleFollowBehavior.class).setMinRange(3 * TILE_SIZE);

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

}
