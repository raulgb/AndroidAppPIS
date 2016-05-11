package edu.ub.pis2016.pis16.strikecom.gameplay;

import android.content.res.AssetManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

// This class is to be used to generate shop inventories.
public class InventoryManager {
	private Context context;
	private Inventory master; //inventory containing every gameObject available to the player

	// Builder.
	public InventoryManager(Context context, String turretsFile, String upgradesFile) throws IOException{
		this.master = new Inventory();
		this.context = context;
		loadTurrets(turretsFile);
		loadUpgrades(upgradesFile);
	}

	// Loads turret objects from assets to master inventory.
	private void loadTurrets(String fileName) throws IOException  {
		AssetManager am = context.getAssets();

		InputStream is = am.open(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		while((line = reader.readLine()) != null){
			master.addItem(TurretItem.parseTurretItem(line));
		}
		reader.close();
		is.close();
	}

	// Loads upgrade objects from assets to master inventory.
	private void loadUpgrades(String fileName) throws IOException  {
		AssetManager am = context.getAssets();

		InputStream is = am.open(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		while((line = reader.readLine()) != null){
			master.addItem(UpgradeItem.parseUpgradeItem(line));
		}
		reader.close();
		is.close();
	}

	// Adds a given amount of fuel canisters to inventory. Fuel descriptor taken from strings.xml
	private void addFuel(Inventory inventory, int num) {
		String fuel = context.getString(R.string.fuel_canister);
		for(int i=0; i<num; i++) {
			inventory.addItem( UpgradeItem.parseUpgradeItem(fuel) );
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
			shop.addItem( master.getTurret(i) );
			addedTurrets++;
		}
		while (addedUpgrades < numUpgrades){
			i = random.nextInt(master.getUpgradeSize());
			shop.addItem( master.getUpgrade(i) );
			addedUpgrades++;
		}
		addFuel(shop, random.nextInt(5)+1);

		return shop;
	}
}