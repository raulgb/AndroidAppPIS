package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import java.util.HashMap;

public abstract class Item {

	public class Value{
		int intVal;
		float floatVal;
		String stringVal;
	}

	HashMap<String, Value> properties;

	public Value getProperty(String string){
		return properties.get(string);
	}
}
