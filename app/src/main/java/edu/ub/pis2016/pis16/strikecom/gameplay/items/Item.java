package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import android.support.annotation.NonNull;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

// Class represents in-game objects, containing all the information relevant to the inventory display.
public abstract class Item implements Comparable<Item> {
	protected String name;    // object name
	protected String image;    // item icon
	protected String model;   // name of representative image on the screen
	protected String flavour; //description, fluff
	protected float price;    // price in game resources

	protected boolean visible = true;

	// Builders
	public Item(String name, String image, String model, String flavour, float price) {
		this.name = name;
		this.image = image;
		this.model = model;
		this.flavour = flavour;
		this.price = price;
	}

	public void setVisibility(boolean visible) { this.visible = visible; }

	public boolean isVisible() { return this.visible; }

	public String getName(){ return this.name; }

	public String getImage(){ return this.image; }

	public String getModel(){ return this.model; }

	public String getFlavour(){ return this.flavour; }

	public float getPrice(){ return this.price; }

	// Returns negative integer if object price is smaller than that of a given object, positive
	// integer if price is bigger and zero if both have equal price.
	@Override
	public int compareTo(@NonNull Item o) {
		return Math.round(this.price - o.price);
	}

	public abstract String getDisplay();

	public GraphicsComponent getGraphics() {
		return new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion(model));
	}
}
