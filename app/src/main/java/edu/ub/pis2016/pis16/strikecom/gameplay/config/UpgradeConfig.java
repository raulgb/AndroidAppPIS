package edu.ub.pis2016.pis16.strikecom.gameplay.config;

/**
 * Created by sdp on 29/05/16.
 */
public class UpgradeConfig {

	public enum Function {
		FUEL,
		ARMOUR_PLATE,
		ARMOUR_COMPOSITE,
		ARMOUR_REACTIVE,
		AI,
		ENGINE_EFFICIENCY,
		SCAVENGER
	}

	public Function function = null;
	public String functionName;
	public String image;
	public float value;
	public int price;

	public UpgradeConfig(Function function) {
		this.function = function;

		switch(function) {
			case FUEL:
				functionName = "fuel";
				image = "fuel_64";
				value = 250f;
				price = 250;
				break;

			case ARMOUR_PLATE:
				functionName = "armour_plate";
				image = "plate_64";
				value = 100;
				price = 1500;
				break;

			case ARMOUR_COMPOSITE:
				functionName = "armour_composite";
				image = "composite_64";
				value = 250;
				price = 2500;
				break;

			case ARMOUR_REACTIVE:
				functionName = "armour_reactive";
				image = "reactive_64";
				value = 3;
				price = 2000;
				break;

			case AI:
				functionName = "ai";
				image = "ai_64";
				value = 0.5f;
				price = 2500;
				break;

			case ENGINE_EFFICIENCY:
				functionName = "engine";
				image = "engine_64";
				value = 0.75f;
				price = 2000;
				break;

			case SCAVENGER:
				functionName = "scavenger";
				image = "scavenger_64";
				value = 1.2f;
				price = 1000;
		}
	}
}
