package edu.ub.pis2016.pis16.strikecom.gameplay.config;

public class StrikeBaseConfig {

	public enum Model {
		MKI,
		MKII,
		MKIII,
		MKIV
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
	public float max_speed = 30f;
	/** Accel in tiles/s^2 */
	public float accel = 25f;
	public float max_reverse_speed = -max_speed / 2f;

	// SPRITE config
	/** Size in tiles */
	public float size_tiles = 2f;
	/** Percentage of the sprite to take into account for vehicle width, [0-1] */
	public float width_factor = 1f;
	/** Ammount to move the thread origin forwards (positive) or backwards (negative) relative to the width. Range [-1,1] */
	public float thread_offset = 0;

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

				size_tiles = 2.5f;
				width_factor = 0.825f;
				thread_offset = 0f;

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-.1093f, .68f};    // left
				turret_offset[1] = new float[]{-.1093f, -.68f};    // right
				turret_offset[2] = new float[]{-.4685f, 0f};    // main
				turret_offset[3] = new float[]{.625f, 0.3125f};    // hull-mounted

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

				width_factor = 0.9f;
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

			case MKIII:
				// StrikeBase Mark. III
				modelName = "sbmk3";
				turret_num = 2;

				turret_dmg_mult = new float[]{1.2f, 1.1f};

				width_factor = 0.7f;
				thread_offset = 0f;

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-.375f, .1562f};    // Top
				turret_offset[1] = new float[]{.5278f, -.2812f};    // Front
				turret_angle_lim = new float[turret_num][2];
				turret_angle_lim[0] = new float[]{0, 270};
				turret_angle_lim[1] = new float[]{90, 0};
				break;

			case MKIV:
				// StrikeBase Mark. IV
				modelName = "sbmk4";
				turret_num = 5;

				turret_dmg_mult = new float[]{1.4f, 0.95f, 0.95f, 1, 1};

				width_factor = 0.365f;
				thread_offset = 0.06f;

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-.5208f, 0f};    // Main
				turret_offset[1] = new float[]{.0625f, .5208f};    // Left
				turret_offset[2] = new float[]{.7292f, .375f};    // Front left
				turret_offset[3] = new float[]{.0625f, -.5208f};    // Right
				turret_offset[4] = new float[]{.7292f, -.375f};    // Front right
				turret_angle_lim = new float[turret_num][2];
				turret_angle_lim[0] = new float[]{0, 270};
				turret_angle_lim[1] = new float[]{90, 0};
				turret_angle_lim[2] = new float[]{0, 270};
				turret_angle_lim[3] = new float[]{90, 0};
				turret_angle_lim[4] = new float[]{0, 270};
				break;

		}
	}
}
