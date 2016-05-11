package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
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
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryItemAdapter;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

public class InventoryFragment extends DialogFragment {

	protected StrikeComGLGame game;

	protected Inventory inventory;

	protected Button equipBtn;
	protected Button cancelBtn;
	protected ListView itemList;
	protected TextView itemDesc;

	protected int selectedItem;
	protected int selectedSlot = -1;

	protected boolean turretIsSelected = true;

	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}

	public void setInventory(Inventory inventory) { this.inventory = inventory; }

	public void setSelectedSlot(int selectedSlot) { this.selectedSlot = selectedSlot; }

	public void setTurretSelection(boolean turretIsSelected) {
		this.turretIsSelected = turretIsSelected;
	}

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
				if(turretIsSelected) {
					turretIsSelected = false;
					itemDesc.setText("");
					selectedItem = -1;
					fillItemList();
				}
			}
		});

		fillItemList();

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}

	protected void configButtons() {
		// Equip/unequip button
		equipBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(selectedItem >= 0) { // valid item selected
					dismiss();
					FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
					if(selectedItem >= 0) { // valid item selection
						if (turretIsSelected) {
							callingActivity.showSlotsDialog(true, inventory.getTurret(selectedItem));
						} else {
							UpgradeItem item = inventory.getUpgrade(selectedItem);
							if(item.isFuel()){
								callingActivity.useFuel(item);
							} else {
								callingActivity.showSlotsDialog(false, item);
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
