package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.UpgradeConfig;

public class UpgradeItem extends Item {

	public UpgradeConfig cfg = null;

	public UpgradeItem(UpgradeConfig cfg, String name, String flavour) {
		super(name, cfg.image, flavour, cfg.price);

		this.cfg = cfg;
	}

	public boolean isFuel() {
		return cfg.functionName.equals("fuel");
	}

	public static UpgradeItem parseUpgradeItem(String seq) {
		try {
			String param[] = seq.split(";"); // ; used as separator

			UpgradeConfig cfg = new UpgradeConfig( UpgradeConfig.Function.valueOf(param[0]) );
			return new UpgradeItem(cfg, param[1], param[2]);

		} catch (Exception ex){
			return null;
		}
	}

	@Override
	public String getDisplayText() {
		if (context == null) {
			return (this.flavour + "\n\nprice: " + Integer.toString(this.price));
		} else {
			return (this.flavour + "\n\n" + context.getString(R.string.stat_price) + Integer.toString(this.price));
		}

	}

	// Returns a string containing all relevant information of the object, using ";" as separator.
	@Override
	public String toString() {
		return (cfg.toString() + ";" + name + ";" + flavour);
	}

}