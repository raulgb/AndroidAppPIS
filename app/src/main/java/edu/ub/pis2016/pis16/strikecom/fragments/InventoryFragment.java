package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.InventoryItemAdapter;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;


public class InventoryFragment extends DialogFragment {

	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MK2;

	protected Inventory inventory;

	protected Button equipBtn;
	protected Button cancelBtn;
	protected ListView itemList;

	protected ImageView itemImage;
	protected TextView itemDesc;
	protected TextView scrapText;
	protected TextView fuelText;

	protected int playerScrap = 0;
	protected float playerFuel = 0f;
	protected int selectedItem;
	protected int selectedSlot = -1;

	protected boolean turretIsSelected = true;
	protected boolean switchListEnabled = true;

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setPlayerScrap(int playerScrap) {
		this.playerScrap = playerScrap;
	}

	public void setPlayerFuel(float playerFuel) {
		this.playerFuel = playerFuel;
	}

	public void setSelectedSlot(int selectedSlot) {
		this.selectedSlot = selectedSlot;
	}

	public void setTurretSelection(boolean turretIsSelected) {
		this.turretIsSelected = turretIsSelected;
	}

	public void setSwitchListEnabled(boolean isEnabled) {
		this.switchListEnabled = isEnabled;
	}

	public TurretItem getSelectedTurretItem() {
		if (selectedItem < 0) {
			return null;

		}
		return inventory.getTurret(selectedItem);
	}

	public UpgradeItem getSelectedUpgradeItem() {
		if (selectedItem < 0) {
			return null;
		}
		return inventory.getUpgrade(selectedItem);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_inventory, container);
		final Typeface myCustomFont= Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.game_font));

		/** Look up all UI Elements */
		LinearLayout outerFrame = (LinearLayout) view.findViewById(R.id.outerFrame);
		LinearLayout itemDescFrame = (LinearLayout) view.findViewById(R.id.itemDescFrame);

		equipBtn = (Button) view.findViewById(R.id.inventoryBtn_1);
		cancelBtn = (Button) view.findViewById(R.id.inventoryBtn_2);

		Button turretSelectionBtn = (Button) view.findViewById(R.id.turretSelectionBtn);
		Button upgradeSelectionBtn = (Button) view.findViewById(R.id.upgradeSelectionBtn);

		itemList = (ListView) view.findViewById(R.id.itemList); // list of items
		// Right side detailed display
		itemImage = (ImageView) view.findViewById(R.id.itemDescImage); // Image of the item selected from the list
		itemDesc = (TextView) view.findViewById(R.id.itemDesc); // description of the item selected from the list

		scrapText = (TextView) view.findViewById(R.id.playerScrap);
		fuelText = (TextView) view.findViewById(R.id.playerFuel);
		scrapText.setText(String.format("%04d", playerScrap));
		fuelText.setText(String.format("%04d", MathUtils.round(playerFuel)));
		/** FINISHED LOOKUP */

		// Apply the same custom font to all UI elements
		equipBtn.setTypeface(myCustomFont);
		cancelBtn.setTypeface(myCustomFont);
		turretSelectionBtn.setTypeface(myCustomFont);
		upgradeSelectionBtn.setTypeface(myCustomFont);
		itemDesc.setTypeface(myCustomFont);
		scrapText.setTypeface(myCustomFont);
		fuelText.setTypeface(myCustomFont);

		// Get strikebase model name and find relevant resources
		String modeSuffix = strikeBaseModel.toString().toLowerCase();

		int frameResID = getResources().getIdentifier("frame_retro_" + modeSuffix, "drawable", getActivity().getPackageName());
		int buttonResId = getResources().getIdentifier("btn_retro_" + modeSuffix, "drawable", getActivity().getPackageName());
		int buttonUpgradeResId = getResources().getIdentifier("btn_retro_upgrade_" + modeSuffix, "drawable", getActivity().getPackageName());

		outerFrame.setBackgroundResource(frameResID);
		itemDescFrame.setBackgroundResource(frameResID);

		equipBtn.setBackgroundResource(buttonResId);
		cancelBtn.setBackgroundResource(buttonResId);
		turretSelectionBtn.setBackgroundResource(buttonResId);
		upgradeSelectionBtn.setBackgroundResource(buttonResId);

		configButtons();

		// Switch to turret selection
		turretSelectionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!turretIsSelected) {
					turretIsSelected = true;
					itemDesc.setText("");
					itemImage.setBackgroundResource(0);
					selectedItem = -1;
					fillItemList();
				}
			}
		});


		// Switch to upgrades selection
		upgradeSelectionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (turretIsSelected) {
					turretIsSelected = false;
					itemDesc.setText("");
					itemImage.setBackgroundResource(0);
					selectedItem = -1;
					fillItemList();
				}
			}
		});

		if (!switchListEnabled) {
			turretSelectionBtn.setEnabled(false);
			turretSelectionBtn.setText("");
			itemImage.setBackgroundResource(0);
			turretSelectionBtn.setBackgroundColor(Color.TRANSPARENT);
			upgradeSelectionBtn.setEnabled(false);
			upgradeSelectionBtn.setText("");
			itemImage.setBackgroundResource(0);
			upgradeSelectionBtn.setBackgroundColor(Color.TRANSPARENT);
		}

		fillItemList();

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		return view;
	}

//	public void show(FragmentManager manager, String tag) {
//	}


	/** Resume game and enable Immersive mode on activity */
	public void onDismiss(DialogInterface dialog) {
		FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
		callingActivity.hideSystemUI();
		callingActivity.resumeGame();
	}

	protected void configButtons() {
		// Equip/unequip button
		equipBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedItem >= 0) { // valid item selected
					dismiss();
					FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();

					if (turretIsSelected) { // selected item is a turret
						TurretItem turret = inventory.getTurret(selectedItem);
						if (selectedSlot < 0) { // show slots dialog if no valid slot has been selected
							callingActivity.showSlotsDialog(turret, true);
						} else { // equip if slot already selected
							callingActivity.equipTurret(turret, selectedSlot);
						}
					} else {// selected item is an upgrade
						UpgradeItem upgrade = inventory.getUpgrade(selectedItem);
						if (upgrade.isFuel()) { // use fuel if selected
							callingActivity.useFuel(upgrade);
						} else if (upgrade.isRepair()) { // use repair if selected
							callingActivity.useRepair(upgrade);
						} else {
							if (selectedSlot < 0) { // show slots dialog if no valid slot has been selected
								callingActivity.showSlotsDialog(upgrade, false);
							} else { // equip if slot already selected
								callingActivity.equipUpgrade(upgrade, selectedSlot);
							}
						}
					}
				}
			}
		});

		// Cancel button
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().dismiss();
			}
		});
	}

	protected void fillItemList() {
		if (turretIsSelected) {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getTurretInventory()));
		} else {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getUpgradeInventory()));
		}

		itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				equipBtn.setText(getString(R.string.equip_button));
				if (i >= 0) { //valid item is selected
					selectedItem = i;

					if (turretIsSelected) {
						TurretItem turretItem = inventory.getTurret(selectedItem);
						itemDesc.setText(turretItem.getDisplayText());

						// Get resource ID for custom image and set it to the ImageView
						String imageName = turretItem.getImage() + "_big";
						int resId = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
						itemImage.setBackgroundResource(resId);

					} else {
						itemDesc.setText(inventory.getUpgrade(selectedItem).getDisplayText());
						if (inventory.getUpgrade(selectedItem).isFuel() || inventory.getUpgrade(selectedItem).isRepair()) {
							equipBtn.setText(getString(R.string.use_item));
						}
					}
				}
			}
		});
	}

}
