package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.physics.ContactListener;
import edu.ub.pis2016.pis16.strikecom.fragments.InventoryFragment;
import edu.ub.pis2016.pis16.strikecom.fragments.MiniMapFragment;
import edu.ub.pis2016.pis16.strikecom.fragments.ShopFragment;
import edu.ub.pis2016.pis16.strikecom.fragments.SidebarFragment;
import edu.ub.pis2016.pis16.strikecom.controller.SidebarEventListener;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.fragments.SlotsFragment;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryManager;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBase;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;

public class FragmentedGameActivity extends Activity {

	PowerManager.WakeLock wakeLock;

	public StrikeComGLGame game;
	public SidebarFragment sidebar;

	public HashMap<String, Object> playerState = new HashMap<>();
	public HashMap<String, Inventory> shopMap = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int selectedConfig = getIntent().getIntExtra("config", 1);
		SidebarFragment.strikeBaseModel = StrikeBaseConfig.Model.values()[selectedConfig];
		SlotsFragment.strikeBaseModel = StrikeBaseConfig.Model.values()[selectedConfig];
		DummyGLScreen.strikeBaseModel = StrikeBaseConfig.Model.values()[selectedConfig];

		// Hide window decorations
		hideSystemUI();

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

		playerState.put("SCRAP", 50);
		playerState.put("FUEL", 100f);
		playerState.put("POINTS", 0);
		playerState.put("INVENTORY", new Inventory());

