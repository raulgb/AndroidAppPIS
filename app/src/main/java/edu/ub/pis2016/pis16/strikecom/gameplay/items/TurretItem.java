package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.CustomTurretBehavior;

public class TurretItem extends Item {
	float[] stats;

	// Builder
	public TurretItem(String name, String image, String flavour, float price, float[] stats) {
		super(name, image, flavour, price);
		this.stats = stats;
	}

	// Returns a new TurretObject whose parameters are contained on a given string.
	public static TurretItem parseTurretItem(String seq){
		String param[] = seq.split(";"); // ; used as separator

		if(param.length < 5){ // seq should at least contain name, image, flavour, price and 1 stat.
			return null;
		}
		float p = Float.valueOf(param[3]); //price
		float s[] = new float[param.length - 4]; //stats
		for(int i=0; i<s.length; i++){
			s[i] = Float.valueOf(param[i+4]);
		}
		return new TurretItem(param[0], param[1], param[2], p, s);
	}

	// Returns a string containing all relevant information of the object, using ";" as separator.
	@Override
	public String toString(){
		String seq = (this.name + ";" + this.image + ";" + this.flavour);
		for(float s : this.stats){
			seq += (";" + Float.toString(s));
		}
		return seq;
	}

	@Override
	public String getDisplay() {
		return  ("attack: " + Float.toString(stats[0]) + "\nspeed: " + Float.toString(stats[0]) + "\nHP: " + Float.toString(stats[0]) +
				"\n\n" + flavour);
	}

	public BehaviorComponent getTurretBehavior() {
		return new CustomTurretBehavior(stats[2], 0.15f);
	}
}