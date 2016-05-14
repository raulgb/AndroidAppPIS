package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryItemAdapter;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;


public class InventoryFragment extends DialogFragment {

	protected Inventory inventory;

	protected Button equipBtn;
	protected Button cancelBtn;
	protected ListView itemList;
	protected TextView itemDesc;
	protected TextView scrapText;
	protected TextView fuelText;

	protected int playerScrap = 0;
	protected int playerFuel = 0;
	protected int selectedItem;
	protected int selectedSlot = -1;

	protected boolean turretIsSelected = true;
	protected boolean switchListEnabled = true;

	public void setInventory(Inventory inventory) { this.inventory = inventory; }

	public void setPlayerScrap(int playerScrap) { this.playerScrap = playerScrap; }

	public void setPlayerFuel(int playerFuel) { this.playerFuel = playerFuel; }

	public void setSelectedSlot(int selectedSlot) { this.selectedSlot = selectedSlot; }

	public void setTurretSelection(boolean turretIsSelected) {
		this.turretIsSelected = turretIsSelected;
	}

	public void setSwitchListEnabled(boolean isEnabled) { this.switchListEnabled = isEnabled; }

	public TurretItem getSelectedTurretItem() {
		if (selectedItem < 0){
			return null;

		}
		return inventory.getTurret(selectedItem);
	}

	public UpgradeItem getSelectedUpgradeItem() {
		if (selectedItem < 0){
			return null;
		}
		return inventory.getUpgrade(selectedItem);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_inventory, container);

		itemList = (ListView) view.findViewById(R.id.itemList); // list of items
		itemDesc = (TextView) view.findViewById(R.id.itemDesc); // description of the item selected from the list
		scrapText = (TextView) view.findViewById(R.id.playerScrap);
		fuelText = (TextView) view.findViewById(R.id.playerFuel);
		scrapText.setText(Integer.toString(playerScrap));
		fuelText.setText(Integer.toString(playerFuel));

		// Equip/unequip button
		equipBtn = (Button) view.findViewById(R.id.inventoryBtn_1);
		// Cancel button
		cancelBtn = (Button) view.findViewById(R.id.inventoryBtn_2);
		configButtons();

		// Switch to turret selection
		final Button turretSelectionBtn = (Button) view.findViewById(R.id.turretSelectionBtn);
		turretSelectionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!turretIsSelected) {
					turretIsSelected = true;
					itemDesc.setText("");
					selectedItem = -1;
					fillItemList();
				}
			}
		});



		// Switch to upgrades selection
		final Button upgradeSelectionBtn = (Button) view.findViewById(R.id.upgradeSelectionBtn);
		upgradeSelectionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (turretIsSelected) {
					turretIsSelected = false;
					itemDesc.setText("");
					selectedItem = -1;
					fillItemList();
				}
			}
		});

		if(!switchListEnabled) {
			turretSelectionBtn.setEnabled(false);
			turretSelectionBtn.setText("");
			turretSelectionBtn.setBackgroundColor(Color.TRANSPARENT);
			upgradeSelectionBtn.setEnabled(false);
			upgradeSelectionBtn.setText("");
			upgradeSelectionBtn.setBackgroundColor(Color.TRANSPARENT);
		}

		fillItemList();



		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}

	public void onCancel (DialogInterface dialog){
		dismiss();
		FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
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
							callingActivity.showSlotsDialog(turret,true);
						} else { // equip if slot already selected
							callingActivity.equipTurret(turret, selectedSlot);
						}
					} else {// selected item is an upgrade
						UpgradeItem upgrade = inventory.getUpgrade(selectedItem);
						if (upgrade.isFuel()) { // use fuel if selected
							callingActivity.useFuel(upgrade);
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
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				callingActivity.resumeGame();
			}
		});
	}

	protected void fillItemList() {
		if(turretIsSelected) {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getTurretInventory()));
		} else {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getUpgradeInventory()));
		}

		itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				equipBtn.setText( getString(R.string.equip_button) );
				if(i>=0){ //valid item is selected
					selectedItem = i;

					if(turretIsSelected) {
						itemDesc.setText( inventory.getTurret(selectedItem).getDisplay() );
					} else {
						itemDesc.setText( inventory.getUpgrade(selectedItem).getDisplay() );
						if( inventory.getUpgrade(selectedItem).isFuel() ){
							equipBtn.setText( getString(R.string.use_item) );
						}
					}
				}
			}
		});
	}

}
