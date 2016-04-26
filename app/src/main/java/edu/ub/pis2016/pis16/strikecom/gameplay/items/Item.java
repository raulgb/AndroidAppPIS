package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import android.support.annotation.NonNull;

// Class represents in-game objects, containing all the information relevant to the inventory display.
public abstract class Item implements Comparable<Item> {
	String name;    // object name
	String image;   // name of representative image
	String flavour; //description, fluff
	float price;    // price in game resources

	// Builders
	public Item(String name, String image, String flavour, float price) {
		this.name = name;
		this.image = image;
		this.flavour = flavour;
		this.price = price;
	}

	public String getName(){ return this.name; }

	public String getImage(){ return this.image; }

	public String getFlavour(){ return this.flavour; }

	public Item() {
	}

	// Returns negative integer if object price is smaller than that of a given object, positive
	// integer if price is bigger and zero if both have equal price.
	@Override
	public int compareTo(@NonNull Item o) {
		return Math.round(this.price - o.price);
	}

	public abstract String getDisplay();
}
