package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2016.pis16.strikecom.fragments.InventoryFragment;

// I've implemented this class just for the sake of having a sorted list. It might be replaced by a
// regular ArrayList. Sorting is managed during insertion. Objects sorted by price.
public class Inventory {
	private ArrayList<Item> objectList;

	// Builder
	public Inventory(){
		objectList = new ArrayList<>();
	}

	// Addition
	public void addItem(Item o){
		for(int i=0; i<objectList.size(); i++) {
			if(o.compareTo(objectList.get(i)) < 0){
				objectList.add(i, o);
				return;
			}
		}
		objectList.add(o);
	}

	// Length.
	public int getSize(){
		return objectList.size();
	}

	// Get the item occupying given position.
	public Item getItem(int i) {
		return objectList.get(i);
	}

	// Subtraction
	public void removeItem(int i) {
		objectList.remove(i);
	}

	// Returns objectList.
	public List<Item> getInventory() {
		return objectList;
	}

}
