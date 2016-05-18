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
	public float accel = 2f * GameConfig.TILE_SIZE;
	public float max_reverse_speed = -max_speed / 2f;

	// FUEL Consumption
	/** Fuel used per second moving */
	public float fuel_usage = 1f;
	public float fuel_usage_mult = 1;

	public int turret_num;
	/** Turret position offsets */
	public float[][] turret_offset;
	/** Angle limits, clockwise */
	public float[][] turret_angle_lim;
	/** Damage multiplicator per turret */
	public float[] turret_dmg_mult;

	public int animThreadFrames;
	public int animHullFrames;

	public StrikeBaseConfig(Model model) {
		animThreadFrames = 4;
		animHullFrames = 3;
		this.model = model;

		switch (model) {
			case MKI:
				// StrikeBase Mark. I
				modelName = "sbmk1";
				turret_num = 4;

				turret_dmg_mult = new float[]{1, 1, 1, 1};

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-16, 16};
				turret_offset[1] = new float[]{16, 16};
				turret_offset[2] = new float[]{-16, -16};
				turret_offset[3] = new float[]{16, -16};

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

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-16, 16};
				turret_offset[1] = new float[]{-16, -16};
				turret_offset[2] = new float[]{16, -16};

				turret_angle_lim = new float[turret_num][2];
				turret_angle_lim[0] = new float[]{0, 270};
				turret_angle_lim[1] = new float[]{90, 0};
				turret_angle_lim[2] = new float[]{0, 360};
				break;
		}
	}
}
