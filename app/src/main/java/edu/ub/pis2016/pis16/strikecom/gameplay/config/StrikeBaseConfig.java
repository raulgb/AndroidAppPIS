package edu.ub.pis2016.pis16.strikecom.gameplay.config;

public class StrikeBaseConfig {

	public enum Model {
		MKI,
		MKII
	}

	public Model model;

	public String modelName;

	/**
	 * TURRET ARRAY CONVENTIONS FOLLOW THIS ORDER FOR STRIKEBASES:
	 *
	 * 		1	2
	 *
	 * 	5	  7		6
	 *
	 * 		3	4
	 *
	 * 	DEPENDING ON TURRET ANCHOR POSITION ON THE SPRITE
	 * 	IF ANY TURRET IS MISSING, DON'T SKIP INDICES
	 * 	I.E. MODEL MKII IS:
	 *
	 * 		1
	 *
	 * 		2	3
	 */
	// Velocity Config
	/** Tiles/second */
	public float max_speed = 1.5f * GameConfig.TILE_SIZE;
	/** Accel in tiles/s^2 */
	public float accel = 2.5f * GameConfig.TILE_SIZE;
	public float max_reverse_speed = -max_speed / 2f;

	/** Ammount to move the thread origin forwards (positive) or backwards (negative) relative to the width. Range [-1,1] */
	public float thread_offset = 0;

	/** Percentage of the sprite to take into account for vehicle width */
	public float width_factor = 0.9f;

	// FUEL Consumption
	/** Fuel used per second moving */
	public float fuel_usage = 0.5f;
	public float fuel_usage_mult = 1f;

	public int turret_num;
	/** Turret position offsets. In relation to half-width. Range [0-1] */
	public float[][] turret_offset;
	/** Angle limits, clockwise. Range [0-360,0-360] */
	public float[][] turret_angle_lim;
	/** Damage factor per turret. Range [0,-] */
	public float[] turret_dmg_mult;

	public int anim_thread_frames;
	public int anim_hull_frames;

	public StrikeBaseConfig(Model model) {
		anim_thread_frames = 4;
		anim_hull_frames = 3;
		this.model = model;

		switch (model) {
			case MKI:
				// StrikeBase Mark. I
				modelName = "sbmk1";
				turret_num = 4;

				turret_dmg_mult = new float[]{1, 1, 1, 1};

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-1, 1};
				turret_offset[1] = new float[]{1, 1};
				turret_offset[2] = new float[]{-1, -1};
				turret_offset[3] = new float[]{1, -1};

				turret_angle_lim = new float[turret_num][2];
				turret_angle_lim[0] = new float[]{0, 270};
				turret_angle_lim[1] = new float[]{270, 180};
				turret_angle_lim[2] = new float[]{90, 270};
				turret_angle_lim[3] = new float[]{180, 90};
				break;

			case MKII:
				// StrikeBase Mark. II
				modelName = "sbmk2";
				turret_num = 3;

				turret_dmg_mult = new float[]{1.15f, 1.15f, 1.7f};

				thread_offset = -0.1f;

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-.5625f, .5f};    // Top Left
				turret_offset[1] = new float[]{-.5625f, -.5f};    // Bottom Left
				turret_offset[2] = new float[]{.5625f, -.5f};    // Bottom right

				turret_angle_lim = new float[turret_num][2];
				turret_angle_lim[0] = new float[]{0, 270};
				turret_angle_lim[1] = new float[]{90, 0};
				turret_angle_lim[2] = new float[]{0, 360};
				break;
		}
	}
}
