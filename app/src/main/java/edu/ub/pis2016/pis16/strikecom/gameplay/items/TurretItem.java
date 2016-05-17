package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;

public class TurretItem extends Item {
	float[] stats;

	// Builder
	public TurretItem(String name, String image, String model, String flavour, int price, float[] stats) {
		super(name, image, model, flavour, price);
		this.stats = stats;
	}

	// Returns a new TurretObject whose parameters are contained on a given string.
	public static TurretItem parseTurretItem(String seq) {
		String param[] = seq.split(";"); // ; used as separator

		if (param.length < 6) { // seq should at least contain name, image, flavour, price and 1 stat.
			return null;
		}
		int p = Integer.valueOf(param[4]); //price
		float s[] = new float[param.length - 5]; //stats
		for (int i = 0; i < s.length; i++) {
			s[i] = Float.valueOf(param[i + 5]);
		}
		return new TurretItem(param[0], param[1], param[2], param[3], p, s);
	}

	// Returns a string containing all relevant information of the object, using ";" as separator.
	@Override
	public String toString() {
		String seq = (this.name + ";" + this.image + ";" + this.model + ";" + this.flavour);
		for (float s : this.stats) {
			seq += (";" + Float.toString(s));
		}
		return seq;
	}

	@Override
	public String getDisplay() {
		return (name + "\n\nattack: " + Float.toString(stats[0]) + "\nspeed: " + Float.toString(stats[1]) + "\nHP: " + Float.toString
				(stats[2]) +
				"\n\n" + flavour);
	}

	public TurretConfig getConfig() {
		TurretConfig cfg = null;
		if (name.equals("Machinegun")) {
			cfg = new TurretConfig(TurretConfig.Type.TURRET_MACHINEGUN);
		} else if (name.equals("Gatling gun")) {
			cfg = new TurretConfig(TurretConfig.Type.TURRET_GATLING);
		} else if (name.equals("Battle cannon")) {
			cfg = new TurretConfig(TurretConfig.Type.TURRET_CANNON);
		} else if (name.equals("Howitzer")) {
			cfg = new TurretConfig(TurretConfig.Type.TURRET_HOWITZER);
		}
		if (cfg == null)
			throw new IllegalArgumentException("No turret confing with name " + this.name);

//		cfg.proj_damage = Math.round(stats[0]);
//		cfg.firerate = 2 / stats[1];
//		cfg.lerp_speed = 1 / stats[1];
		return cfg;
	}

	public GraphicsComponent getGraphics() {
		return new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion(this.image));
	}

	public int getHitPoints() {
		return Math.round(stats[2]);
	}
}