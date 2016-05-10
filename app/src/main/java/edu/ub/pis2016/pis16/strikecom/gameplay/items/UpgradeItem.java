package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import edu.ub.pis2016.pis16.strikecom.engine.game.Component;

public class UpgradeItem extends Item{

	public String function;

	public UpgradeItem(String name, String image, String model, String flavour, String function, float price) {
		super(name, image, model, flavour, price);
	}

	public static UpgradeItem parseUpgradeItem(String seq){
		String param[] = seq.split(";"); // ; used as separator
		if(param.length < 6){ //should at least contain name, image, model, flavour, function, price
			return null;
		}
		return new UpgradeItem(param[0], param[1], param[2], param[3], param[4], Float.valueOf(param[4]));
	}

	@Override
	public String getDisplay() {
		return this.flavour;
	}

	public Component getUpgrade(){
		return null;
	}
}