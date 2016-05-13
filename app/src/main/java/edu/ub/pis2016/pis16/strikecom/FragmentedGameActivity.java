package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.fragments.InventoryFragment;
import edu.ub.pis2016.pis16.strikecom.fragments.MiniMapFragment;
import edu.ub.pis2016.pis16.strikecom.fragments.SidebarFragment;
import edu.ub.pis2016.pis16.strikecom.controller.SidebarEventListener;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.fragments.SlotsFragment;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryManager;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBaseTest;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

public class FragmentedGameActivity extends Activity {

	PowerManager.WakeLock wakeLock;

	StrikeComGLGame game;
	SidebarFragment sidebar;

	HashMap<String, Object> playerState = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide window decorations
		hideStatusAndNavBar();

		// Obtain wakelock (to keep the screen on)
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");

		// Load layout with both fragments defined
		setContentView(R.layout.activity_fragmented_game);

		// Get a reference to both fragments
		game = (StrikeComGLGame) getFragmentManager().findFragmentById(R.id.gameFragment);
		sidebar = (SidebarFragment) getFragmentManager().findFragmentById(R.id.sidebarFragment);

		// Give the sidebar fragment a reference to the game fragment.
		sidebar.setGame(game);

		playerState.put("SCRAP", Integer.valueOf(getString(R.string.res1_defVal)));
		playerState.put("FUEL", Integer.valueOf(getString(R.string.res2_defVal)));
		playerState.put("POINTS", 0);
		//playerState.put("INVENTORY", new Inventory());

