package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ub.pis2016.pis16.strikecom.fragments.InventoryFragment;

public class Inventory {
	private ArrayList<Item> turretList;
	private ArrayList<Item> upgradeList;

	// Builder
	public Inventory(){
		turretList = new ArrayList<>();
		upgradeList = new ArrayList<>();
	}

	// Length
	public int getSize() {
		return turretList.size() + upgradeList.size();
	}
	public int getTurretSize() {
		return turretList.size();
	}
	public int getUpgradeSize() {
		return upgradeList.size();
	}

	// Get the item occupying given position.
	public TurretItem getTurret(int i) {
		if (i >= getTurretSize()){
			return null;
		}
		return (TurretItem) turretList.get(i);
	}
	public UpgradeItem getUpgrade(int i) {
		if (i >= getUpgradeSize()){
			return null;
		}
		return (UpgradeItem) upgradeList.get(i);
	}

	// Sorted addition
	public void addItem(TurretItem item) {
		if (item == null){
			return;
		}
		for(int i=0; i<turretList.size(); i++) {
			if(item.compareTo(turretList.get(i)) < 0){
				turretList.add(i, item);
				return;
			}
		}
		turretList.add(item);
	}
	public void addItem(UpgradeItem item) {
		if (item == null){
			return;
		}
		if (!UpgradeItem.functionIsSupported(item.getFunction())){
			return;
		}
		// upgrades sorted backwards, so fuel stays at the end of the list
		for(int i=0; i<upgradeList.size(); i++) {
			if(item.compareTo(upgradeList.get(i)) >= 0){
				upgradeList.add(i, item);
				return;
			}
		}
		upgradeList.add(item);
	}

	// Subtraction
	public void removeTurret(int i) {
		turretList.remove(i);
	}
	public void removeUpgrade(int i) {
		upgradeList.remove(i);
	}
	public void removeItem(TurretItem item) {
		turretList.remove(item);
	}
	public void removeItem(UpgradeItem item) {
		upgradeList.remove(item);
	}

	// Return lists
	public List<Item> getTurretInventory() {
		return turretList;
	}
	public List<Item> getUpgradeInventory() {
		return upgradeList;
	}

}
