package edu.ub.pis2016.pis16.strikecom.gameplay.items;

public class UpgradeItem extends Item{

	public String function;

	public UpgradeItem(String name, String image, String flavour, String function, float price) {
		super(name, image, flavour, price);
	}

	public static UpgradeItem parseUpgradeItem(String seq){
		String param[] = seq.split(";"); // ; used as separator
		if(param.length < 5){ //should at least contain name, image, flavour, function, price
			return null;
		}
		return new UpgradeItem(param[0], param[1], param[2], param[3], Float.valueOf(param[4]));
	}

	@Override
	public String getDisplay() {
		return this.flavour;
	}
}