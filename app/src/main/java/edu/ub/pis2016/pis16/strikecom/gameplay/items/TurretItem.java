package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;

public class TurretItem extends Item {
	private String model;
	public TurretConfig cfg = null;

	// Builder
	public TurretItem(TurretConfig cfg, String name, String flavour) {
		super(name, cfg.image, flavour, cfg.price);
		this.model = cfg.sprite;
		this.cfg = cfg;
	}

	// Returns a new TurretObject whose parameters are contained on a given string.
	public static TurretItem parseTurretItem(String seq) {
		try {
			String param[] = seq.split(";"); // ; used as separator

			TurretConfig cfg = new TurretConfig( TurretConfig.Type.valueOf(param[0]) );
			return new TurretItem(cfg, param[1], param[2]);
		} catch (Exception ex) {
			return null;
		}
	}

	public String getModel(){ return this.model; }

	// Returns a string containing all relevant information of the object, using ";" as separator.
	@Override
	public String toString() {
		return (cfg.toString() + ";" +this.name + ";" + this.flavour);
	}

	@Override
	public String getDisplayText() {
		int attack, firerate, range;

		// TODO adjust these formulas for better reflecting the turret stats
		attack = cfg.proj_damage;
		firerate = Math.round( 10f / cfg.firerate );
		range = Math.round(cfg.range);

		if (context == null) {
			return (name + "\n\n\tattack: " + Integer.toString(attack) + "\n\tfirerate: " + Integer.toString(firerate) + "\n\trange: " +
					Integer
					.toString(range) + "\n\tprice: " + Integer.toString(price));
		} else {
			return (name + "\n\n\t" + context.getString(R.string.stat_attack) + Integer.toString(attack) + "\n\t" + context.getString(R
					.string.stat_rate) + Integer.toString(firerate) + "\n\t" + context.getString(R.string.stat_range) + Integer.toString
					(range) + "\n\t" + context.getString(R.string.stat_price) + Integer.toString(price));
		}
	}

	public TurretConfig getConfig() {
		return cfg;
	}

	public boolean isPlasmaCannon(){
		return cfg.typeName.equals("plasma");
	}
}