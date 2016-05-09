package edu.ub.pis2016.pis16.strikecom.gameplay;

import android.content.res.AssetManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

// This class is to be used to generate shop inventories.
public class InventoryManager {
	private Inventory master; //inventory containing every gameObject available to the player

	// Builder.
	public InventoryManager(Context context, String turretsFile, String upgradesFile) throws IOException{
		this.master = new Inventory();
		loadTurrets(context, turretsFile);
		//loadUpgrades(context, upgradesFile);
	}

	// Loads turret objects from assets to master inventory.
	private void loadTurrets(Context context, String fileName) throws IOException  {
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
	private void loadUpgrades(Context context, String fileName) throws IOException  {
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

	// Returns a new, freshly generated inventory with given size.
	public Inventory getNewInventory(int size){
		Random r = new Random();
		Inventory inventory = new Inventory();
		while(inventory.getSize() < size){
			inventory.addItem(master.getItem(r.nextInt(master.getSize())));
		}
		return inventory;
	}

	public HashMap getMasterInventory() {
		HashMap<String, Item> masterInventory = new HashMap<>();
		for (int i=0; i<master.getSize(); i++){
			Item item = master.getItem(i);
			masterInventory.put(item.getName(), item);
		}
		return masterInventory;
	}
}