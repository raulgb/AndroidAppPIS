package edu.ub.pis2016.pis16.strikecom.gameplay.config;

public class GameConfig {

	/** Size of tiles in pixels */
	public static final int TILE_SIZE = 16;
	/** Size of map in tiles */
	public static final int MAP_SIZE = 32;
	/** Width of screen in tiles */
	public static final int TILES_ON_SCREEN = 10;

	/** Speed of bullets in tiles/second */
	public static final float BULLET_SPEED = 4f;


	// Groups
	// TODO Implement this in place of Tag-testing
	public static final int GROUP_NEUTRAL = 0x00;
	public static final int GROUP_PLAYER = 0x01;
	public static final int GROUP_RAIDERS = 0x02;
	public static final int GROUP_MECHANICS = 0x04;

}
