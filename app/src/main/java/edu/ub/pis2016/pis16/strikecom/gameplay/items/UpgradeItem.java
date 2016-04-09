package edu.ub.pis2016.pis16.strikecom.gameplay.items;

// Not yet implemented.
public class UpgradeItem extends Item{

	public UpgradeItem(String name, String image, String flavour, float price) {
		super(name, image, flavour, price);
	}

	public static UpgradeItem parseUpgradeItem(String seq){
		String param[] = seq.split(";"); // ; used as separator
		if(param.length < 4){ //should at least contain name, image, flavour and price
			return null;
		}
		return new UpgradeItem(param[0], param[1], param[2], Float.valueOf(param[3]));
	}
}