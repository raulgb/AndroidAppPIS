package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;

public class TurretItem extends Item {
	private String id;

	TurretConfig cfg = null;

	// Builder
	public TurretItem(String id, String name, String image, String model, String flavour, int price) {
		super(name, image, model, flavour, price);
		this.id = id;
	}

	// Returns a new TurretObject whose parameters are contained on a given string.
	public static TurretItem parseTurretItem(String seq) {
		int price;
		String id;
		TurretConfig cfg;
		TurretItem item;

		try {
			// string must contain id, name, image, model, flavour, price
			String param[] = seq.split(";"); // ; used as separator
			price = Integer.parseInt(param[5]);

			id = param[0];
			if (id.equals("machinegun")) {
				cfg = new TurretConfig(TurretConfig.Type.TURRET_MACHINEGUN);
			} else if (id.contains("gatling")) {
				cfg = new TurretConfig(TurretConfig.Type.TURRET_GATLING);
			} else if (id.equals("battle_cannon")) {
				cfg = new TurretConfig(TurretConfig.Type.TURRET_CANNON);
			} else if (id.equals("howitzer")) {
				cfg = new TurretConfig(TurretConfig.Type.TURRET_HOWITZER);
			} else {
				return null;
			}

			item = new TurretItem(id, param[1], param[2], param[3], param[4], price);
			item.cfg = cfg;
		} catch (Exception ex) {
			return null;
		}
		return item;
	}

	// Returns a string containing all relevant information of the object, using ";" as separator.
	@Override
	public String toString() {
		return (this.id + ";" +this.name + ";" + this.image + ";" + this.model + ";" + this.flavour + Integer.toString(price));
	}

	@Override
	public String getDisplayText() {
		int attack, firerate, range;

		// TODO adjust these formulas for better reflecting the turret stats
		attack = cfg.proj_damage;
		firerate = Math.round( 10f / cfg.firerate );
		range = Math.round(cfg.range);

		return (name + "\n\n\tattack: " + Integer.toString(attack) + "\n\tfirerate: " + Integer.toString(firerate) + "\n\trange: " + Integer
				.toString(range) + "\n\n\tprice: " + Integer.toString(price));
	}

	public TurretConfig getConfig() {
		return cfg;
	}

	public GraphicsComponent getGraphics() {
		return new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion(this.image));
	}

}