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
	public float maxSpeed = 1f * GameConfig.TILE_SIZE;
	/** Accel in tiles/s^2 */
	public float accel = 2f * GameConfig.TILE_SIZE;
	public float maxReverseSpeed = -maxSpeed / 2f;

	// FUEL Consumption
	/** Fuel used per second moving */
	public float fuelUsage = 1f;
	public float fuelUsageMultiplier = 1;

	public int turretNum;
	/** Turret position offsets */
	public float[][] turretOffsets;
	/** Angle limits, clockwise */
	public float[][] turretAngleLimit;
	/** Damage multiplicator per turret */
	public float[] turretDmg = new float[]{0, 0, 0, 0};
	/** Damage multiplicator per turret */
	public float[] turretDmgMult;

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
				turretNum = 4;

				turretDmgMult = new float[]{1, 1, 1, 1};

				turretOffsets = new float[turretNum][2];
				turretOffsets[0] = new float[]{-8, 8};
				turretOffsets[1] = new float[]{8, 8};
				turretOffsets[2] = new float[]{-8, -8};
				turretOffsets[3] = new float[]{8, -8};

				turretAngleLimit = new float[turretNum][2];
				turretAngleLimit[0] = new float[]{0, 270};
				turretAngleLimit[1] = new float[]{270, 180};
				turretAngleLimit[2] = new float[]{90, 270};
				turretAngleLimit[3] = new float[]{180, 90};
				break;

			case MKII:
				// StrikeBase Mark. II
				modelName = "sbmk2";
				turretNum = 3;

				turretDmgMult = new float[]{1.15f, 1.15f, 1.7f};

				turretOffsets = new float[turretNum][2];
				turretOffsets[0] = new float[]{-8, 8};
				turretOffsets[1] = new float[]{-8, -8};
				turretOffsets[2] = new float[]{8, -8};

				turretAngleLimit = new float[turretNum][2];
				turretAngleLimit[0] = new float[]{0, 270};
				turretAngleLimit[1] = new float[]{90, 0};
				turretAngleLimit[2] = new float[]{0, 360};
				break;
		}
	}
}