		// Thread updating sidebar once per second
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							sidebar.updateScrap((Integer) playerState.get("SCRAP"));
							sidebar.updateFuel((Float) playerState.get("FUEL"));
						}
					});
				}
			}
		}).start();


		shopMap.put("shop_1", null);
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

		// Hide window decorations
		hideSystemUI();

		wakeLock.acquire();

		game.setSidebarListener(new SidebarEventListener(game) {
			@Override
			public void onClickMinimap() {
				// TODO Alexander
				showMiniMapDialog();
			}

			@Override
			public void onClickInventory() {
				showInventoryDialog(-1, true, true);
			}

			@Override
			public void onClickTurret(int index) {
				showInventoryDialog(index, true, false);
			}

			@Override
			public void onClickUpgrade(int index) {
				showInventoryDialog(index, false, false);
			}
		});

	}

	public void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	@Override
	public void onBackPressed() {
		//Ask the user if he/she really wants to exit game
		pauseGame();

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.alert_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		Button dialogYes = (Button) dialog.findViewById(R.id.btnYes);
		dialogYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// return to main menu

				Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});

		Button dialogNo = (Button) dialog.findViewById(R.id.btnNo);
		dialogNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				resumeGame();
				dialog.dismiss();
			}
		});

		dialog.show();
			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

			(builder.create()).show();*/
		/*}*/
	}

	/**
	 * shows minimap dialogue window
	 */
	public void showMiniMapDialog() {
		//game.getCurrentScreen().pauseGame(); // pause game

		MiniMapFragment miniMapFrag = new MiniMapFragment();
		miniMapFrag.show(getFragmentManager(), "MiniMap");
	}

	public void showInventoryDialog(int selectedSlot, boolean turretIsSelected, boolean switchIsEnabled) {
		pauseGame();

		InventoryFragment inventoryFrag = new InventoryFragment();
		inventoryFrag.setInventory((Inventory) playerState.get("INVENTORY"));
		inventoryFrag.setSelectedSlot(selectedSlot);
		inventoryFrag.setSwitchListEnabled(switchIsEnabled);
		inventoryFrag.setTurretSelection(turretIsSelected);
		inventoryFrag.setPlayerScrap((Integer) playerState.get("SCRAP"));
		inventoryFrag.setPlayerFuel((Float) playerState.get("FUEL"));
		inventoryFrag.show(getFragmentManager(), "Inventory_Fragment");

	}

	public void showSlotsDialog(Item selectedItem, boolean turretIsSelected) {
		pauseGame();

		Screen screen = game.getCurrentScreen();
		StrikeBase strikeBase = screen.getGameObject("StrikeBase", StrikeBase.class);

		SlotsFragment slots = new SlotsFragment();
		slots.setNewItem(selectedItem);
		slots.setTurretSelection(turretIsSelected);
		slots.show(getFragmentManager(), "slots");

	}

	public void showShopDialog(String shopID) {
		pauseGame();

		ShopFragment shopFragment = new ShopFragment();
		shopFragment.setId(shopID);
		shopFragment.setInventory(shopMap.get(shopID));
		shopFragment.setPlayerScrap((Integer) playerState.get("SCRAP"));
		shopFragment.setPlayerFuel((Float) playerState.get("FUEL"));
		shopFragment.show(getFragmentManager(), "shop");

	}

	public void equipTurret(TurretItem item, int slot) {
		Screen screen = game.getCurrentScreen();
		StrikeBase strikeBase = screen.getGameObject("StrikeBase", StrikeBase.class);
		retrieveTurret(slot);
		strikeBase.addTurret(item, slot);

		Inventory inv = (Inventory) playerState.get("INVENTORY");
		inv.removeItem(item);
		playerState.put("INVENTORY", inv);

		Button slotBtn = (Button) sidebar.getTurretSlot(slot);
		int imageID = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
		Bitmap original = BitmapFactory.decodeResource(getResources(), imageID);
		Bitmap b = Bitmap.createScaledBitmap(original, slotBtn.getWidth(), slotBtn.getHeight(), false);
		Drawable d = new BitmapDrawable(getResources(), b);
		slotBtn.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

		resumeGame();
	}

	public void equipUpgrade(UpgradeItem item, int slot) {
		Screen screen = game.getCurrentScreen();
		StrikeBase strikeBase = screen.getGameObject("StrikeBase", StrikeBase.class);
		retrieveUpgrade(slot);
		strikeBase.addUpgrade(item, slot);

		Inventory inv = (Inventory) playerState.get("INVENTORY");
		inv.removeItem(item);
		playerState.put("INVENTORY", inv);

		Button slotBtn = (Button) sidebar.getUpgradeSlot(slot);
		int imageID = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
		Bitmap original = BitmapFactory.decodeResource(getResources(), imageID);
		Bitmap b = Bitmap.createScaledBitmap(original, slotBtn.getWidth(), slotBtn.getHeight(), false);
		Drawable d = new BitmapDrawable(getResources(), b);
		slotBtn.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

		resumeGame();
	}

	public void useFuel(UpgradeItem fuel) {
		Inventory inv = (Inventory) playerState.get("INVENTORY");
		inv.removeItem(fuel);
		playerState.put("INVENTORY", inv);
		playerState.put("FUEL", (Float) playerState.get("FUEL") + 250f);

		updateFuelCounter();
		resumeGame();
	}

	public void buyItem(String shopID, TurretItem item) {
		Inventory playerInventory = (Inventory) playerState.get("INVENTORY");
		Inventory shopInventory = shopMap.get(shopID);
		int scrap = (Integer) playerState.get("SCRAP");

		shopInventory.removeItem(item);
		scrap -= item.getPrice();
		playerInventory.addItem(item);

		playerState.put("INVENTORY", playerInventory);
		playerState.put("SCRAP", scrap);
	}

	public void buyItem(String shopID, UpgradeItem item) {
		Inventory playerInventory = (Inventory) playerState.get("INVENTORY");
		Inventory shopInventory = shopMap.get(shopID);
		int scrap = (Integer) playerState.get("SCRAP");

		shopInventory.removeItem(item);
		scrap -= item.getPrice();

		if (item.isFuel()) {
			playerState.put("FUEL", (Float) playerState.get("FUEL") + 250f);
		} else {
			playerInventory.addItem(item);
			playerState.put("INVENTORY", playerInventory);
		}
		playerState.put("SCRAP", scrap);
	}

	private void retrieveTurret(int slot) {
		StrikeBase strikeBase = (game.getCurrentScreen()).getGameObject("StrikeBase", StrikeBase.class);
		TurretItem turret = strikeBase.getTurret(slot);

		if (turret != null) {
			strikeBase.removeTurret(slot);
			Inventory inv = (Inventory) playerState.get("INVENTORY");
			inv.addItem(turret);
			playerState.put("INVENTORY", inv);
		}
	}

	private void retrieveUpgrade(int slot) {
		StrikeBase strikeBase = (game.getCurrentScreen()).getGameObject("StrikeBase", StrikeBase.class);
		UpgradeItem upgrade = strikeBase.getUpgrade(slot);

		if (upgrade != null) {
			strikeBase.removeUpgrade(slot);
			Inventory inv = (Inventory) playerState.get("INVENTORY");
			inv.addItem(upgrade);
			playerState.put("INVENTORY", inv);
		}
	}

	public void generateInventories() {
		String turretsFile = getString(R.string.turretsFile);
		String upgradesFile = getString(R.string.upgradesFile);
		try {
			InventoryManager im = new InventoryManager(this, turretsFile, upgradesFile);
			playerState.put("INVENTORY", im.getStartingInventory());

			for (String key : shopMap.keySet()) {
				shopMap.put(key, im.getShopInventory(20, 2));
			}

		} catch (IOException ex) {
		}
	}

	public void pauseGame() {
		game.getCurrentScreen().pauseGame(); // pause game
	}

	public void resumeGame() {
		game.getCurrentScreen().resumeGame();
	}

	public void updateScrapCounter() {
		sidebar.updateScrap((Integer) playerState.get("SCRAP"));
	}

	public void updateFuelCounter() {
		sidebar.updateFuel((Float) playerState.get("FUEL"));
	}

}

