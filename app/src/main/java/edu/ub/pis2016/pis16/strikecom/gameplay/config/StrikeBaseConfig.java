package edu.ub.pis2016.pis16.strikecom.gameplay.config;

public class StrikeBaseConfig {

	public enum Model {
		MKI,
		MKII,
		MKIII,
		MKIV,
		MKV
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

	public float maneuverability = 0.6f;

	// SPRITE config
	/** Size in tiles */
	public float size_tiles = 2f;
	/** Portion of the total sprite size to use for the hitbox. First is X axis second is Y axis */
	public float[] hitbox_factor = {1, 1};
	/** Where in the sprite are the threads. 1 means the outermost edge, 0 means in the center. [0-1]*/
	public float thread_offsetY = 1f;
	/** Ammount to move the thread origin forwards (positive) or backwards (negative) relative to the width. Range [-1,1] */
	public float thread_offsetX = 0;

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

				maneuverability = 0.5f;

				turret_dmg_mult = new float[]{1, 1, 1, 1};

				size_tiles = 2.5f;

				thread_offsetY = 0.825f;
				thread_offsetX = 0f;

				hitbox_factor[0] = 0.95f;
				hitbox_factor[1] = 0.85f;

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

				maneuverability = 0.6f;

				turret_dmg_mult = new float[]{1.15f, 1.15f, 1.7f};

				thread_offsetY = 0.9f;
				thread_offsetX = -0.1f;

				hitbox_factor[0] = 1.05f;
				hitbox_factor[1] = 0.8f;

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

				maneuverability = 0.75f;

				turret_dmg_mult = new float[]{1.2f, 1.1f};

				thread_offsetX = 0f;
				thread_offsetY = 0.7f;

				hitbox_factor[0] = 0.85f;
				hitbox_factor[1] = 0.7f;

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

				maneuverability = 0.4f;

				size_tiles = 3;
				turret_dmg_mult = new float[]{1.4f, 0.95f, 0.95f, 1, 1};

				thread_offsetX = 0.06f;
				thread_offsetY = 0.365f;

				hitbox_factor[0] = 0.9f;
				hitbox_factor[1] = 0.6f;

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

			case MKV:
				// StrikeBase Mark. V
				modelName = "sbmk5";
				turret_num = 6;

				maneuverability = 0.25f;

				size_tiles = 4;
				turret_dmg_mult = new float[]{1, 1, 0.8f, 1.2f, 1, 1};

				thread_offsetX = 0.01563f;
				thread_offsetY = 0.578f;

				hitbox_factor[0] = 0.9f;
				hitbox_factor[1] = 0.6f;

				turret_offset = new float[turret_num][2];
				turret_offset[0] = new float[]{-.2969f, .5469f};    // Rear Left
				turret_offset[1] = new float[]{.2969f, .5469f};    // Front left
				turret_offset[2] = new float[]{.8438f, .1563f};     // Hull-mounted
				turret_offset[3] = new float[]{-.5625f, -.1406f};    // Main
				turret_offset[4] = new float[]{-.2969f, -.5469f};    // Rear Right
				turret_offset[5] = new float[]{.2969f, -.5469f};    // Front Right
				turret_angle_lim = new float[turret_num][2];
				turret_angle_lim[0] = new float[]{0, 270};
				turret_angle_lim[1] = new float[]{90, 0};
				turret_angle_lim[2] = new float[]{0, 270};
				turret_angle_lim[3] = new float[]{90, 0};
				turret_angle_lim[4] = new float[]{0, 270};
				turret_angle_lim[5] = new float[]{0, 270};
				break;

		}
	}
}
