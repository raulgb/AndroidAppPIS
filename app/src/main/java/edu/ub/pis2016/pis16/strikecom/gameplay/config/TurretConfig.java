package edu.ub.pis2016.pis16.strikecom.gameplay.config;

/**
 * Created by Arbitro on 15/05/2016.
 */
public class TurretConfig {

	/** Idle seconds after target not found */
	public float idle_seconds = 2f;
	/** Seconds between each shot */
	public float shoot_freq = 0.5f;
	/** Moving rotational speed of the turret */
	public float lerp_speed = 0.075f;
	/** Range to aim and shoot at targets */
	public float range = 6 * GameConfig.TILE_SIZE;

	// Bullet properties
	/** Bullet Damage */
	public int proj_damage = 1;
	/** Bullet Speed in tiles/second */
	public float proj_speed = 8;

	public TurretConfig() {
		// TODO constructor with an enum parameter
	}
}
