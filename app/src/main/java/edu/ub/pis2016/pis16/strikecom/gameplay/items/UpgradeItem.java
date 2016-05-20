package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import edu.ub.pis2016.pis16.strikecom.engine.game.Component;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

public class UpgradeItem extends Item {

	public enum AVAILABLE_FUNCTIONS {
		FUEL,
		ARMOUR_PLATE,
		ARMOUR_COMPOSITE,
		AI,
		ENGINE_EFFICIENCY

	}

	private String function;

	public UpgradeItem(String name, String image, String model, String flavour, String function, int price) {
		super(name, image, model, flavour, price);
		this.function = function;
	}

	public String getFunction() {
		return this.function;
	}

	public boolean isFuel() {
		return function.equals("FUEL");
	}

	public static UpgradeItem parseUpgradeItem(String seq) {
		String param[] = seq.split(";"); // ; used as separator
		if (param.length < 6) { //should at least contain name, image, model, flavour, function, price
			return null;
		}
		return new UpgradeItem(param[0], param[1], param[2], param[3], param[4], Integer.valueOf(param[5]));
	}

	public static boolean functionIsSupported(String f) {
		for (AVAILABLE_FUNCTIONS FUNCTION : AVAILABLE_FUNCTIONS.values()) {
			if (FUNCTION.name().equals(f)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDisplay() {
		return (this.flavour + "\n\nprice: " + Integer.toString(this.price));
	}

	// Returns a string containing all relevant information of the object, using ";" as separator.
	@Override
	public String toString() {
		return (this.name + ";" + this.image + ";" + this.model + ";" + this.flavour + ";" + this.function + ";" + Integer.toString(this
				.price));
	}

	// Requires model name of the strikebase configuration
	public GraphicsComponent getGraphics(String modelName) {
		return new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion(this.model + "_" + modelName));
	}
}