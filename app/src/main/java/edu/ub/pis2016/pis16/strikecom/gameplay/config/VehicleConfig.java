package edu.ub.pis2016.pis16.strikecom.gameplay.config;

/**
 * Basic Tank Config
 * Created by Herman on 14/05/2016.
 */
public class VehicleConfig {
	// Velocity Config
	/** Tiles/second */
	public float maxSpeed = 1f * GameConfig.TILE_SIZE;
	/** Accel in tiles/s^2 */
	public float accel = 2f * GameConfig.TILE_SIZE;
	public float maxReverseSpeed = -maxSpeed / 2f;
}
