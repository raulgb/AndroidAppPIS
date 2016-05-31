package edu.ub.pis2016.pis16.strikecom.gameplay.items;

import android.content.res.AssetManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import edu.ub.pis2016.pis16.strikecom.R;

// This class is to be used to generate shop inventories.
public class InventoryManager {
	private Context context;
	private Inventory master; //inventory containing every gameObject available to the player

	// Builder.
	public InventoryManager(Context context){
		this.master = new Inventory();
		this.context = context;

		loadTurrets();
		loadUpgrades();
	}

	// Load all turrets from strings.xml
	private void loadTurrets() {
		String[] turrets = context.getResources().getStringArray(R.array.Turrets);
		for (String turret : turrets) {
			master.addItem(TurretItem.parseTurretItem(turret));
		}
	}

	// Load all turrets from strings.xml
	private void loadUpgrades() {
		String[] upgrades = context.getResources().getStringArray(R.array.Upgrades);
		for (String upgrade : upgrades) {
			master.addItem(UpgradeItem.parseUpgradeItem(upgrade));
		}
	}

	// Loads turret objects from assets to master inventory.
	public void loadTurretsFromFile(String fileName) throws IOException {
		AssetManager am = context.getAssets();

		InputStream is = am.open(context.getString(R.string.turretsFile));
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = reader.readLine()) != null) {
			master.addItem(TurretItem.parseTurretItem(line));
		}
		reader.close();
		is.close();
	}

	// Loads upgrade objects from assets to master inventory.
	public void loadUpgradesFromFile(String fileName) throws IOException {
		AssetManager am = context.getAssets();

		InputStream is = am.open(context.getString(R.string.upgradesFile));
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = reader.readLine()) != null) {
			master.addItem(UpgradeItem.parseUpgradeItem(line));
		}
		reader.close();
		is.close();
	}

	// Adds a given amount of fuel canisters to inventory. Fuel descriptor taken from strings.xml
	private void addFuel(Inventory inventory, int num) {
		for (int i = 0; i < num; i++) {
			inventory.addItem(UpgradeItem.parseUpgradeItem(context.getString(R.string.FUEL)));
		}
	}

	private void addRepair(Inventory inventory, int num) {
		for (int i = 0; i < num; i++) {
			inventory.addItem(UpgradeItem.parseUpgradeItem(context.getString(R.string.REPAIR)));
		}
	}

	// Returns a new, freshly generated shop inventory. Each one of these contain a given number of random turrets, random number of
	// upgrades and 1 to 5 fuel canisters.
	public Inventory getShopInventory(int numTurrets, int numUpgrades) {
		Inventory shop = new Inventory();
		Random random = new Random();

		int addedTurrets = 0;
		int addedUpgrades = 0;
		int i;
		while (addedTurrets < numTurrets) {
			i = random.nextInt(master.getTurretSize());
			TurretItem turret = master.getTurret(i);
			if (!turret.isPlasmaCannon()) {
				shop.addItem(turret);
				addedTurrets++;
			}
		}
		while (addedUpgrades < numUpgrades) {
			i = random.nextInt(master.getUpgradeSize());
			shop.addItem(master.getUpgrade(i));
			addedUpgrades++;
		}
		addFuel(shop, random.nextInt(5) + 1);
		addRepair(shop, random.nextInt(5) + 1);

		return shop;
	}

	public Inventory getVaultInventory() {
		Inventory shop = new Inventory();
		Random random = new Random();

		// 4 plasma cannons, 6 upgrades, 8 fuel canisters
		for(int i=0; i< 4; i++){
			shop.addItem(master.getTurret(master.getTurretSize() -1));
		}
		int i;
		int addedUpgrades = 0;
		while (addedUpgrades < 6) {
			i = random.nextInt(master.getUpgradeSize());
			shop.addItem(master.getUpgrade(i));
			addedUpgrades++;
		}
		addFuel(shop, 8);
		addRepair(shop, 8);

		return shop;
	}

	// Returns and inventory intended to be used as the starting one for the player.
	public Inventory getStartingInventory() {
		Inventory startInventory = new Inventory();
		startInventory.addItem(master.getTurret(0));
		return startInventory;
	}
}