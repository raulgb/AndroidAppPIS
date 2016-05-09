package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.fragments.InventoryFragment;
import edu.ub.pis2016.pis16.strikecom.fragments.SidebarFragment;
import edu.ub.pis2016.pis16.strikecom.controller.SidebarEventListener;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.fragments.SlotsFragment;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryManager;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBaseTest;
import edu.ub.pis2016.pis16.strikecom.gameplay.Turret;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;

public class FragmentedGameActivity extends Activity {

	PowerManager.WakeLock wakeLock;

	StrikeComGLGame game;
	SidebarFragment sidebar;

	HashMap<String, Object> playerState = new HashMap<>();
	HashMap<String, Item> masterInventory = new HashMap<>();

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
		playerState.put("POINTS",0);

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
			public void onClickInventory() {
				showInventoryDialog(-1);
			}

			@Override
			public void onClickTurret(int index) {
				if (index > 3 || index == 1)
					return;

				Screen screen = game.getCurrentScreen();
				String tName = "Turret" + index;

				if (screen.getGameObject(tName) != null)
					// remove existing
					screen.removeGameObject(tName);
				else {
					// Create and put new turret
					StrikeBaseTest strikeBase = screen.getGameObject("StrikeBase", StrikeBaseTest.class);
					Turret newTurret = new Turret("turret_mk1", strikeBase, "turret_" + index);
					newTurret.getComponent(GraphicsComponent.class).getSprite().setScale(0.75f);
					newTurret.setParent(strikeBase);
					newTurret.putComponent(new TurretBehavior());
					newTurret.setLayer(Screen.LAYER_3);
					screen.addGameObject(tName, newTurret);
				}
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

	public void showInventoryDialog(int selectedSlot) {
		InventoryFragment inventoryFrag = new InventoryFragment();
		inventoryFrag.setInventory( (Inventory) playerState.get("INVENTORY") );
		inventoryFrag.setSelectedSlot(selectedSlot);
		inventoryFrag.show(getFragmentManager(), "Inventory_Fragment");
	}

	public void showSlotsDialog(Item selectedItem) {
		Screen screen = game.getCurrentScreen();
		StrikeBaseTest strikeBase = screen.getGameObject("StrikeBase", StrikeBaseTest.class);

		SlotsFragment slots = new SlotsFragment();
		slots.setStrikeBaseConfig(strikeBase.getCongig());
		slots.setNewItem(selectedItem);
		slots.show(getFragmentManager(), "slots");
	}

	public void equipItem(Item selectedItem, int slot) {

	}

	public void generateInventories() {
		String turretsFile = getString(R.string.turretsFile);
		String upgradesFile = getString(R.string.upgradesFile);
		try{
			InventoryManager im = new InventoryManager(this, turretsFile, upgradesFile);
			masterInventory = im.getMasterInventory();
			playerState.put("INVENTORY", im.getNewInventory(10));

		} catch (IOException ex){
			// TEST INVENTORY ----
			Inventory testInventory = new Inventory();
			testInventory.addItem(TurretItem.parseTurretItem("Machinegun;machinegun;Weak yet cheap, makes an ideal weapon for a newbie.;100;2;4;1"));
			testInventory.addItem(TurretItem.parseTurretItem("Gatling gun;gatling;Multi-barreled machinegun with superior firerate.;300;2;6;2"));
			testInventory.addItem(TurretItem.parseTurretItem("Battle cannon;cannon;This cannon can punch a hole through most enemies.;1500;4;2;4"));
			playerState.put("INVENTORY", testInventory);
		}
	}
}