		generateInventories();
	}

	@Override
	protected void onPause() {
		super.onPause();
		wakeLock.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		wakeLock.acquire();

		game.setSidebarListener(new SidebarEventListener(game) {
			@Override
			public void onClickMinimap() {
				// TODO Alexander
				showMiniMapDialog();
			}

			@Override
			public void onClickInventory() {
				showInventoryDialog(true, -1);
			}

			@Override
			public void onClickTurret(int index) {
				showInventoryDialog(true, index);
			}

			@Override
			public void onClickUpgrade(int index) {
				showInventoryDialog(false, index);
			}
		});
	}

	private void hideStatusAndNavBar() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	@Override
	public void onBackPressed() {
		//Ask the user if he/she really wants to exit game

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(getString(R.string.quit_alert));
		builder.setPositiveButton(getString(R.string.alert_positive), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// return to main menu
				Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});
		builder.setNegativeButton(getString(R.string.alert_negative), null);

		(builder.create()).show();
	}

	/**
	 * shows minimap dialogue window
	 */
	public void showMiniMapDialog() {
		game.getCurrentScreen().pauseGame(); // pause game


		MiniMapFragment miniMapFrag = new MiniMapFragment();
		/*inventoryFrag.setInventory((Inventory) playerState.get("INVENTORY"));
		inventoryFrag.setSelectedSlot(selectedSlot);
		inventoryFrag.setTurretSelection(turretIsSelected);
		inventoryFrag.show(getFragmentManager(), "Inventory_Fragment");*/
		miniMapFrag.show(getFragmentManager(), "MiniMap");
	}

	public void showInventoryDialog(boolean turretIsSelected, int selectedSlot) {
		game.getCurrentScreen().pauseGame(); // pause game

		InventoryFragment inventoryFrag = new InventoryFragment();
		inventoryFrag.setInventory((Inventory) playerState.get("INVENTORY"));
		inventoryFrag.setSelectedSlot(selectedSlot);
		inventoryFrag.setTurretSelection(turretIsSelected);
		inventoryFrag.show(getFragmentManager(), "Inventory_Fragment");
	}

	public void showSlotsDialog(boolean turretIsSelected, Item selectedItem) {
		Screen screen = game.getCurrentScreen();
		StrikeBaseTest strikeBase = screen.getGameObject("StrikeBase", StrikeBaseTest.class);

		screen.pauseGame(); // pause game

		SlotsFragment slots = new SlotsFragment();
		slots.setStrikeBaseModel(strikeBase.getConfig().model);
		slots.setNewItem(selectedItem);
		slots.setTurretSelection(turretIsSelected);
		slots.show(getFragmentManager(), "slots");
	}

	public void equipTurret(TurretItem item, int slot) {
		Screen screen = game.getCurrentScreen();
		StrikeBaseTest strikeBase = screen.getGameObject("StrikeBase", StrikeBaseTest.class);
		retrieveTurret(slot);
		strikeBase.addTurret(item, slot);

		Inventory inv = (Inventory) playerState.get("INVENTORY");
		inv.removeItem(item);
		playerState.put("INVENTORY", inv);

		// TODO Find a better way to display equipped items on the sidebar
		Button slotBtn = (Button) sidebar.getTurretSlot(slot);
		int imageID = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
		Bitmap original = BitmapFactory.decodeResource(getResources(), imageID);
		Bitmap b = Bitmap.createScaledBitmap(original, slotBtn.getWidth(), slotBtn.getHeight(), false);
		Drawable d = new BitmapDrawable(getResources(), b);
		slotBtn.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

		screen.resumeGame(); // resume game
	}

	public void equipUpgrade(UpgradeItem item, int slot) {
		Screen screen = game.getCurrentScreen();
		StrikeBaseTest strikeBase = screen.getGameObject("StrikeBase", StrikeBaseTest.class);
		retrieveUpgrade(slot);
		strikeBase.addUpgrade(item, slot);

		Inventory inv = (Inventory) playerState.get("INVENTORY");
		inv.removeItem(item);
		playerState.put("INVENTORY", inv);

		// TODO Find a better way to display equipped items on the sidebar

		Button slotBtn = (Button) sidebar.getUpgradeSlot(slot);
		int imageID = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
		Bitmap original = BitmapFactory.decodeResource(getResources(), imageID);
		Bitmap b = Bitmap.createScaledBitmap(original, slotBtn.getWidth(), slotBtn.getHeight(), false);
		Drawable d = new BitmapDrawable(getResources(), b);
		slotBtn.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

		screen.resumeGame(); // resume game
	}

	public void useFuel(UpgradeItem fuel){
		Inventory inv = (Inventory) playerState.get("INVENTORY");
		inv.removeItem(fuel);
		playerState.put("INVENTORY", inv);
		playerState.put("FUEL", (Integer)playerState.get("FUEL") + 250);

		sidebar.updateFuel( (Integer)playerState.get("FUEL")  );
		game.getCurrentScreen().resumeGame(); // resume game
	}

	private void retrieveTurret(int slot) {
		StrikeBaseTest strikeBase = (game.getCurrentScreen()).getGameObject("StrikeBase", StrikeBaseTest.class);
		TurretItem turret = strikeBase.getTurret(slot);

		if(turret != null){
			strikeBase.removeTurret(slot);
			Inventory inv = (Inventory) playerState.get("INVENTORY");
			inv.addItem(turret);
			playerState.put("INVENTORY", inv);

			Button view = (Button) sidebar.getTurretSlot(slot);
			//view.setCompoundDrawables(null, null, null, null);
		}
	}

	private void retrieveUpgrade(int slot) {
		StrikeBaseTest strikeBase = (game.getCurrentScreen()).getGameObject("StrikeBase", StrikeBaseTest.class);
		UpgradeItem upgrade = strikeBase.getUpgrade(slot);

		if(upgrade != null){
			strikeBase.removeUpgrade(slot);
			Inventory inv = (Inventory) playerState.get("INVENTORY");
			inv.addItem(upgrade);
			playerState.put("INVENTORY", inv);

			Button view = (Button) sidebar.getTurretSlot(slot);
			//view.setCompoundDrawables(null, null, null, null);
		}
	}

	public void generateInventories() {
		String turretsFile = getString(R.string.turretsFile);
		String upgradesFile = getString(R.string.upgradesFile);
		try {
			InventoryManager im = new InventoryManager(this, turretsFile, upgradesFile);
			playerState.put("INVENTORY", im.getShopInventory(10, 1));

		} catch (IOException ex){
			Inventory testInventory = new Inventory();
			testInventory.addItem( TurretItem.parseTurretItem("Machinegun;machinegun_64;turret_mk1;Weak yet cheap, makes an ideal weapon for a newbie.;100;2;4;1"));
			testInventory.addItem( UpgradeItem.parseUpgradeItem("Composite armour;composite_64;turret_mk1;Advanced plating made from a " +
					"variety of metals and ceramics.;COMPOSITE;4000"));
			playerState.put("INVENTORY", testInventory);
		}
	}
}

