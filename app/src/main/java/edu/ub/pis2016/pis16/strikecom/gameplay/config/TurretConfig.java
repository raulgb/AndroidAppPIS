package edu.ub.pis2016.pis16.strikecom.gameplay.config;

import edu.ub.pis2016.pis16.strikecom.gameplay.factories.ProjectileFactory;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.*;

/**
 * Parameters of a turret GameObject.
 */
public class TurretConfig {

	public enum Type {
		TURRET_MACHINEGUN,
		TURRET_GATLING,
		TURRET_CANNON,
		TURRET_HOWITZER
	}

	/** Idle seconds after target not found */
	public float idle_seconds = 1f;
	/** Seconds between each shot */
	public float firerate = 0.25f;
	/** Moving rotational speed of the turret */
	public float lerp_speed = 0.050f;
	/** Range to aim and shoot at targets */
	public float range = 6f * TILE_SIZE;
	/** Degrees within the turret will shoot */
	public float fire_cone = 5f;
	/** Max degree of spread */
	public float fire_spread = 1f;

	// Bullet properties
	/** Projectile to spawn on shoot */
	public ProjectileFactory.Type proj_type = ProjectileFactory.Type.PROJ_BULLET;
	/** Bullet Damage */
	public int proj_damage = 1;
	/** Bullet Speed in tiles/second */
	public float proj_speed = 2f * TILE_SIZE;

	static public final TurretConfig DEFAULT = new TurretConfig(Type.TURRET_MACHINEGUN);

	public TurretConfig(Type turretType) {
		// TODO Check balance
		switch (turretType) {
			case TURRET_MACHINEGUN:
				lerp_speed = 0.050f;
				firerate = 0.4f;
				fire_cone = 5f;
				fire_spread = 2f;
				range = 7f * TILE_SIZE;


				proj_type = ProjectileFactory.Type.PROJ_BULLET;
				proj_damage = 3;
				proj_speed = 0.4f * TILE_SIZE;
				break;

			case TURRET_GATLING:
				lerp_speed = 0.045f;
				firerate = 0.15f;
				fire_cone = 10f;
				fire_spread = 6.5f;
				range = 6.5f * TILE_SIZE;

				proj_type = ProjectileFactory.Type.PROJ_GATTLING_BULLET;
				proj_damage = 1;
				proj_speed = 0.5f * TILE_SIZE;
				break;

			case TURRET_CANNON:
				lerp_speed = 0.020f;
				firerate = 2f;
				fire_cone = 10f;
				fire_spread = 2.5f;
				range = 8f * TILE_SIZE;

				proj_type = ProjectileFactory.Type.PROJ_TANKSHELL;
				proj_damage = 10;
				proj_speed = 0.15f * TILE_SIZE;
				break;

			case TURRET_HOWITZER:
				lerp_speed = 0.005f;
				firerate = 5f;
				fire_cone = 5f;
				fire_spread = 5f;
				range = 12f * TILE_SIZE;

				proj_type = ProjectileFactory.Type.PROJ_HE_ROUND;
				proj_damage = 25;
				proj_speed = 0.10f * TILE_SIZE;
				break;
		}
	}
}
