package edu.ub.pis2016.pis16.strikecom.gameplay.config;

/**
 * Created by sdp on 28/05/16.
 */
public class EnemyConfig {

	public enum Model {
		small_1,
		small_2,
		medium_1,
		medium_2,
		large
	}
	
	public Model model;

	/** Tiles/second */
	public float maxSpeed = 1f * GameConfig.TILE_SIZE;
	/** Accel in tiles/s^2 */
	public float accel = 2f * GameConfig.TILE_SIZE;
	public float maxReverseSpeed = -maxSpeed / 2f;
	
	public String modelName;
	public String region = "enemy";
	public int index = -1;
	public int maxHitpoints = 25;
	public float size = GameConfig.TILE_SIZE;
	
	public TurretConfig turretType = null;
	
	public EnemyConfig(Model model) {
		
		switch (model) {
			case small_1:
				modelName = "small_1";
				region = "enemy_small";
				index = 0;
				turretType = new TurretConfig(TurretConfig.Type.TURRET_MACHINEGUN);
				size *= 0.9f;
				break;
			
			case small_2:
				modelName = "small_2";
				region = "enemy_small";
				index = 1;
				turretType = new TurretConfig(TurretConfig.Type.TURRET_GATLING);
				size *= 1.1f;
				break;
			
			case medium_1:
				modelName = "medium_1";
				region = "enemy_medium";
				index = 0;
				maxHitpoints = 50;
				turretType = new TurretConfig(TurretConfig.Type.TURRET_CANNON);
				size *= 1.4f;
				break;
			
			case medium_2:
				modelName = "medium_2";
				region = "enemy_medium";
				maxHitpoints = 50;
				index = 1;
				turretType = new TurretConfig(TurretConfig.Type.TURRET_HOWITZER);
				size *= 1.6f;
				break;
			
			case large:
				modelName = "large";
				region = "enemy_large";
				index = -1;
				maxHitpoints = 75;
				turretType = new TurretConfig(TurretConfig.Type.TURRET_PLASMA);
				size *= 1.8f;
		}
	}
}
